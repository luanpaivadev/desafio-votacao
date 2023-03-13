package com.dbserver.desafiovotacao.domain.service;

import com.dbserver.desafiovotacao.domain.exceptions.AssociadoNaoEncontradoException;
import com.dbserver.desafiovotacao.domain.model.Associado;
import com.dbserver.desafiovotacao.domain.repository.AssociadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.text.MessageFormat.format;

@Service
@RequiredArgsConstructor
public class AssociadoService {

    public static final String ASSOCIADO_NAO_ENCONTRADO = "Não foi possível localizar um associado com id: {0}";
    private final AssociadoRepository associadoRepository;

    @Transactional
    public Associado save(final Associado associado) {
        associado.setCpf(associado.getCpf().replaceAll("\\D", ""));
        return associadoRepository.save(associado);
    }

    public Associado buscarAssociadoPeloId(final Long associadoId) throws AssociadoNaoEncontradoException {
        return associadoRepository.findById(associadoId)
                .orElseThrow(() -> new AssociadoNaoEncontradoException(format(ASSOCIADO_NAO_ENCONTRADO, associadoId)));
    }

}
