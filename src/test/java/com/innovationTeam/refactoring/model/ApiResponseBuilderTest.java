package com.innovationTeam.refactoring.model;

import com.innovationTeam.refactoring.model.response.ApiResponseBuilder;
import com.innovationTeam.refactoring.model.response.ApiResponseModel;
import com.innovationTeam.refactoring.model.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ApiResponseBuilderTest {

    @InjectMocks
    private ApiResponseBuilder<String> apiResponseBuilder;


    @Test
    public void testSuccess() {
        ApiResponseModel<String> response = apiResponseBuilder.success().build();

        assertEquals("Succeed", response.getResult());
        assertEquals("200", response.getStatus());
        assertNull(response.getData());
        assertNull(response.getError());
    }

    @Test
    public void testError() {
        ErrorResponse errorResponse = new ErrorResponse("Error Code", "Error Message", "Error Detail");

        ApiResponseModel<String> response = apiResponseBuilder.error(errorResponse).build();

        assertEquals("Failed", response.getResult());
        assertEquals("500", response.getStatus());
        assertNull(response.getData());
        assertNotNull(response.getError());
        assertEquals(errorResponse, response.getError());
    }

    @Test
    public void testAddData() {
        String data = "Test Data";

        ApiResponseModel<String> response = apiResponseBuilder.addData(data).build();

        assertEquals("Succeeded", response.getResult());
        assertEquals("200", response.getStatus());
        assertEquals(data, response.getData());
        assertNull(response.getError());
    }

    @Test
    public void testSuccessWithAddData() {
        String data = "Test Data";

        ApiResponseModel<String> response = apiResponseBuilder.success().addData(data).build();

        assertEquals("Succeeded", response.getResult());
        assertEquals("200", response.getStatus());
        assertEquals(data, response.getData());
        assertNull(response.getError());
    }

    @Test
    public void testSuccessWithError() {
        ErrorResponse errorResponse = new ErrorResponse("Error Code", "Error Message", "Error Detail");

        ApiResponseModel<String> response = apiResponseBuilder.success().error(errorResponse).build();

        assertEquals("Failed", response.getResult());
        assertEquals("500", response.getStatus());
        assertNull(response.getData());
        assertNotNull(response.getError());
        assertEquals(errorResponse, response.getError());
    }

    @Test
    public void testBuildDirectly() {
        String data = "Test Data";

        ApiResponseModel<String> response = apiResponseBuilder.result("Succeeded").status("200").addData(data).build();

        assertEquals("Succeeded", response.getResult());
        assertEquals("200", response.getStatus());
        assertEquals(data, response.getData());
        assertNull(response.getError());
    }

    @Test
    public void testDefaultValues() {
        ApiResponseModel<String> response = apiResponseBuilder.build();

        assertNull(response.getResult());
        assertNull(response.getStatus());
        assertNull(response.getData());
        assertNull(response.getError());
    }

}
