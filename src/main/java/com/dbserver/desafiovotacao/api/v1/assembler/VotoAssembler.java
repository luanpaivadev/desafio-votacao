package com.dbserver.desafiovotacao.api.v1.assembler;

import com.dbserver.desafiovotacao.api.v1.model.dto.VotoDto;
import com.dbserver.desafiovotacao.domain.model.Voto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VotoAssembler implements MapperToDtoObject<Voto, VotoDto> {

    private final ModelMapper modelMapper;

    @Override
    public VotoDto toDtoObject(Voto voto) {
        return modelMapper.map(voto, VotoDto.class);
    }

    @Override
    public List<VotoDto> toListDtoObject(List<Voto> votos) {
        return votos.stream().map(voto -> modelMapper.map(voto, VotoDto.class)).toList();
    }
}
