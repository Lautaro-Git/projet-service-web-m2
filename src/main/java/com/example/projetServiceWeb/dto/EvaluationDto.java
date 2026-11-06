package com.example.projetServiceWeb.dto;

import com.example.projetServiceWeb.entities.EvaluationEntity;
import com.example.projetServiceWeb.entities.RestaurantEntity;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {

    //@NotBlank
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String evaluateur;

    @NotBlank
    @Size(max = 255)
    private String commentaire;


    @Min(value = 0)
    @Max(value = 3)
    private int note;

    public static EvaluationDto fromEntity(EvaluationEntity entity) {
        return EvaluationDto.builder()
                .id(entity.getId())
                .evaluateur(entity.getEvaluateur())
                .commentaire(entity.getCommentaire())
                .note(entity.getNote())
                .build();
    }

}
