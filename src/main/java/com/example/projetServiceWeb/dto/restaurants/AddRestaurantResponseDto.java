package com.example.projetServiceWeb.dto.restaurants;


import com.example.projetServiceWeb.entities.RestaurantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddRestaurantResponseDto {
    private Long id;

    public static AddRestaurantResponseDto fromEntity(RestaurantEntity entity){
        return AddRestaurantResponseDto.builder()
                .id(entity.getId())
                .build();
    }
}
