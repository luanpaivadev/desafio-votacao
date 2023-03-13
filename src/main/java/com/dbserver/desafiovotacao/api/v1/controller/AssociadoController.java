package com.dbserver.desafiovotacao.api.v1.controller;

import com.dbserver.desafiovotacao.api.v1.assembler.AssociadoAssembler;
import com.dbserver.desafiovotacao.api.v1.assembler.AssociadoDisassembler;
import com.dbserver.desafiovotacao.api.v1.model.dto.AssociadoDto;
import com.dbserver.desafiovotacao.api.v1.model.input.AssociadoInput;
import com.dbserver.desafiovotacao.domain.model.Associado;
import com.dbserver.desafiovotacao.domain.repository.AssociadoRepository;
import com.dbserver.desafiovotacao.domain.service.AssociadoService;
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

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static com.dbserver.desafiovotacao.util.ValidarCpf.validar;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/associados")
@Tag(name = "AssociadoController")
public class AssociadoController {

    public static final String CPF_INVALIDO = "CPF inválido.";
    public static final String ASSOCIADO_JA_CADASTRADO = "Associado com CPF: {0}, já cadastrado.";
    private final AssociadoService associadoService;
    private final AssociadoRepository associadoRepository;
    private final AssociadoAssembler associadoAssembler;
    private final AssociadoDisassembler associadoDisassembler;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AssociadoDto> listarAssociados() {
        List<Associado> associados = associadoRepository.findAll();
        return associadoAssembler.toListDtoObject(associados);
    }

    @GetMapping("/{associadoId}")
    public ResponseEntity<AssociadoDto> buscarAssociadoPeloId(@PathVariable final Long associadoId) {
        Optional<Associado> associadoOptional = associadoRepository.findById(associadoId);
        if (associadoOptional.isPresent()) {
            AssociadoDto associadoDto = associadoAssembler.toDtoObject(associadoOptional.get());
            return ResponseEntity.ok(associadoDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Object> salvarAssociado(@RequestBody @Valid final AssociadoInput associadoInput) {

        final String CPF = associadoInput.getCpf();
        try {
            if (!validar(CPF)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CPF_INVALIDO);
            }
            Associado associado = associadoDisassembler.toDomainObject(associadoInput);
            associadoService.save(associado);
            AssociadoDto associadoDto = associadoAssembler.toDtoObject(associado);
            return ResponseEntity.status(HttpStatus.CREATED).body(associadoDto);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(MessageFormat.format(ASSOCIADO_JA_CADASTRADO, CPF));
        }

    }
}
