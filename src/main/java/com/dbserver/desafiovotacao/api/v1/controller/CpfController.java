package com.dbserver.desafiovotacao.api.v1.controller;

import com.dbserver.desafiovotacao.api.v1.model.StatusCpf;
import com.dbserver.desafiovotacao.util.ValidarCpf;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cpfs")
@Tag(name = "CpfController")
public class CpfController {

    public static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";
    public static final String UNABLE_TO_VOTE = "UNABLE_TO_VOTE";

    @GetMapping("/validar")
    public ResponseEntity<StatusCpf> validarCpf(@RequestParam final String cpf) {

        return ResponseEntity.ok(
                ValidarCpf.validar(cpf) ? new StatusCpf(ABLE_TO_VOTE) : new StatusCpf(UNABLE_TO_VOTE)
        );
    }
}
