package com.pecpwee.lib.simplejson;

/**
 * Created by pw on 2017/7/31.
 */

class TokenType {
    public static final int START_OBJ = 0;
    public static final int END_OBJ = START_OBJ + 1;
    public static final int START_ARRAY = END_OBJ + 1;
    public static final int END_ARRAY = START_ARRAY + 1;
    public static final int NULL = END_ARRAY + 1;
    public static final int NUMBER = NULL + 1;
    public static final int STRING = NUMBER + 1;
    public static final int BOOLEAN = STRING + 1;
    public static final int COLON = BOOLEAN + 1;
    public static final int COMMA = COLON + 1;
    public static final int END_DOC = COMMA + 1;
}
