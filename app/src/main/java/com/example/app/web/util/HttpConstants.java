package com.example.app.web.util;

/**
 * Constants file containing HTTP protocol related keywords and values
 */
public final class HttpConstants {

    private HttpConstants() {
        // Private empty constructor to avoid accidental instantiation
    }

    // Headers Constants ---------------------------------------------------------------------------
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ALLOW = "Allow";
    public static final int NO_RESPONSE_LENGTH = -1;

    // MIME Constants ------------------------------------------------------------------------------
    public static final String HTML_MIME = "text/html";
    public static final String JSON_MIME = "application/json";
    public static final String JS_MIME = "application/javascript";

    // Extensions Constants ------------------------------------------------------------------------
    public static final String JS_EXTENSION = ".js";

    // Method Constants ----------------------------------------------------------------------------
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "POST";
    public static final String METHOD_DELETE = "POST";
    public static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_POST + "," + METHOD_PUT + "," + METHOD_DELETE;

    // Response Codes Constants --------------------------------------------------------------------
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_METHOD_NOT_ALLOWED = 405;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;

    // Response Messages Constants -----------------------------------------------------------------
    public static final String MESSAGE_BAD_REQUEST = "API cannot process this request.";
    public static final String MESSAGE_NOT_FOUND = "Resource not found.";

}
