package com.bence.mate.mappers;

import com.bence.mate.exceptions.DataNotFoundException;
import com.bence.mate.models.Error;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.Produces;

@Provider
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

    public Response toResponse(DataNotFoundException e) {
        Response.Status status = Response.Status.NOT_FOUND;
        Error error = new Error(e.getMessage(), status.getStatusCode(), "https://github.com/matebence");

        return Response.status(status)
                .entity(error)
                .build();
    }
}
