package com.example.app.web.base;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class BaseHttpHandler implements HttpHandler  {

    protected static final String WEB_ROOT = "www";

    private Gson gson; // Assist with parsing POJOs to JSON strings
    private String handlersEndpointPath; // The endpoint associated to the Handler's child instance.

    public BaseHttpHandler() {
        gson = new Gson();
    }

    /**
     * Overloaded constructor that receives the endpoint associated to the HttpHandler.
     *
     * @param endpointPath {@link String} value for the endpoint
     */
    public BaseHttpHandler(String endpointPath) {
        this();
        handlersEndpointPath = endpointPath;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        //  Default skeleton for HttpHandler interface contract
    }

    /**
     * Generic helper method to parse objects into JSON Strings
     *
     * @param input the object to be parsed
     * @param <T> the Type of the object to be parsed
     * @return {@link String} representing the JSON
     */
    protected <T> String parseToJson(T input) {
        return gson.toJson(input);
    }

    /**
     * Helps return the first Path parameter passed on a given GET/PUT request.
     *
     * @param path the URI representing the path requested.
     * @return {@link String} representing the parameter in the first place.
     */
    protected String getFirstPathParameter(String path) {
        return path.substring(handlersEndpointPath.length() + 1);
    }

    /**
     * Helps to extract all parameters from a RequestBody instance.
     *
     * @param requestBody the {@link InputStream} representing the Request's body.
     * @return {@link Map<String, String>} parameter's key and value String map.
     */
    protected Map<String, String> getBodyParameters(InputStream requestBody) {
        Map<String, String> parameters = new LinkedHashMap<>();

        Scanner scanner = new Scanner(requestBody).useDelimiter("\\A");
        String contents = scanner.next();
        String[] inputs = contents.split("&");

        for(String input:inputs) {
            String[] keyValue = input.split("=");
            parameters.put(keyValue[0], keyValue[1]);
        }

        return parameters;
    }

    /**
     * Provides the skeleton to handle clean up tasks and memory release calls.
     */
    public void tearDown() {
        gson = null;
    }

}
