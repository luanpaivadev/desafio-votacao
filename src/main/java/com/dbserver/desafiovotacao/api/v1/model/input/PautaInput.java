package com.dbserver.desafiovotacao.api.v1.model.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PautaInput {

    @NotBlank
    private String descricao;
}
