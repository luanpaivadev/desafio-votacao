package com.dbserver.desafiovotacao.domain.scheduler;

import com.dbserver.desafiovotacao.domain.service.PautaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class PautaScheduler {

    @Autowired
    private PautaService pautaService;

    @Scheduled(fixedRate = 30000)
    public void fecharVotacao() {
        pautaService.fecharVotacao();
    }
}
