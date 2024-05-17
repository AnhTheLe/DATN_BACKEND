package com.projectcnw.salesmanagement.configs;

import com.projectcnw.salesmanagement.dto.productDtos.BaseProductDto;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    public void ModelMapperConfig(){};
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setAmbiguityIgnored(true);
        return modelMapper;
    }
}
