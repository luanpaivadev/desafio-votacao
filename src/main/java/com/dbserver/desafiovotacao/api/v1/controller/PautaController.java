package com.dbserver.desafiovotacao.api.v1.controller;

import com.dbserver.desafiovotacao.api.v1.assembler.PautaAssembler;
import com.dbserver.desafiovotacao.api.v1.assembler.PautaDisassembler;
import com.dbserver.desafiovotacao.api.v1.model.ResultadoVotacao;
import com.dbserver.desafiovotacao.api.v1.model.dto.PautaDto;
import com.dbserver.desafiovotacao.api.v1.model.input.PautaInput;
import com.dbserver.desafiovotacao.domain.exceptions.PautaException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import com.dbserver.desafiovotacao.domain.model.Situacao;
import com.dbserver.desafiovotacao.domain.model.Voto;
import com.dbserver.desafiovotacao.domain.service.PautaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/pautas")
@Tag(name = "PautaController")
public class PautaController {

    private final PautaService pautaService;
    private final PautaAssembler pautaAssembler;
    private final PautaDisassembler pautaDisassembler;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PautaDto> listarPautas(@RequestParam(required = false) final Situacao situacao) {
        List<Pauta> pautas = pautaService.listarPautas(situacao);
        return pautaAssembler.toListDtoObject(pautas);
    }

    @GetMapping("/{pautaId}")
    public ResponseEntity<Object> buscarPautaPeloId(@PathVariable final Long pautaId) {
        try {
            Pauta pauta = pautaService.buscarPautaPeloId(pautaId);
            PautaDto pautaDto = pautaAssembler.toDtoObject(pauta);
            return ResponseEntity.ok(pautaDto);
        } catch (PautaNaoEncontradaException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{pautaId}/resultado-votacao")
    public ResponseEntity<Object> resultadoVotacao(@PathVariable final Long pautaId) {
        try {
            Pauta pauta = pautaService.buscarPautaPeloId(pautaId);
            List<Voto> votos = pauta.getVotos();
            long quantidadeVotosSim = votos.stream().filter(Voto::getVotoSim).count();
            long quantidadeVotosNao = votos.stream().filter(Voto::getVotoNao).count();
            return ResponseEntity.ok(new ResultadoVotacao(quantidadeVotosSim, quantidadeVotosNao));
        } catch (PautaNaoEncontradaException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<PautaDto> salvarPauta(@RequestBody @Valid final PautaInput pautaInput) {
        Pauta pauta = pautaDisassembler.toDomainObject(pautaInput);
        pautaService.save(pauta);
        PautaDto pautaDto = pautaAssembler.toDtoObject(pauta);
        return ResponseEntity.status(HttpStatus.CREATED).body(pautaDto);
    }

    @PutMapping("/{pautaId}/abrir-sessao")
    public ResponseEntity<Object> abrirSessao(@PathVariable final Long pautaId,
                                              @RequestParam(required = false) final LocalDateTime dataHoraFim) {
        try {
            Pauta pauta = pautaService.abrirSessao(pautaId, dataHoraFim);
            PautaDto pautaDto = pautaAssembler.toDtoObject(pauta);
            return ResponseEntity.ok(pautaDto);
        } catch (PautaNaoEncontradaException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PautaException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
