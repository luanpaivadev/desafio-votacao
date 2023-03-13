package com.dbserver.desafiovotacao.api.v1.assembler;

import com.dbserver.desafiovotacao.api.v1.model.dto.AssociadoDto;
import com.dbserver.desafiovotacao.domain.model.Associado;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AssociadoAssembler implements MapperToDtoObject<Associado, AssociadoDto> {

    private final ModelMapper modelMapper;

    @Override
    public AssociadoDto toDtoObject(Associado associado) {
        return modelMapper.map(associado, AssociadoDto.class);
    }

    @Override
    public List<AssociadoDto> toListDtoObject(List<Associado> associados) {
        return associados.stream().map(associado -> modelMapper.map(associado, AssociadoDto.class)).toList();
    }
}
