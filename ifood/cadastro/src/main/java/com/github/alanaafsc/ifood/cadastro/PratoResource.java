package com.github.alanaafsc.ifood.cadastro;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.github.alanaafsc.ifood.cadastro.dto.AdicionarPratoDTO;
import com.github.alanaafsc.ifood.cadastro.dto.AtualizarPratoDTO;
import com.github.alanaafsc.ifood.cadastro.dto.PratoDTO;
import com.github.alanaafsc.ifood.cadastro.dto.PratoMapper;
import com.github.alanaafsc.ifood.cadastro.dto.RestauranteMapper;


@Path("/restaurantes/{idRestaurante}/pratos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "prato")
@RolesAllowed("proprietario")
@SecurityScheme(securitySchemeName = "ifood-oauth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "http://localhost:8180/auth/realms/ifood/protocol/openid-connect/token")))
@SecurityRequirement(name = "ifood-oauth", scopes = {})
public class PratoResource {
	
	@Inject
	PratoMapper pratoMapper;
	
	@Inject
	RestauranteMapper restauranteMapper;

	@GET
	@Path("{idRestaurante}/pratos")
	public List<PratoDTO> listarPratos(@PathParam("idRestaurante") Long idRestaurante) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante n??o existe");
		}
		 Stream<Prato> pratos = Prato.stream("restaurante", restauranteOp.get());
         return pratos.map(p -> pratoMapper.toDTO(p)).collect(Collectors.toList());
	}

	@POST
	@Path("{idRestaurante}/pratos")
	@Transactional
	public Response criarPrato(@PathParam("idRestaurante") Long idRestaurante, AdicionarPratoDTO dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante n??o existe");
		}
//		Prato prato = new Prato();
//		prato.nome = dto.nome;
//		prato.descricao = dto.descricao;
//		prato.persist();
		
		Prato prato = pratoMapper.toPrato(dto);
        prato.restaurante = restauranteOp.get();
        prato.persist();
        
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	public void atualizarPrato(@PathParam("idRestaurante") Long idRestaurante, 
			@PathParam("id") Long id, AtualizarPratoDTO dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante n??o existe");
		}
		
		//id do prato ser?? ??nico, independente do restaurante 	
		Optional<Prato> pratoOp = Prato.findByIdOptional(id);
		if (pratoOp.isEmpty()) {
			throw new NotFoundException("Prato n??o existe");
		}
		
		Prato prato = pratoOp.get();
		pratoMapper.toPratoAtt(dto, prato);
		prato.persist();
	}

	@DELETE
	@Path("{idRestaurante}/pratos/{id}")
	@Transactional
	public void deletarPrato(@PathParam("idRestaurante") Long idRestaurante, 
			@PathParam("id") Long id) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
	
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante n??o existe");
		}
		
		Optional<Prato> pratoOp = Prato.findByIdOptional(id);
		pratoOp.ifPresentOrElse(Prato::delete, () -> {
			throw new NotFoundException("Prato n??o existe");
		});

	}

}
