package com.bence.mate.filters;

import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import javax.ws.rs.core.MultivaluedMap;
import javax.annotation.Priority;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(2)
public class LogFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext reqContext) {
        System.out.println("-- req headers --");
        log(reqContext.getUriInfo(), reqContext.getHeaders());
    }

    @Override
    public void filter(ContainerRequestContext reqContext, ContainerResponseContext resContext) {
        System.out.println("-- res headers --");
        log(reqContext.getUriInfo(), resContext.getHeaders());
    }

    private void log(UriInfo uriInfo, MultivaluedMap<String, ?> headers) {
        System.out.println("Path: " + uriInfo.getPath());
        headers.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}