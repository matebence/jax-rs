package com.bence.mate.mappers;

import com.bence.mate.models.Error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.ext.Provider;
import javax.ws.rs.Produces;

@Provider
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ThrowableMapper implements ExceptionMapper<Throwable> {

    public Response toResponse(Throwable thrw) {
        Error error = Error.builder()
                .errorMessage(thrw.getMessage())
                .documentation("https://github.com/matebence")
                .build();

        if (thrw instanceof WebApplicationException) {
            Response response = ((WebApplicationException) thrw).getResponse();
            return Response.status(response.getStatusInfo())
                    .entity(error)
                    .build();
        }

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        error.setErrCode(status.getStatusCode());
        return Response.status(status)
                .entity(error)
                .build();
    }
}