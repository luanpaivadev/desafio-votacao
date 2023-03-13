package com.dbserver.desafiovotacao.service;

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
import com.dbserver.desafiovotacao.domain.service.AssociadoService;
import com.dbserver.desafiovotacao.domain.service.PautaService;
import com.dbserver.desafiovotacao.domain.service.VotoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.dbserver.desafiovotacao.domain.service.VotoService.NAO_FOI_POSSIVEL_LOCALIZAR_UM_VOTO_COM_ID;
import static com.dbserver.desafiovotacao.domain.service.VotoService.SESSAO_FINALIZADA;
import static com.dbserver.desafiovotacao.domain.service.VotoService.SESSAO_NAO_ABERTA;
import static java.text.MessageFormat.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VotoServiceTest {

    public static final String VOTO_SIM = "Sim";
    @InjectMocks
    private VotoService votoService;
    @Mock
    private PautaService pautaService;
    @Mock
    private AssociadoService associadoService;
    @Mock
    private VotoRepository votoRepository;
    @Captor
    private ArgumentCaptor<Voto> votoArgumentCaptor;

    @Test
    void deveLancarUmaException_QuandoNaoExistirUmVotoComOIdInformado() {

        long votoId = 1L;
        when(votoRepository.findById(votoId)).thenReturn(Optional.empty());

        try {
            votoService.buscarVotoPeloId(votoId);
            fail();
        } catch (VotoNaoEncontradoException e) {
            assertEquals(format(NAO_FOI_POSSIVEL_LOCALIZAR_UM_VOTO_COM_ID, votoId), e.getMessage());
        }
    }

    @Test
    void deveSalvarUmVotoComSucesso() throws PautaNaoEncontradaException, AssociadoNaoEncontradoException, PautaException {

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setSituacao(Situacao.VOTACAO_ABERTA);
        pauta.setDataHoraInicio(LocalDateTime.now());
        pauta.setDataHoraFim(pauta.getDataHoraInicio().plusMinutes(1));

        Associado associado = new Associado();
        associado.setId(1L);

        VotoInput votoInput = new VotoInput(associado.getId(), VOTO_SIM, pauta.getId());

        when(pautaService.buscarPautaPeloId(pauta.getId())).thenReturn(pauta);
        when(associadoService.buscarAssociadoPeloId(votoInput.getAssociadoId())).thenReturn(associado);

        votoService.salvarVoto(votoInput);
        verify(votoRepository).save(votoArgumentCaptor.capture());
        Voto voto = votoArgumentCaptor.getValue();
        assertEquals(associado, voto.getAssociado());
        assertEquals(pauta, voto.getPauta());
        assertEquals(votoInput.getVoto(), voto.getVoto());
    }

    @Test
    void deveLancarUmaException_QuandoASessaoParaAPautaNaoEstiverAberta()
            throws AssociadoNaoEncontradoException, PautaNaoEncontradaException {

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setSituacao(Situacao.VOTACAO_FECHADA);
        pauta.setDataHoraInicio(null);

        VotoInput votoInput = new VotoInput();
        votoInput.setPautaId(pauta.getId());

        when(pautaService.buscarPautaPeloId(pauta.getId())).thenReturn(pauta);

        try {
            votoService.salvarVoto(votoInput);
            fail();
        } catch (PautaException e) {
            assertEquals(SESSAO_NAO_ABERTA, e.getMessage());
        }
    }

    @Test
    void deveLancarUmaException_QuandoASessaoParaAPautaJaEstiverFinalizada()
            throws AssociadoNaoEncontradoException, PautaNaoEncontradaException {

        Pauta pauta = new Pauta();
        pauta.setId(1L);
        pauta.setSituacao(Situacao.VOTACAO_FECHADA);
        pauta.setDataHoraInicio(LocalDateTime.now());
        pauta.setDataHoraFim(pauta.getDataHoraInicio().plusMinutes(1));

        VotoInput votoInput = new VotoInput();
        votoInput.setPautaId(pauta.getId());

        when(pautaService.buscarPautaPeloId(pauta.getId())).thenReturn(pauta);

        try {
            votoService.salvarVoto(votoInput);
            fail();
        } catch (PautaException e) {
            assertEquals(SESSAO_FINALIZADA, e.getMessage());
        }
    }
}
