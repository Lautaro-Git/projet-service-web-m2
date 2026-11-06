package com.example.projetServiceWeb.dto.evaluations;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEvaluationDto
{
    @NotBlank
    @Size(max=255)
    private String commentaire;

    @NotNull
    @Min(0)
    @Max(3)
    private Integer note;
}
