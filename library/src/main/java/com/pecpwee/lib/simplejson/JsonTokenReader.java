package com.pecpwee.lib.simplejson;

import static java.lang.Character.isDigit;

/**
 * Created by pw on 2017/7/31.
 */

class JsonTokenReader {
    private String mJsonStr;
    private int mIndex;

    public JsonTokenReader(String jsonStr) {
        mJsonStr = jsonStr;
        mIndex = -1;
    }

    private char getNextChar() {
        mIndex++;
        return mJsonStr.charAt(mIndex);
    }

    private void revokeCharRead() {
        mIndex--;
    }


    public Token getNextToken() {

        if (mIndex + 1 >= mJsonStr.length()) {
            return new Token(TokenType.END_DOC, "EOF");
        }
        char c = getNextChar();

        Token resultToken = null;

        if (isFitPatternStr(c, "null")) {
            resultToken = new Token(TokenType.NULL, null);
        } else if (c == ',') {
            resultToken = new Token(TokenType.COMMA, ",");
        } else if (c == ':') {
            resultToken = new Token(TokenType.COLON, ":");
        } else if (c == '{') {
            resultToken = new Token(TokenType.START_OBJ, "{");
        } else if (c == '[') {
            resultToken = new Token(TokenType.START_ARRAY, "[");
        } else if (c == ']') {
            resultToken = new Token(TokenType.END_ARRAY, "]");
        } else if (c == '}') {
            resultToken = new Token(TokenType.END_OBJ, "}");
        } else if (isFitPatternStr(c, "true")) {
            resultToken = new Token(TokenType.BOOLEAN, "true"); //the value of TRUE is not null
        } else if (isFitPatternStr(c, "false")) {
            resultToken = new Token(TokenType.BOOLEAN, "false"); //the value of FALSE is null
        } else if (c == '"') {
            resultToken = readString();
        } else if (isDigit(c) || c == '-') {//num head
            resultToken = readNum(c);
        } else {
            throwParseError();
        }
        return resultToken;
    }

    private boolean isASCControlChar(char c) {
        return c < 32 || c == 127;
    }

    /*
    * number = int | int frac | int exp | int frac exp
    * int = digit | digit1-9 digits  | - digit | - digit1-9 digits
    * frac = . digits
    * exp = e digits
    * digits = digit | digit digits
    * e = e | e+ | e-  | E | E+ | E-
    *
    * 54.5 -44.44E-433
    * */
    private Token readNum(char firstchar) {
        StringBuilder numConstructCache = new StringBuilder();
        char c = firstchar;

        while (true) {
            if (isLegalNumChar(c)) {
                numConstructCache.append(c);
            } else {
                revokeCharRead();
                break;
            }
            c = getNextChar();
        }
        return new Token(TokenType.NUMBER, numConstructCache.toString()); //the value of 0 is null
    }

    //we assume the json is legal. not do check.the error will happen when convering it to java object
    private boolean isLegalNumChar(char c) {
        return c == '-'
                || c == '+'
                || (c >= '0' && c <= '9')
                || c == 'e'
                || c == 'E'
                || c == '.';
    }

    //pattern will be true,false,
    private boolean isFitPatternStr(char startC, String patternStr) {

        if (startC != patternStr.charAt(0)) {
            return false;
        }
        char inputChar = ' ';
        char patternchar = ' ';
        for (int i = 1; i < patternStr.length(); i++) {//the post
            patternchar = patternStr.charAt(i);
            inputChar = getNextChar();
            if (inputChar != patternchar) {
                throwParseError();
            }

        }
        return true;
    }

    private Token readString() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = getNextChar();

            if (c == '\\') {//isEscape
                sb.append(getUnescapeChar());
            } else if (c == '"') {
                break;
            } else if (c == '\r' || c == '\n') {
                throwParseError();
            } else {
                sb.append(c);
            }
        }
        return new Token(TokenType.STRING, sb.toString());

    }


    private char getUnescapeChar() {

        char c = getNextChar();
        switch (c) {
            case '"':
                return '\"';
            case '\\':
                return '\\';
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            case '/':
                return '/';
            case 'u': {
                StringBuilder unicodesb = new StringBuilder();
                for (int i = 0; i < 4; i++) {
                    unicodesb.append(getNextChar());
                }
                return (char) Integer.parseInt(unicodesb.toString(), 16);
            }
            default:
                throwParseError();
        }
        throwParseError();
        return 0;
    }


    private void throwParseError() {
        throw new IllegalArgumentException("illegal JSON String.the error happend in the end of:" + mJsonStr.substring(0, mIndex));
    }
}
