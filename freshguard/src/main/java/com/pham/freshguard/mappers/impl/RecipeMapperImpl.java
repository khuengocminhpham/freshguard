package com.pham.freshguard.mappers.impl;

import com.pham.freshguard.domain.dto.RecipeDto;
import com.pham.freshguard.domain.entities.RecipeEntity;
import com.pham.freshguard.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapperImpl implements Mapper<RecipeEntity, RecipeDto> {
    private ModelMapper modelMapper;

    public RecipeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RecipeDto mapTo(RecipeEntity recipeEntity) {
        return modelMapper.map(recipeEntity, RecipeDto.class);
    }

    @Override
    public RecipeEntity mapFrom(RecipeDto recipeDto) {
        return modelMapper.map(recipeDto, RecipeEntity.class);
    }

}
