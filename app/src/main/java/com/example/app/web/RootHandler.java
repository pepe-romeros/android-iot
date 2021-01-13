package com.example.app.web;

import android.content.res.AssetManager;

import com.example.app.web.base.BaseHttpHandler;
import com.example.app.web.util.HtmlReaderUtil;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

import static com.example.app.web.util.HttpConstants.CONTENT_TYPE;
import static com.example.app.web.util.HttpConstants.HTML_MIME;
import static com.example.app.web.util.HttpConstants.JS_EXTENSION;
import static com.example.app.web.util.HttpConstants.JS_MIME;
import static com.example.app.web.util.HttpConstants.MESSAGE_NOT_FOUND;
import static com.example.app.web.util.HttpConstants.STATUS_NOT_FOUND;
import static com.example.app.web.util.HttpConstants.STATUS_SUCCESS;


/**
 * This HttpHandler serves as the Root handler and serves the main page (i.e. index page).
 */
public class RootHandler extends BaseHttpHandler {

    public static final String ENDPOINT = "/";
    private static final String INDEX_FILE = "/index.html";

    private AssetManager assetManager;

    public RootHandler(AssetManager assetManager) {
        super(ENDPOINT);
        this.assetManager = assetManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final String pathRequested = httpExchange.getRequestURI().getPath();
        final Headers responseHeaders = httpExchange.getResponseHeaders();
        String response;
        OutputStream os = httpExchange.getResponseBody();
        if(pathRequested.equals(ENDPOINT)) {
            responseHeaders.set(CONTENT_TYPE, HTML_MIME);
            response = HtmlReaderUtil.readFile(assetManager.open(WEB_ROOT + INDEX_FILE));
            httpExchange.sendResponseHeaders(STATUS_SUCCESS, 0);
        } else if(pathRequested.contains(JS_EXTENSION)){
            responseHeaders.set(CONTENT_TYPE, JS_MIME);
            response = HtmlReaderUtil.readFile(assetManager.open(WEB_ROOT+ pathRequested));
            httpExchange.sendResponseHeaders(STATUS_SUCCESS, 0);
        } else {
            response = MESSAGE_NOT_FOUND;
            httpExchange.sendResponseHeaders(STATUS_NOT_FOUND, response.length());
        }
        os.write(response.getBytes());
        os.flush();
        os.close();
    }

}
