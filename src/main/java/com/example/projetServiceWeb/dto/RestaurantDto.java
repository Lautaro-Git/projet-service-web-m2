package com.example.projetServiceWeb.dto;

import com.example.projetServiceWeb.entities.EvaluationEntity;
import com.example.projetServiceWeb.entities.RestaurantEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {

    //@NotBlank
    private Long id;

    @NotBlank
    @Size(max = 90)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String adress;

    private double moyenne;

    private List<EvaluationDto> evaluations;

    public static RestaurantDto fromEntity(RestaurantEntity entity) {
        return RestaurantDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .adress(entity.getAdress())
                .moyenne(entity.getEvaluations().stream().mapToInt(EvaluationEntity::getNote).average().orElse(-1))
                .evaluations(entity.getEvaluations().stream().map(EvaluationDto::fromEntity).toList())
                .build();
    }

}
