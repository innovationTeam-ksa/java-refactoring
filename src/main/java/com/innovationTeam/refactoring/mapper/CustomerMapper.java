package com.innovationTeam.refactoring.mapper;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.model.CustomerRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer mapToCustomer(CustomerRequestDto customerRequestDto);
}
