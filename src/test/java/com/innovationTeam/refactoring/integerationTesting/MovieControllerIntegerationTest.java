package com.innovationTeam.refactoring.integerationTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovationTeam.refactoring.entity.Movie;
import com.innovationTeam.refactoring.enums.Code;
import com.innovationTeam.refactoring.model.request.CustomerRequestDto;
import com.innovationTeam.refactoring.model.request.MovieRequestDto;
import com.innovationTeam.refactoring.repository.MovieRepository;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class MovieControllerIntegerationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private CustomerService customerService;

    @MockBean
    private MovieRepository movieRepository;

    @InjectMocks
    private com.innovationTeam.refactoring.controller.MovieController customerController;


    private CustomerRequestDto customerRequestDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setName("Customer name");
    }


    @Test
    void testGetAllMoviesSuccess() throws Exception {
        Movie movie1 = new Movie(1L, "Movie 1", Code.NEW);
        Movie movie2 = new Movie(2L, "Movie 2", Code.REGULAR);
        List<Movie> movieResponses = Arrays.asList(movie1, movie2);

        Mockito.when(movieRepository.findAll()).thenReturn(movieResponses);

        mockMvc.perform(get("/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void testGetAllMoviesServerError() throws Exception {
        when(movieRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateMovieSuccess() throws Exception {
        MovieRequestDto movieRequestDto = new MovieRequestDto();
        movieRequestDto.setTitle("New Movie");

        when(movieRepository.save(any())).thenReturn(new Movie(1L, "new Movie", Code.NEW));

        mockMvc.perform(post("/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieRequestDto)))
                .andExpect(status().isCreated());

    }


}
