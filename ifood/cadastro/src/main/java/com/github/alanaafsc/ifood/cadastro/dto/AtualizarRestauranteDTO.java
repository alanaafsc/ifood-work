package com.github.alanaafsc.ifood.cadastro.dto;

import javax.validation.constraints.Size;

public class AtualizarRestauranteDTO {
	
	@Size(min = 3, max = 30)
    public String nomeFantasia;
}
