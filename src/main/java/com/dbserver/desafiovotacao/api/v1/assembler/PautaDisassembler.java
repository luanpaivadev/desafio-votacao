package com.dbserver.desafiovotacao.api.v1.assembler;

import com.dbserver.desafiovotacao.api.v1.model.input.PautaInput;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PautaDisassembler implements MapperToDomainObject<PautaInput, Pauta> {

    private final ModelMapper modelMapper;

    @Override
    public Pauta toDomainObject(PautaInput pautaInput) {
        return modelMapper.map(pautaInput, Pauta.class);
    }

    @Override
    public List<Pauta> toListDomainObject(List<PautaInput> pautaInputList) {
        return pautaInputList.stream().map(pautaInput -> modelMapper.map(pautaInput, Pauta.class)).toList();
    }
}
