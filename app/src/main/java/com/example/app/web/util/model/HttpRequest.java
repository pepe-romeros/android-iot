package com.example.app.web.util.model;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.io.OutputStream;

public class HttpRequest {

    private Headers headers;
    private HttpMethod requestMethod;
    private String requestPath;
    private final OutputStream responseBody;
    private InputStream requestBody;

    public HttpRequest(HttpExchange httpExchange) {
        this.headers = httpExchange.getResponseHeaders();
        this.requestMethod = HttpMethod.valueOf(httpExchange.getRequestMethod().toUpperCase());
        this.requestPath = httpExchange.getRequestURI().getPath();
        this.responseBody = httpExchange.getResponseBody();
        this.requestBody = httpExchange.getRequestBody();
    }

    public Headers getHeaders() {
        return headers;
    }

    public HttpMethod getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public OutputStream getResponseBody() {
        return responseBody;
    }

    public InputStream getRequestBody() {
        return requestBody;
    }
}
