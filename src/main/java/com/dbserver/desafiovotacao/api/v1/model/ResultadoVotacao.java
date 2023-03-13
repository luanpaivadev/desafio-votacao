package com.dbserver.desafiovotacao.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultadoVotacao {
    private long quantidadeVotosSim;
    private long quantidadeVotosNao;
}
