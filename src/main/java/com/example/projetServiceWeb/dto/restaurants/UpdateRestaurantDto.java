package com.example.projetServiceWeb.dto.restaurants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRestaurantDto {

    @NotBlank
    @Size(max = 90)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String adress;


}
