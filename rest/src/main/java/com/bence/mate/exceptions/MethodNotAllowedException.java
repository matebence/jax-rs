package com.bence.mate.exceptions;

import com.bence.mate.models.Error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public class MethodNotAllowedException extends WebApplicationException {

    public MethodNotAllowedException(String httpMethod) {
        super(Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.ALLOW, httpMethod)
                .entity(new Error(
                        "Method not allowed",
                        "https://github.com/matebence",
                        Response.Status.METHOD_NOT_ALLOWED.getStatusCode()))
                .build());
    }
}
