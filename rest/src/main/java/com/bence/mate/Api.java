package com.bence.mate;

import org.glassfish.jersey.server.ResourceConfig;
import com.bence.mate.models.Discover;
import com.bence.mate.models.Health;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.GET;

@Path("")
@ApplicationPath("/api")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class Api extends ResourceConfig {

    private Boolean ready = Boolean.FALSE;

    public Api() {
        ready = Boolean.TRUE;
    }

    @GET
    @Path("/health")
    public Response getHealth() {
        if (!ready) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(new Health(Health.DOWN))
                    .build();
        }
        return Response.ok()
                .entity(new Health(Health.UP))
                .build();
    }

    @GET
    @Path("/discover")
    public Response getEndpoints() {
        return Response.ok()
                .entity(new Discover())
                .build();
    }
}
