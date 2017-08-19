package com.pecpwee.lib.simplejson;

/**
 * Created by pw on 2017/7/31.
 */

public class Token {
    public int type;
    public String value;

    public Token(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    private String getValue() {
        return value;
    }

    public String toString() {
        return getValue();
    }
}

