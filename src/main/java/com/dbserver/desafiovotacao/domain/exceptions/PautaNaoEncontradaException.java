package com.dbserver.desafiovotacao.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PautaNaoEncontradaException extends Exception {

    public PautaNaoEncontradaException(String message) {
        super(message);
    }
}
