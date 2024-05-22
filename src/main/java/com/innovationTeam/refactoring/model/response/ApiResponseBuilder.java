package com.innovationTeam.refactoring.model.response;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiResponseBuilder<T> {
    private final ApiResponseModel<T> apiResponse = new ApiResponseModel<>();

    public ApiResponseBuilder<T> result(String result) {
        apiResponse.setResult(result);
        return this;
    }

    public ApiResponseBuilder<T> status(String status) {
        apiResponse.setStatus(status);
        return this;
    }

    public ApiResponseBuilder<T> success() {
        return new ApiResponseBuilder<T>().result("Succeed").status("200");
    }

    public ApiResponseBuilder<T> fail() {
        return new ApiResponseBuilder<T>().result("Failed").status("500");
    }

    public ApiResponseBuilder<T> error(ErrorResponse error) {
        apiResponse.setError(error);
        apiResponse.setResult("Failed");
        apiResponse.setStatus("500");
        return this;
    }

    public ApiResponseBuilder<T> addData(T body) {
        apiResponse.setData(body);
        apiResponse.setResult("Succeeded");
        apiResponse.setStatus("200");
        return this;
    }

    public ApiResponseModel<T> build() {
        return apiResponse;
    }
}
