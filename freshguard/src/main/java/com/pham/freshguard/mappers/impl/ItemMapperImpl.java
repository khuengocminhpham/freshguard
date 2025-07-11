package com.pham.freshguard.mappers.impl;

import com.pham.freshguard.domain.dto.ItemDto;
import com.pham.freshguard.domain.entities.ItemEntity;
import com.pham.freshguard.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ItemMapperImpl implements Mapper<ItemEntity, ItemDto> {
    private ModelMapper modelMapper;

    public ItemMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ItemDto mapTo(ItemEntity itemEntity) {
        return modelMapper.map(itemEntity, ItemDto.class);
    }

    @Override
    public ItemEntity mapFrom(ItemDto itemDto) {
        return modelMapper.map(itemDto, ItemEntity.class);
    }

}
