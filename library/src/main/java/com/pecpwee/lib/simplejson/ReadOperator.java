package com.pecpwee.lib.simplejson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pw on 2017/7/31.
 */

public class ReadOperator<T> {


    private JsonTokenReader reader;

    public ReadOperator(JsonTokenReader reader) {
        this.reader = reader;
    }


    private Token getNextToken() {
        Token token = reader.getNextToken();
        if (token.type == TokenType.END_DOC) {
            throwTokenError();
        }
        return token;
    }


    private Object readObj(Class clazz, Token startToken) {
        if (startToken.type != TokenType.START_OBJ) {
            throwTokenError();
        }

        Token token = null;
        Object instanceObj = Utils.createInstance(clazz);

        token = getNextToken();

        if (token.type == TokenType.END_OBJ) {
            return instanceObj;
        }

        while (true) {
            String memberName = null;
            if (token.type == TokenType.STRING) {//field pair key
                memberName = token.value;
            } else {
                throwTokenError();
            }
            token = getNextToken();
            if (token.type != TokenType.COLON) {//:
                throwTokenError();
            }
            Field memberField = null;
            if (memberName == null) {
                break;
            }


            memberField = findFieldInWholeLevel(instanceObj, memberName);
            if (memberField != null) {
                memberField.setAccessible(true);
                Object fieldValueObj = dispatchParse(memberField.getType(), memberField, null);
                try {
                    memberField.set(instanceObj, fieldValueObj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                consumeTheFieldValue();
            }
            token = getNextToken();
            //READ ','
            if (token.type == TokenType.COMMA) {
                token = getNextToken();//read the name String of the next field
                continue;
            } else if (token.type == TokenType.END_OBJ) {
                return instanceObj;
            }

        }
        throwTokenError();
        return null;
    }

    private Field findFieldInWholeLevel(Object object, String fieldName) {
        Class currentLevelClass = object.getClass();
        Field resultField = null;
        while (true) {
            try {
                resultField = currentLevelClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
            if (resultField != null) {
                return resultField;
            }
            currentLevelClass = currentLevelClass.getSuperclass();
            if (currentLevelClass == null) {
                return null;
            }
        }

    }

    /**
     * @param clazz
     * @param field      this is for object parsing
     * @param startToken this is for not revoke read
     * @return
     */
    public Object dispatchParse(Class clazz, Field field, Token startToken) {
        Token token = startToken;
        if (token == null) {
            token = getNextToken();
        }
        if (token.type == TokenType.START_OBJ) {

            return readObj(clazz, token);
        } else if (token.type == TokenType.STRING) {
            return token.value;
        } else if (token.type == TokenType.NUMBER) {
            return parseNumber(clazz, token);
        } else if (token.type == TokenType.START_ARRAY) {
            return parseArrayOrList(clazz, field, token);
        } else if (token.type == TokenType.BOOLEAN) {
            return Boolean.parseBoolean(token.value);
        } else if (token.type == TokenType.NULL) {
            return null;
        }
        throwTokenError();
        return null;
    }

    private void consumeTheFieldValue() {// TODO: 2017/7/31 test

        Token token = getNextToken();
        int startType = -1;
        int endType = -1;
        if (token.type == TokenType.START_ARRAY) {
            startType = TokenType.START_ARRAY;
            endType = TokenType.END_ARRAY;
        } else if (token.type == TokenType.START_OBJ) {
            startType = TokenType.START_OBJ;
            endType = TokenType.END_OBJ;
        } else {
            return;
        }
        int pairCount = 1;
        while (true) {
            token = getNextToken();
            if (token.type == startType) {
                pairCount++;
            } else if (token.type == endType) {
                pairCount--;
                if (pairCount == 0) {
                    break;
                }
            }
        }
    }

    private Object parseArrayOrList(Class clazz, Field field, Token startToken) {

        if (clazz.isArray()) {
            Class componentType = clazz.getComponentType();
            ArrayList componentValueList = new ArrayList();
            parseComponent2List(componentValueList, componentType, startToken);
            Object arrayObj = Array.newInstance(componentType, componentValueList.size());
            for (int i = 0; i < componentValueList.size(); i++) {
                Array.set(arrayObj, i, componentValueList.get(i));
            }
            return arrayObj;
        } else if (Utils.isListObject(clazz) && !clazz.isInterface()) {
            List list = (List) Utils.createInstance(clazz);
            Class componentType = Utils.getListComponentType(field);
            parseComponent2List(list, componentType, startToken);
            return list;
        } else if (clazz.isInterface() && clazz == List.class) {// TODO: 2017/7/31 need to be more complete
            Class componentType = Utils.getListComponentType(field);
            if (componentType == null) {
                throwTokenError();//cannot get the type
            }
            ArrayList list = new ArrayList();
            parseComponent2List(list, componentType, startToken);
            return list;
        }
        throwTokenError();
        return null;
    }


    private void parseComponent2List(List list, Class componentType, Token startToken) {

        if (startToken.type != TokenType.START_ARRAY) {
            throwTokenError();
        }

        Token token = getNextToken();
        if (token.type == TokenType.END_ARRAY) {
            return;
        }

        while (true) {
            Object object = dispatchParse(componentType, null, token);
            list.add(object);
            token = getNextToken();
            if (token.type != TokenType.COMMA) {
                break;
            }
            token = getNextToken();
        }

        if (token.type == TokenType.END_ARRAY) {
            return;
        } else {
            throwTokenError();
        }
        return;
    }

    private Object parseNumber(Class clazz, Token startToken) {
        Token token = startToken;
        if (clazz == byte.class || clazz == Byte.class) {
            return Byte.decode(token.value);

        } else if (clazz == short.class || clazz == Short.class) {
            return Short.decode(token.value);

        } else if (clazz == int.class || clazz == Integer.class) {
            return Integer.decode(token.value);
        } else if (clazz == long.class || clazz == Long.class) {
            return Long.decode(token.value);

        } else if (clazz == float.class || clazz == Float.class) {
            return Float.valueOf(token.value);

        } else if (clazz == double.class || clazz == Double.class) {
            return Double.valueOf(token.value);
        }
        return null;
    }


    private void throwTokenError() {
        throw new IllegalArgumentException("illegal token:");
    }

}
