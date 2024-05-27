package com.innovationTeam.refactoring.mapper;

import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.response.CustomerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(MockitoExtension.class)
public class CustomerMapperTest {

    private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    void shouldMapCustomerRequestDtoToCustomer() {
        CustomerRequestDto dto = new CustomerRequestDto();
        dto.setName("Customer name");

        Customer customer = mapper.mapToCustomer(dto);

        assertEquals(dto.getName(), customer.getName());
    }

    @Test
    void shouldMapCustomerToCustomerResponse() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer name");

        CustomerResponse response = mapper.mapToCustomerResponse(customer);

        assertEquals(customer.getName(), response.getName());
    }

    @Test
    void shouldReturnNullWhenMappingNullCustomerRequestDto() {

        Customer customer = mapper.mapToCustomer(null);

        assertNull(customer);
    }

    @Test
    void shouldReturnNullWhenMappingNullCustomer() {
        CustomerResponse response = mapper.mapToCustomerResponse(null);

        assertNull(response);
    }
}
