package org.alliancegenome.api.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.alliancegenome.es.index.data.APIResponce;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import io.swagger.annotations.Api;

@Path("/data")
@Api(value = "Meta Data Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MetaDataRESTInterface {
    
    @GET
    @Path("/snapshot")
    public APIResponce getSnapShot(
            @QueryParam(value = "system")
            String system,
            @QueryParam(value = "releaseVersion")
            String releaseVersion);
    
    @GET
    @Path("/takesnapshot")
    public APIResponce takeSnapShot(
            @HeaderParam("api_access_token")
            String api_access_token, 
            @QueryParam("system")
            String system,
            @QueryParam(value = "releaseVersion")
            String releaseVersion);

    @GET
    @Path("/releases")
    public APIResponce getReleases(@QueryParam(value = "system") String system);
    
    @POST
    @Path("/submit")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public APIResponce submitData(@HeaderParam("api_access_token") String api_access_token, MultipartFormDataInput input);

    @POST
    @Path("/validate")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public APIResponce validateData(MultipartFormDataInput input);

}