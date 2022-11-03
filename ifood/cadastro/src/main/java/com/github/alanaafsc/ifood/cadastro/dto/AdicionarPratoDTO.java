package com.github.alanaafsc.ifood.cadastro.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;

public class AdicionarPratoDTO {
	
	@NotBlank
	public String nome;

	public String descricao;

	public BigDecimal preco;
}
