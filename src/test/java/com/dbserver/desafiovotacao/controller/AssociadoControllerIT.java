package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.api.v1.model.input.AssociadoInput;
import com.dbserver.desafiovotacao.util.DatabaseCleaner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static com.dbserver.desafiovotacao.api.v1.controller.AssociadoController.CPF_INVALIDO;
import static io.restassured.RestAssured.given;

@TestPropertySource(properties = {"spring.config.location=classpath:application-test.properties"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssociadoControllerIT {

    @LocalServerPort
    private int port;
    public static final String BASE_PATH = "/v1/associados";
    private AssociadoInput associadoInput;
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = port;
        RestAssured.basePath = BASE_PATH;

        // Obs.: Dados gerados aleatoriamente no site https://www.4devs.com.br/gerador_de_pessoas
        this.associadoInput = new AssociadoInput();
        this.associadoInput.setNome("Fábio Otávio Duarte");
        this.associadoInput.setCpf("765.387.893-82");

        databaseCleaner.clearTables();
    }

    @Test
    void deveRetornarStatus201Created_QuandoForSalvoUmAssociadoComSucesso() {

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(associadoInput)
                .when().post()
                .then().statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deveRetornarStatus400BadRequest_QuandoForSalvoUmAssociadoComCpfInvalido() {

        associadoInput.setCpf("765.387.893-83");

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(associadoInput)
                .when().post()
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .and().body(Matchers.is(CPF_INVALIDO));
    }
}
