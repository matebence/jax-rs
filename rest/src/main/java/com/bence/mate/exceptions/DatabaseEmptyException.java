package com.bence.mate.exceptions;

import com.bence.mate.models.Error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class DatabaseEmptyException extends WebApplicationException {

    public DatabaseEmptyException() {
        super(Response.status(Response.Status.NOT_FOUND)
                .entity(new Error(
                        "https://github.com/matebence",
                        "The database is empty",
                        Response.Status.NOT_FOUND.getStatusCode()))
                .build());
    }
}
