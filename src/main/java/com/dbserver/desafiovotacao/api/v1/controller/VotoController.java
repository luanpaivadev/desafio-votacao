package com.dbserver.desafiovotacao.api.v1.controller;

import com.dbserver.desafiovotacao.api.v1.assembler.VotoAssembler;
import com.dbserver.desafiovotacao.api.v1.model.dto.VotoDto;
import com.dbserver.desafiovotacao.api.v1.model.input.VotoInput;
import com.dbserver.desafiovotacao.domain.exceptions.AssociadoNaoEncontradoException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.exceptions.VotoInvalidoException;
import com.dbserver.desafiovotacao.domain.exceptions.VotoNaoEncontradoException;
import com.dbserver.desafiovotacao.domain.model.Voto;
import com.dbserver.desafiovotacao.domain.service.VotoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/votos")
@Tag(name = "VotoController")
public class VotoController {

    public static final String VOTO_SIM = "Sim";
    public static final String VOTO_NAO = "Não";
    public static final String ASSOCIADO_NAO_PODE_VOTAR_NA_MESMA_PAUTA = "Associado não pode votar na mesma pauta.";
    public static final String VOTO_INVALIDO = "Voto inválido. Votar somente em [Sim] ou [Não].";
    private final VotoService votoService;
    private final VotoAssembler votoAssembler;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VotoDto> listarVotos() {
        List<Voto> votos = votoService.listarVotos();
        return votoAssembler.toListDtoObject(votos);
    }

    @GetMapping("/{votoId}")
    public ResponseEntity<Object> buscarVotoPeloId(@PathVariable final Long votoId) {
        try {
            Voto voto = votoService.buscarVotoPeloId(votoId);
            VotoDto votoDto = votoAssembler.toDtoObject(voto);
            return ResponseEntity.ok(votoDto);
        } catch (VotoNaoEncontradoException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> salvarVoto(@RequestBody @Valid final VotoInput votoInput) {
        try {
            validarVoto(votoInput);
            Voto voto = votoService.salvarVoto(votoInput);
            VotoDto votoDto = votoAssembler.toDtoObject(voto);
            return ResponseEntity.status(HttpStatus.CREATED).body(votoDto);
        } catch (AssociadoNaoEncontradoException | PautaNaoEncontradaException | PautaException |
                 VotoInvalidoException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ASSOCIADO_NAO_PODE_VOTAR_NA_MESMA_PAUTA);
        }
    }

    private static void validarVoto(VotoInput votoInput) throws VotoInvalidoException {
        if (!Objects.equals(votoInput.getVoto(), VOTO_SIM) && !Objects.equals(votoInput.getVoto(), VOTO_NAO)) {
            throw new VotoInvalidoException(VOTO_INVALIDO);
        }
    }
}
