package com.example.app.web.util.model;

import com.google.gson.annotations.SerializedName;

public class HttpResponse {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public HttpResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}

