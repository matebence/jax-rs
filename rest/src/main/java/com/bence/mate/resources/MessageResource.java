package com.bence.mate.resources;

import com.bence.mate.dtos.MessageFilterParam;
import com.bence.mate.services.MessageService;
import com.bence.mate.models.Message;

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.PermitAll;
import javax.annotation.security.DenyAll;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Link;

import javax.ws.rs.core.Context;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.BeanParam;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.bence.mate.models.Error;

import java.util.List;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {

    private MessageService messageService = new MessageService();

    @POST
    @RolesAllowed("admin")
    public Response addMessage(@Context UriInfo uriInfo, Message message) {
        Message newMessage = messageService.addMessage(message);

        return Response.created(Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(newMessage.getId())))
                .build().getUri())
                .entity(newMessage)
                .build();
    }

    @GET
    @DenyAll
    public Response getAllMessages(@Context UriInfo uriInfo, @Context HttpHeaders headers) {
        System.out.println(uriInfo.getPathParameters());
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println(uriInfo.getBaseUri());
        System.out.println(headers.getCookies());

        List<Message> messages = messageService.getAllMessages();

        if (messages.isEmpty()) {
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            Error error = new Error("The database is empty", status.getStatusCode(), "https://github.com/matebence");

//            Response.seeOther(uriInfo.getBaseUriBuilder()
//                    .path(MessageResource.class, "getMessage")
//                    .resolveTemplate("pathId", messages.iterator().next().getId()).build())
//                    .build();

            throw new WebApplicationException(Response.status(status)
                    .entity(error)
                    .build());
        }

        return Response.ok(messages)
                .links(messages.stream().map(e -> Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                        .path(MessageResource.class, "getMessage")
                        .resolveTemplate("pathId", e.getId()))
                        .rel("message")
                        .build()).toArray(Link[]::new))
                .build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("/{pathId: [0-9]*}")
    public Response updateMessage(@Context UriInfo uriInfo, @PathParam("pathId") Long pathId, Message message) {
        message.setId(pathId);
        Message updatedMessage = messageService.updateMessage(message);

        return Response.status(Status.OK)
                .links(Link.fromUriBuilder(uriInfo.getBaseUriBuilder()
                        .path(MessageResource.class, "getMessage")
                        .resolveTemplate("pathId", updatedMessage.getId()))
                        .rel("message")
                        .build())
                .entity(updatedMessage)
                .build();
    }

    @DELETE
    @RolesAllowed("admin")
    @Path("/{pathId: [0-9]*}")
    public Response removeMessage(@HeaderParam("headerId") Long headerId, @PathParam("pathId") Long pathId, @CookieParam("cookieId") Long cookieId) {
        System.out.println(pathId);
        System.out.println(headerId);
        System.out.println(cookieId);

        messageService.removeMessage(pathId);

        return Response.noContent()
                .build();
    }

    @GET
    @PermitAll
    @Path("/{pathId: [0-9]*}")
    public Response getMessage(@PathParam("pathId") Long pathId) {
        System.out.println(pathId);

        return Response.ok(messageService.getMessage(pathId))
                .build();
    }

    @GET
    @PermitAll
    @Path("/filter")
    //@PageLinks(Message.class)
    public Response getAllMessagesForYear(@BeanParam MessageFilterParam messageFilterParam) {
        System.out.println(messageFilterParam.getYear());

        return Response.ok(messageService.getAllMessagesForYear(messageFilterParam.getYear()))
                .build();
    }

    @PermitAll
    public Response subResource(String profileName, Long pathId) {
        System.out.println(profileName);
        System.out.println(pathId);

        return Response.noContent()
                .build();
    }
}
