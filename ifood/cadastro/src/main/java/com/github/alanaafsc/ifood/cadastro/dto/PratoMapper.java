package com.github.alanaafsc.ifood.cadastro.dto;

import javax.enterprise.context.ApplicationScoped;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.github.alanaafsc.ifood.cadastro.Prato;

@ApplicationScoped
@Mapper(componentModel = "cdi")
public interface PratoMapper {
	
	PratoDTO toDTO(Prato p);

    Prato toPrato(AdicionarPratoDTO dto);

    void toPratoAtt(AtualizarPratoDTO dto, @MappingTarget Prato prato);
}
