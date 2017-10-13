package com.pecpwee.lib.simplejson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by pw on 2017/7/31.
 */

class WriteOperator {


    public void dispatchParse(StringBuilder sb, Object object) {
        if (object == null) {
            sb.append("null");
            return;
        }
        Class objType = object.getClass();
        if (objType == byte.class || objType == Byte.class) {
            sb.append((byte) object);
        } else if (objType == short.class || objType == Short.class) {
            sb.append((short) (object));
        } else if (objType == int.class || objType == Integer.class) {
            sb.append((int) (object));
        } else if (objType == long.class || objType == Long.class) {
            sb.append((long) (object));
        } else if (objType == float.class || objType == Float.class) {
            sb.append((float) (object));
        } else if (objType == double.class || objType == Double.class) {
            sb.append((double) (object));
        } else if (objType == String.class) {
            writeString((String) object, sb);
        } else if (objType == boolean.class || objType == Boolean.class) {
            sb.append((boolean) object);
        } else if (Utils.isListObject(object.getClass())) {
            parseList(object, sb);
        } else if (objType.isArray()) {
            parseArray(object, sb);
        } else {//it's SIZE object
            parseObj(object, sb);
        }
    }

    private void writeString(String object, StringBuilder sb) {
        sb.append("\"");
        appendEscapeString(object, sb);
        sb.append("\"");
    }

    //method from repo:com.googlecode.json-simple org.json.simple.JSONValue.escape()
    private void appendEscapeString(String s, StringBuilder sb) {
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    //Reference: http://www.unicode.org/versions/Unicode5.1.0/
                    //it's unvisible char
                    if ((ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F') || (ch >= '\u2000' && ch <= '\u20FF')) {
                        appendHexUnicode(ch, sb);
                    } else if ((ch == '&' || ch == '<' || ch == '>' || ch == '\\' || ch == '\'')) {//html escape char.
                        appendHexUnicode(ch, sb);
                    } else {
                        sb.append(ch);
                    }
            }
        }//for
    }

    private void appendHexUnicode(char ch, StringBuilder sb) {
        String ss = Integer.toHexString(ch);
        sb.append("\\u");
        for (int k = 0; k < 4 - ss.length(); k++) {
            sb.append('0');
        }
        sb.append(ss.toLowerCase());

    }

    private void parseList(Object object, StringBuilder sb) {
        List listObj = null;
        listObj = (List) object;
        if (listObj == null) {
            throw new RuntimeException("list not support");
        }
        sb.append("[");
        for (Object item : listObj) {
            dispatchParse(sb, item);
            sb.append(",");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1);
        }
        sb.append("]");

    }

    private void parseArray(Object object, StringBuilder sb) {
        int arrLength = Array.getLength(object);
        sb.append("[");

        for (int i = 0; i < arrLength; i++) {
            Object arrListObj = Array.get(object, i);
            dispatchParse(sb, arrListObj);
            sb.append(",");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1);
        }
        sb.append("]");

    }

    private void parseObj(Object object, StringBuilder sb) {
        sb.append("{");

        Class currentLevelClass = object.getClass();
        while (true) {
            parseObjField(currentLevelClass, object, sb);
            currentLevelClass = currentLevelClass.getSuperclass();
            if (currentLevelClass == null) {
                break;
            }
        }


        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");
    }

    private void parseObjField(Class currentLevelClazz, Object object, StringBuilder sb) {
        Field[] fields = currentLevelClazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (shouldSkipField(object, field)) {
                continue;
            }
            parseFieldName(field, sb);

            sb.append(":");

            parseFieldValue(field, object, sb);
            sb.append(",");
        }
    }

    private boolean shouldSkipField(Object object, Field field) {

        Object fieldObj = null;
        try {
            fieldObj = field.get(object);
        } catch (IllegalAccessException e) {
        }

        int mod = field.getModifiers();
        return fieldObj == null
                || Modifier.isStatic(mod)
                || Modifier.isTransient(mod)
                || Modifier.isFinal(mod);

    }

    private void parseFieldName(Field field, StringBuilder sb) {
        sb.append("\"")
                .append(field.getName())
                .append("\"");
    }

    private void parseFieldValue(Field field, Object object, StringBuilder sb) {
        try {
            dispatchParse(sb, field.get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
