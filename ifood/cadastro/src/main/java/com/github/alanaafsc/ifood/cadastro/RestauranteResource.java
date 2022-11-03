package com.github.alanaafsc.ifood.cadastro;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.github.alanaafsc.ifood.cadastro.dto.AdicionarRestauranteDTO;
import com.github.alanaafsc.ifood.cadastro.dto.AtualizarRestauranteDTO;
import com.github.alanaafsc.ifood.cadastro.dto.RestauranteDTO;
import com.github.alanaafsc.ifood.cadastro.dto.RestauranteMapper;

@Path("/restaurantes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Restaurante")
public class RestauranteResource {

	@Inject
	RestauranteMapper restauranteMapper;


	@GET
	public List<RestauranteDTO> listarRestaurantes() {
		Stream<Restaurante> restaurantes = Restaurante.streamAll();
		return restaurantes.map(r -> restauranteMapper.toRestauranteDTO(r)).collect(Collectors.toList());
	}

	@POST
	@Transactional
	public Response criarRestaurante(@Valid AdicionarRestauranteDTO dto) {
		Restaurante restaurante = restauranteMapper.toRestaurante(dto);
		restaurante.persist();
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public void atualizarRestaurante(@PathParam("id") Long id, AtualizarRestauranteDTO dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException();
		}
		Restaurante restaurante = restauranteOp.get();

		//MapStruct: aqui passo a referencia para ser atualizada 
        restauranteMapper.toRestauranteAtt(dto, restaurante);

		restaurante.persist();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public void deletarRestaurante(@PathParam("id") Long id) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
		restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {
			throw new NotFoundException();
		});

	}
}
