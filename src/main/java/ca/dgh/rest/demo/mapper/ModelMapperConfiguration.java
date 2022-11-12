package ca.dgh.rest.demo.mapper;

import ca.dgh.rest.demo.model.AbstractEntity;
import ca.dgh.rest.demo.model.dto.AbstractDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperConfiguration {

    @Bean("modelMapper")
    public ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.createTypeMap(AbstractDTO.class, AbstractEntity.class);
        modelMapper.createTypeMap(AbstractEntity.class, AbstractDTO.class);
        return modelMapper;
    }
}
