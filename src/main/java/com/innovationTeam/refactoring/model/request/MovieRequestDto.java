package com.innovationTeam.refactoring.model.request;

import com.innovationTeam.refactoring.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequestDto {
    private String title;
    private Code code;
}
