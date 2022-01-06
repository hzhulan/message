package com.hzhu.wechat.model;

/**
 * @authod: pp_lan on 2021/10/26.
 */
public class Response<T> {

    private int code;

    private T data;

    public Response(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> Response ok(T data) {
        return new Response(200, data);
    }

    public static Response error(String msg) {
        return new Response(500, msg);
    }

    public static Response error(Throwable e) {
        return new Response(500, e.getMessage());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
