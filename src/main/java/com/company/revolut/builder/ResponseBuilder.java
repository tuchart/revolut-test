package com.company.revolut.builder;

import com.company.revolut.dto.Response;

public class ResponseBuilder {

    public Response buildSuccessResponse(String requestId, Object body, int statusCode) {
        Response response = new Response();
        response.setRequestId(requestId);
        response.setBody(body);
        response.setStatusCode(statusCode);
        response.setErrorMessage(null);
        return response;
    }

    public Response buildErrorResponse(String requestId, int statusCode, String errorMessage) {
        Response response = new Response();
        response.setRequestId(requestId);
        response.setBody(null);
        response.setStatusCode(statusCode);
        response.setErrorMessage(errorMessage);
        return response;
    }
}
