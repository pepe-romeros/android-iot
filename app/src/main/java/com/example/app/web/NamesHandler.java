package com.example.app.web;

import android.content.Context;

import com.example.app.model.Person;
import com.example.app.service.PersonService;
import com.example.app.web.base.BaseHttpHandler;
import com.example.app.web.util.model.HttpRequest;
import com.example.app.web.util.model.HttpResponse;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

import static com.example.app.web.util.HttpConstants.ALLOWED_METHODS;
import static com.example.app.web.util.HttpConstants.CONTENT_TYPE;
import static com.example.app.web.util.HttpConstants.HEADER_ALLOW;
import static com.example.app.web.util.HttpConstants.JSON_MIME;
import static com.example.app.web.util.HttpConstants.MESSAGE_BAD_REQUEST;
import static com.example.app.web.util.HttpConstants.MESSAGE_NOT_FOUND;
import static com.example.app.web.util.HttpConstants.NO_RESPONSE_LENGTH;
import static com.example.app.web.util.HttpConstants.STATUS_BAD_REQUEST;
import static com.example.app.web.util.HttpConstants.STATUS_INTERNAL_SERVER_ERROR;
import static com.example.app.web.util.HttpConstants.STATUS_NOT_FOUND;
import static com.example.app.web.util.HttpConstants.STATUS_SUCCESS;
import static com.example.app.web.util.HttpConstants.STATUS_METHOD_NOT_ALLOWED;


/**
 * NamesHandler handles all GET, POST, PUT and DELETE HTTP request methods for the "/names" endpoint.
 */
public class NamesHandler extends BaseHttpHandler {

    public static final String ENDPOINT = "/names";

    private PersonService personService;

    /**
     * Main constructor for this Handler. It will create a new instance of {@link PersonService} and
     * receive the Application's context to work with the database.
     *
     * @param context the main Application's {@link Context} instance.
     */
    public NamesHandler(Context context){
        super(ENDPOINT);
        personService = new PersonService(context);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        final HttpRequest httpRequest = new HttpRequest(httpExchange);
        switch (httpRequest.getRequestMethod()) {
            case GET: // responds to GET http://ipaddress:5000/names OR GET http://ipaddress:5000/names/{id}
                handleGetRequest(httpExchange, httpRequest);
                break;
            case POST: // responds to POST request to http://ipaddress:5000/names
                handlePostRequest(httpExchange, httpRequest);
                break;
            case PUT: // responds to PUT request to http://ipaddress:5000/names/{id}
                handlePutRequest(httpExchange, httpRequest);
                break;
            case DELETE:
                // TODO - Handle DELETE request from JS JQuery function call
                handleDeleteRequest(httpExchange, httpRequest);
                break;
            default:
                httpRequest.getHeaders().set(HEADER_ALLOW, ALLOWED_METHODS);
                httpExchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                break;
        }
    }

    /**
     * Helper method that decides if this is a "GET ALL" or "GET BY ID" request.
     *
     * @param exchange the {@link HttpExchange} containing the Response instance.
     * @param request the {@link HttpRequest} instance mapping the HTTP Request received.
     * @throws IOException thrown if an I/O error is encountered while writing to the stream.
     */
    private void handleGetRequest(HttpExchange exchange, HttpRequest request) throws IOException {
        if (request.getRequestPath().equals(ENDPOINT)) {
            doGetAllNamesResponse(exchange, request);
        } else {
           doGetNameWithIdResponse(exchange, request, getFirstPathParameter(request.getRequestPath()));
        }
    }

    /**
     * Handles the GET request made to the "/names" endpoint and retrieves all Persons in the system.
     *
     * @param exchange the {@link HttpExchange} containing the Response instance.
     * @param request the {@link HttpRequest} instance mapping the HTTP Request received.
     * @throws IOException thrown if an I/O error is encountered while writing to the stream.
     */
    private void doGetAllNamesResponse(HttpExchange exchange, HttpRequest request) throws IOException {
        String response; // will wold the json response to be sent back to client
        request.getHeaders().set(CONTENT_TYPE, JSON_MIME); // set response type as JSON
        response = parseToJson(personService.findAllPeople()); // JSON Array with all Persons available
        exchange.sendResponseHeaders(STATUS_SUCCESS, response.length()); // Response SUCCESS 200

        // Actually write the json response to the response body.
        request.getResponseBody().write(response.getBytes());
        // Close the output stream
        request.getResponseBody().close();
    }

    /**
     * Handles the GET request made to the "/names/{id}" endpoint. Expects the {id} path parameter
     * to be of type Integer.
     *
     * If the ID is found will retrieve the Person associated to it.
     *
     * @param exchange the {@link HttpExchange} containing the Response instance.
     * @param request the {@link HttpRequest} instance mapping the HTTP Request received.
     * @throws IOException thrown if an I/O error is encountered while writing to the stream.
     */
    private void doGetNameWithIdResponse(HttpExchange exchange, HttpRequest request, String id) throws IOException {
        String response; // will wold the json response to be sent back to client
        request.getHeaders().set(CONTENT_TYPE, JSON_MIME); // set response type as JSON
        try {
            // Find the requested person by its id
            Person person = personService.findPersonById(Integer.parseInt(id));

            if (person != null) {
                // person was found let's send the result back
                response = parseToJson(person);
                exchange.sendResponseHeaders(STATUS_SUCCESS, response.length());
            } else {
                // person was not found let's respond with 404 Not Found
                HttpResponse httpResponse = new HttpResponse(STATUS_NOT_FOUND, MESSAGE_NOT_FOUND);
                response = parseToJson(httpResponse);
                exchange.sendResponseHeaders(STATUS_NOT_FOUND, response.length());
            }
            // Actually write the json response to the response body.
            request.getResponseBody().write(response.getBytes());

            // Close the output stream
            request.getResponseBody().close();
        } catch (NumberFormatException e) {
            // Client requested an unhandled GET PATH parameter respond with BAD REQUEST
            HttpResponse httpResponse = new HttpResponse(STATUS_BAD_REQUEST, MESSAGE_BAD_REQUEST);
            response = parseToJson(httpResponse);
            exchange.sendResponseHeaders(STATUS_BAD_REQUEST, response.length());
            // Actually write the json response to the response body.
            request.getResponseBody().write(response.getBytes());
            // Close the output stream
            request.getResponseBody().close();
            // Log error to console
            e.printStackTrace();
        }
    }

    /**
     * Handles the POST request made to the "/names" endpoint to create a new Person record.
     *
     * @param exchange the {@link HttpExchange} containing the Response instance.
     * @param request the {@link HttpRequest} instance mapping the HTTP Request received.
     * @throws IOException thrown if an I/O error is encountered while writing to the stream.
     */
    private void handlePostRequest(HttpExchange exchange, HttpRequest request) throws IOException {
        request.getHeaders().set(CONTENT_TYPE, JSON_MIME); // set response type as JSON

        // Retrieve POST's body parameters.
        Map<String, String> parameters = getBodyParameters(request.getRequestBody());

        // Save new Person to the database.
        long id = personService.addNewPerson(parameters.get(Person.SERIALIZED_FIRST_NAME),
                parameters.get(Person.SERIALIZED_LAST_NAME));

        HttpResponse httpResponse; // Holds the result Response to sent back to client.
        String message; // Holds the result message of the operation.
        String response; // Holds the raw response to be sent back.

        if(id > 0) { // We got a positive ID, Person was created successfully
            message = "New person added successfully";
            httpResponse = new HttpResponse(STATUS_SUCCESS, message);
            response = parseToJson(httpResponse);
            exchange.sendResponseHeaders(STATUS_SUCCESS, response.length());
        } else { // We got a negative ID, an error happened and Person record was not created.
            message = "An error occurred trying to add new Person.";
            httpResponse = new HttpResponse(STATUS_INTERNAL_SERVER_ERROR, message);
            response = parseToJson(httpResponse);
            exchange.sendResponseHeaders(STATUS_INTERNAL_SERVER_ERROR, response.length());
        }

        // Actually write the json response to the response body.
        request.getResponseBody().write(response.getBytes());
        // Close the output stream
        request.getResponseBody().close();
    }

    /**
     * Handles the PUT request made to the "/names/{id}" endpoint. Expects the {id} path parameter.
     *
     * If the ID is found, the Person associated to it will be updated with the provided values.
     *
     * @param exchange the {@link HttpExchange} containing the Response instance.
     * @param request the {@link HttpRequest} instance mapping the HTTP Request received.
     * @throws IOException thrown if an I/O error is encountered while writing to the stream.
     */
    private void handlePutRequest(HttpExchange exchange, HttpRequest request) throws IOException {
        String response; // will wold the json response to be sent back to client
        HttpResponse httpResponse; // Holds the result Response to sent back to client.
        String message; // Holds the result message of the operation.
        request.getHeaders().set(CONTENT_TYPE, JSON_MIME); // set response type as JSON

        Map<String, String> parameters = getBodyParameters(request.getRequestBody());
        String firstName = parameters.get(Person.SERIALIZED_FIRST_NAME);
        String lastName = parameters.get(Person.SERIALIZED_LAST_NAME);

        try {
            int id = Integer.parseInt(getFirstPathParameter(request.getRequestPath()));
            if (personService.findPersonById(id) != null) {
                if(personService.updatePerson(id, firstName, lastName)) {
                    message = "Person updated successfully.";
                    httpResponse = new HttpResponse(STATUS_SUCCESS, message);
                    response = parseToJson(httpResponse);
                    exchange.sendResponseHeaders(STATUS_SUCCESS, response.length());
                } else {
                    message = "An error occurred trying to update Person.";
                    httpResponse = new HttpResponse(STATUS_INTERNAL_SERVER_ERROR, message);
                    response = parseToJson(httpResponse);
                    exchange.sendResponseHeaders(STATUS_INTERNAL_SERVER_ERROR, response.length());
                }
            } else {
                // person was not found let's respond with 404 Not Found
                httpResponse = new HttpResponse(STATUS_NOT_FOUND, MESSAGE_NOT_FOUND);
                response = parseToJson(httpResponse);
                exchange.sendResponseHeaders(STATUS_NOT_FOUND, response.length());
            }
            // Actually write the json response to the response body.
            request.getResponseBody().write(response.getBytes());

            // Close the output stream
            request.getResponseBody().close();
        } catch (NumberFormatException e) {
            // Client requested an unhandled GET PATH parameter respond with BAD REQUEST
            httpResponse = new HttpResponse(STATUS_BAD_REQUEST, MESSAGE_BAD_REQUEST);
            response = parseToJson(httpResponse); // Prepare Response message
            exchange.sendResponseHeaders(STATUS_BAD_REQUEST, response.length()); // Send Headers

            // Actually write the json response to the response body.
            request.getResponseBody().write(response.getBytes());
            // Close the output stream
            request.getResponseBody().close();
            // Log error to console
            e.printStackTrace();
        }
    }

    /**
     * Handles the PUT request made to the "/names/{id}" endpoint. Expects the {id} path parameter.
     *
     * @param exchange the {@link HttpExchange} containing the Response instance.
     * @param request the {@link HttpRequest} instance mapping the HTTP Request received.
     * @throws IOException thrown if an I/O error is encountered while writing to the stream.
     */
    private void handleDeleteRequest(HttpExchange exchange, HttpRequest request) throws IOException {
        String response; // will wold the json response to be sent back to client
        HttpResponse httpResponse; // Holds the result Response to sent back to client.
        String message; // Holds the result message of the operation.
        request.getHeaders().set(CONTENT_TYPE, JSON_MIME); // set response type as JSON
        try {
            int id = Integer.parseInt(getFirstPathParameter(request.getRequestPath()));
            if (personService.findPersonById(id) != null) {
                if(personService.deletePerson(id)) {
                    message = "Person deleted successfully.";
                    httpResponse = new HttpResponse(STATUS_SUCCESS, message);
                    response = parseToJson(httpResponse);
                    exchange.sendResponseHeaders(STATUS_SUCCESS, response.length());
                } else {
                    message = "An error occurred trying to update Person.";
                    httpResponse = new HttpResponse(STATUS_INTERNAL_SERVER_ERROR, message);
                    response = parseToJson(httpResponse);
                    exchange.sendResponseHeaders(STATUS_INTERNAL_SERVER_ERROR, response.length());
                }
            } else {
                // person was not found let's respond with 404 Not Found
                httpResponse = new HttpResponse(STATUS_NOT_FOUND, MESSAGE_NOT_FOUND);
                response = parseToJson(httpResponse);
                exchange.sendResponseHeaders(STATUS_NOT_FOUND, response.length());
            }
            // Actually write the json response to the response body.
            request.getResponseBody().write(response.getBytes());

            // Close the output stream
            request.getResponseBody().close();
        } catch (NumberFormatException e) {
            // Client requested an unhandled GET PATH parameter respond with BAD REQUEST
            httpResponse = new HttpResponse(STATUS_BAD_REQUEST, MESSAGE_BAD_REQUEST);
            response = parseToJson(httpResponse); // Prepare Response message
            exchange.sendResponseHeaders(STATUS_BAD_REQUEST, response.length()); // Send Headers
            // Actually write the json response to the response body.
            request.getResponseBody().write(response.getBytes());
            // Close the output stream
            request.getResponseBody().close();
            // Log error to console
            e.printStackTrace();
        }
    }

    @Override
    public void tearDown() {
        super.tearDown();
        if (personService != null) {
            personService.cleanUp();
        }
        personService = null;
    }

}
