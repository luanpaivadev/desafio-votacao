package com.dbserver.desafiovotacao.api.v1.model.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotoInput {

    @NotNull
    private Long associadoId;
    @NotBlank
    private String voto;
    @NotNull
    private Long pautaId;

}
