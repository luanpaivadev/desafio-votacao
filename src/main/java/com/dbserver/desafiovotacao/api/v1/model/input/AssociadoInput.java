package com.dbserver.desafiovotacao.api.v1.model.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssociadoInput {

    @NotBlank
    private String nome;
    @NotBlank
    private String cpf;
}
