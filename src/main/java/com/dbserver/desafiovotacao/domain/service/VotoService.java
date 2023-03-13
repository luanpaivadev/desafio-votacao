package com.dbserver.desafiovotacao.domain.service;

import com.dbserver.desafiovotacao.api.v1.model.input.VotoInput;
import com.dbserver.desafiovotacao.domain.exceptions.AssociadoNaoEncontradoException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.exceptions.VotoNaoEncontradoException;
import com.dbserver.desafiovotacao.domain.model.Associado;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import com.dbserver.desafiovotacao.domain.model.Situacao;
import com.dbserver.desafiovotacao.domain.model.Voto;
import com.dbserver.desafiovotacao.domain.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static java.text.MessageFormat.format;

@Service
@RequiredArgsConstructor
public class VotoService {

    public static final String NAO_FOI_POSSIVEL_LOCALIZAR_UM_VOTO_COM_ID = "Não foi possível localizar um voto com id: {0}.";
    public static final String SESSAO_NAO_ABERTA = "A sessão para esta pauta ainda não foi aberta.";
    public static final String SESSAO_FINALIZADA = "A sessão para esta pauta já foi finalizada.";
    private final VotoRepository votoRepository;
    private final AssociadoService associadoService;
    private final PautaService pautaService;

    public List<Voto> listarVotos() {
        return votoRepository.findAll();
    }

    public Voto buscarVotoPeloId(final Long votoId) throws VotoNaoEncontradoException {
        return votoRepository.findById(votoId)
                .orElseThrow(() -> new VotoNaoEncontradoException(format(NAO_FOI_POSSIVEL_LOCALIZAR_UM_VOTO_COM_ID, votoId)));
    }

    @Transactional
    public Voto salvarVoto(final VotoInput votoInput)
            throws AssociadoNaoEncontradoException, PautaNaoEncontradaException, PautaException {

        Pauta pauta = pautaService.buscarPautaPeloId(votoInput.getPautaId());
        validarSituacaoPauta(pauta);
        Associado associado = associadoService.buscarAssociadoPeloId(votoInput.getAssociadoId());

        return votoRepository.save(new Voto(associado, votoInput.getVoto(), pauta));
    }

    private static void validarSituacaoPauta(Pauta pauta) throws PautaException {
        if (Objects.equals(pauta.getSituacao(), Situacao.VOTACAO_FECHADA) &&
                Objects.isNull(pauta.getDataHoraInicio())) {
            throw new PautaException(SESSAO_NAO_ABERTA);
        }

        if (Objects.equals(pauta.getSituacao(), Situacao.VOTACAO_FECHADA) &&
                Objects.nonNull(pauta.getDataHoraInicio())) {
            throw new PautaException(SESSAO_FINALIZADA);
        }
    }
}
