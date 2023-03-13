package com.dbserver.desafiovotacao.domain.service;

import com.dbserver.desafiovotacao.api.v1.model.ResultadoVotacao;
import com.dbserver.desafiovotacao.domain.exceptions.PautaException;
import com.dbserver.desafiovotacao.domain.exceptions.PautaNaoEncontradaException;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import com.dbserver.desafiovotacao.domain.model.Situacao;
import com.dbserver.desafiovotacao.domain.model.Voto;
import com.dbserver.desafiovotacao.domain.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.text.MessageFormat.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class PautaService {

    public static final String NAO_FOI_POSSIVEL_LOCALIZAR_UMA_PAUTA_COM_ID = "Não foi possível localizar uma pauta com id: {0}.";
    public static final String SESSAO_JA_ABERTA_PARA_VOTACAO = "A sessão para a pauta com id: [{0}], já está aberta para votação.";
    public static final String SESSAO_FINALIZADA = "A sessão para a pauta com id: [{0}], já foi finalizada.";
    public static final String DATA_FINALIZACAO_INVALIDA = "Data/Hora de finalização da sessão inválida.";
    private final PautaRepository pautaRepository;

    public List<Pauta> listarPautas(final Situacao situacao) {
        if (Objects.nonNull(situacao)) {
            return pautaRepository.findBySituacao(situacao);
        }
        return pautaRepository.findAll();
    }

    public Pauta buscarPautaPeloId(final Long pautaId) throws PautaNaoEncontradaException {
        return pautaRepository.findById(pautaId)
                .orElseThrow(() -> new PautaNaoEncontradaException(format(NAO_FOI_POSSIVEL_LOCALIZAR_UMA_PAUTA_COM_ID, pautaId)));
    }

    @Transactional
    public Pauta save(final Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    @Transactional
    public void fecharVotacao() {
        List<Pauta> pautas = pautaRepository.findBySituacao(Situacao.VOTACAO_ABERTA);
        pautas.forEach(pauta -> {
            if (pauta.getDataHoraFim().isBefore(LocalDateTime.now())) {
                log.info("Iniciando processo de fechamento de votação para a pauta com id: [{}]", pauta.getId());
                pauta.setSituacao(Situacao.VOTACAO_FECHADA);
                save(pauta);
                log.info("Votação em pauta fechada com sucesso.");
            }
        });
    }

    public Pauta abrirSessao(final Long pautaId, final LocalDateTime dataHoraFim) throws PautaNaoEncontradaException, PautaException {

        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new PautaNaoEncontradaException(format(NAO_FOI_POSSIVEL_LOCALIZAR_UMA_PAUTA_COM_ID, pautaId)));

        validarSituacaoPauta(pautaId, pauta);
        pauta.setSituacao(Situacao.VOTACAO_ABERTA);
        pauta.setDataHoraInicio(LocalDateTime.now());
        validarDataHoraFim(pautaId, dataHoraFim, pauta);
        pauta.setDataHoraFim(Objects.nonNull(dataHoraFim) ? dataHoraFim : pauta.getDataHoraInicio().plusMinutes(1));

        return save(pauta);
    }

    public ResultadoVotacao resultadoVotacao(final Long pautaId) throws PautaNaoEncontradaException {
        Pauta pauta = buscarPautaPeloId(pautaId);
        List<Voto> votos = pauta.getVotos();
        long quantidadeVotosSim = votos.stream().filter(Voto::getVotoSim).count();
        long quantidadeVotosNao = votos.stream().filter(Voto::getVotoNao).count();
        return new ResultadoVotacao(quantidadeVotosSim, quantidadeVotosNao);
    }

    private void validarDataHoraFim(Long pautaId, LocalDateTime dataHoraFim, Pauta pauta) throws PautaException {
        if (Objects.nonNull(dataHoraFim) && dataHoraFim.isBefore(pauta.getDataHoraInicio())) {
            throw new PautaException(format(DATA_FINALIZACAO_INVALIDA, pautaId));
        }
    }

    private void validarSituacaoPauta(Long pautaId, Pauta pauta) throws PautaException {
        if (Objects.equals(pauta.getSituacao(), Situacao.VOTACAO_ABERTA)) {
            throw new PautaException(format(SESSAO_JA_ABERTA_PARA_VOTACAO, pautaId));
        }

        if (Objects.equals(pauta.getSituacao(), Situacao.VOTACAO_FECHADA)
                && Objects.nonNull(pauta.getDataHoraInicio())) {
            throw new PautaException(format(SESSAO_FINALIZADA, pautaId));
        }
    }
}
