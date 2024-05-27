package com.innovationTeam.refactoring.integerationTesting;//package com.innovationTeam.refactoring.integerationTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovationTeam.refactoring.entity.Customer;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.repository.CustomerRepository;
import com.innovationTeam.refactoring.service.impl.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CustomerControllerIntegerationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @InjectMocks
    private com.innovationTeam.refactoring.controller.CustomerController customerController;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerRequestDto customerRequestDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setName("Customer name");
    }

    @Test
    public void testGetAllCustomersSuccess() throws Exception {
        when(customerRepository.findAll()).thenReturn(List.of(new Customer(1L, "mego", new ArrayList<>())));
        mockMvc.perform(get("/v1/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void testGetAllCustomersEmptySuccess() throws Exception {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v1/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateCustomerSuccess() throws Exception {
        when(customerRepository.save(any())).thenReturn(new Customer(1L, "Customer name", new ArrayList<>()));

        mockMvc.perform(post("/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequestDto)))
                .andExpect(status().isCreated());
    }


    @Test
    public void testGetCustomerStatementSuccess() throws Exception {
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer(1L, "Customer name", new ArrayList<>())));

        mockMvc.perform(get("/v1/customers/1/statement")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }


}




