package com.company.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/")
public class indexResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getIndexPage() {
        try {
            java.nio.file.Path htmlFilePath = Paths.get("src/main/resources/templates/index.html");
            byte[] htmlBytes = Files.readAllBytes(htmlFilePath);
            String content = new String(htmlBytes);
            return Response.ok(content).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
