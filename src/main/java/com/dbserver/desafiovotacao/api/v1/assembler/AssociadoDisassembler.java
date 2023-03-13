package com.dbserver.desafiovotacao.api.v1.assembler;

import com.dbserver.desafiovotacao.api.v1.model.input.AssociadoInput;
import com.dbserver.desafiovotacao.domain.model.Associado;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AssociadoDisassembler implements MapperToDomainObject<AssociadoInput, Associado> {

    private final ModelMapper modelMapper;

    @Override
    public Associado toDomainObject(AssociadoInput associadoInput) {
        return modelMapper.map(associadoInput, Associado.class);
    }

    @Override
    public List<Associado> toListDomainObject(List<AssociadoInput> associadoInputs) {
        return associadoInputs.stream().map(associadoInput -> modelMapper.map(associadoInput, Associado.class)).toList();
    }
}
