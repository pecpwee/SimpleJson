package com.pecpwee.lib.simplejson;

/**
 * Created by pw on 2017/7/31.
 */

public class SimpleJson {

    public String toJson(Object object) {
        StringBuilder sb = new StringBuilder();
        WriteOperator operator = new WriteOperator();
        operator.dispatchParse(sb, object);
        return sb.toString();

    }


    public <T> T fromJson(String str, Class clazz) {
        JsonTokenReader jsonReader = new JsonTokenReader(str);
        ReadOperator operator = new ReadOperator(jsonReader);
        T result = null;
        result = (T) operator.dispatchParse(clazz,null,null);
        return result;
    }
}
