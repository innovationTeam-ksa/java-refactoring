package com.innovationTeam.refactoring.mapper;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer mapToCustomer(CustomerRequestDto customerRequestDto);

    CustomerResponse mapToCustomerResponse(Customer customer);
}
