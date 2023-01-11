package com.existot01.guesswho.common;

public class R<T> {
    private Integer code; // 1: 成功， 2: 失败

    private String msg;

    private T data;

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r. msg = msg;
        r.code = 0;
        return r;
    }
}
