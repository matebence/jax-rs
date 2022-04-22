package com.bence.mate.resources;

import com.bence.mate.exceptions.DatabaseEmptyException;
import com.bence.mate.services.ProfileService;
import com.bence.mate.models.Profile;

import org.glassfish.jersey.linking.ProvideLink;
import org.glassfish.jersey.linking.Binding;

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.PermitAll;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Link;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;

import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("/profiles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfileResource {

    private ProfileService profileService = new ProfileService();

    @POST
    @PermitAll
    @ProvideLink(value = Profile.class, rel = "create")
    public Response addProfile(Profile profile, @Context UriInfo uriInfo) {
        Profile newProfile = profileService.addProfile(profile);

        return Response.created(Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(newProfile.getId())))
                .build().getUri())
                .entity(newProfile)
                .build();
    }

    @GET
    @PermitAll
    @ProvideLink(value = Profile.class, rel = "getAll")
    public Response getAllProfiles(@Context UriInfo uriInfo, @Context HttpHeaders headers) {
        System.out.println(uriInfo.getPathParameters());
        System.out.println(uriInfo.getAbsolutePath());
        System.out.println(uriInfo.getBaseUri());
        System.out.println(headers.getCookies());

        List<Profile> profiles = profileService.getAllProfiles();

        if (profiles.isEmpty()) {
            throw new DatabaseEmptyException();
        }

        return Response.ok(profiles)
                .build();
    }

    @PUT
    @PermitAll
    @Path("/{profileName: [a-z]*}")
    @ProvideLink(value = Profile.class, rel = "update", bindings = {
            @Binding(name = "profileName", value = "${instance.profileName}")
    })
    public Response updateProfile(Profile profile, @PathParam("profileName") String profileName, @Context UriInfo uriInfo) {
        profile.setProfileName(profileName);
        Profile updatedProfile = profileService.updateProfile(profile);

        return Response.status(Status.OK)
                .entity(updatedProfile)
                .build();
    }

    @DELETE
    @RolesAllowed({"user"})
    @Path("/{profileName: [a-z]*}")
    @ProvideLink(value = Profile.class, rel = "remove", bindings = {
            @Binding(name = "profileName", value = "${instance.profileName}")
    })
    public Response removeProfile(@PathParam("profileName") String profileName, @MatrixParam("matrixName") String matrixName) {
        System.out.println(profileName);
        System.out.println(matrixName);

        profileService.removeProfile(profileName);

        return Response.noContent()
                .build();
    }

    @GET
    @RolesAllowed({"user"})
    @Path("/{profileName: [a-z]*}")
    @ProvideLink(value = Profile.class, rel = "get", bindings = {
            @Binding(name = "profileName", value = "${instance.profileName}")
    })
    public Response getProfile(@PathParam("profileName") String profileName, @Context UriInfo uriInfo) {
        System.out.println(profileName);

        Profile profile = profileService.getProfile(profileName);

        return Response.ok(profile)
                .build();
    }

    @GET
    @RolesAllowed({"user"})
    @Path("/filter/{year: [0-9]*}")
    @ProvideLink(value = Profile.class, rel = "filter", bindings = {
            @Binding(name = "year", value = "${instance.year}")
    })
    public Response getAllProfilesPaginated(@PathParam("year") Integer year) {
        System.out.println(year);

        List<Profile> profiles;

        if (year >= 2020) {
            profiles = profileService.getAllProfilesPaginated(0, 10);
        } else {
            profiles = profileService.getAllProfiles();
        }

        return Response.ok(profiles)
                .build();
    }

    @GET
    @RolesAllowed({"user", "admin"})
    @Path("/{profileName: [a-z]*}/messages/{pathId: [0-9]*}")
    @ProvideLink(value = Profile.class, rel = "messages", bindings = {
            @Binding(name = "profileName", value = "${instance.profileName}"),
            @Binding(name = "pathId", value = "${instance.messages.iterator().next().id}"),
    })
    public MessageResource getProfileRelatedMessages(@PathParam("profileName") String profileName, @PathParam("pathId") Long pathId) {
        MessageResource messageResource = new MessageResource();
        messageResource.subResource(profileName, pathId);
        return messageResource;
    }

    @GET
    @RolesAllowed({"user"})
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public List<Profile> getAllLegacyProfiles() {
        return profileService.getAllProfiles();
    }
}
