package com.github.alanaafsc.ifood.cadastro.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.github.alanaafsc.ifood.cadastro.Restaurante;

@Mapper(componentModel = "cdi")
public interface RestauranteMapper {
	
	@Mapping(target = "nome", source = "nomeFantasia")
	public Restaurante toRestaurante(AdicionarRestauranteDTO dto);
}
