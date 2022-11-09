package com.github.alanaafsc.ifood.mp;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/restaurantes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RestauranteResource {

    @Inject
    PgPool client;

    @GET
    @Path("{idRestaurante}/pratos")
    public Multi<PratoDTO> buscarPratos(@PathParam("idRestaurante") Long idRestaurante){
        return Prato.findAll(client, idRestaurante);
    }

}
