package com.lion.pinepeople.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {

    private String resultCode;
    private T result;



    public static <T> Response<T> error(String resultCode, T result) {
        return new Response(resultCode, result);
    }

    public static <T> Response<T> error(T result) {
        return new Response<>("ERROR", result);
    }
    public static <T> Response<T> success(T result) {
        return new Response("SUCCESS", result);
    }

    public static Response<Void> success() {
        return new Response("SUCCESS", null);
    }

    public static Response<Void> success(String message) {
        return new Response("SUCCESS", message);
    }

    public static Response<String> successToMessage (String message) {
        return new Response("SUCCESS", message);
    }




}