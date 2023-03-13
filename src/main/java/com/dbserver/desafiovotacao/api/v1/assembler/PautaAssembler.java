package com.dbserver.desafiovotacao.api.v1.assembler;

import com.dbserver.desafiovotacao.api.v1.model.dto.PautaDto;
import com.dbserver.desafiovotacao.domain.model.Pauta;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PautaAssembler implements MapperToDtoObject<Pauta, PautaDto> {

    private final ModelMapper modelMapper;

    @Override
    public PautaDto toDtoObject(Pauta pauta) {
        return modelMapper.map(pauta, PautaDto.class);
    }

    @Override
    public List<PautaDto> toListDtoObject(List<Pauta> pautas) {
        return pautas.stream().map(pauta -> modelMapper.map(pauta, PautaDto.class)).toList();
    }
}
