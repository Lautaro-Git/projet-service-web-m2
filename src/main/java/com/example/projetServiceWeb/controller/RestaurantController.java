package com.example.projetServiceWeb.controller;

import com.example.projetServiceWeb.dto.ImagePutDto;
import com.example.projetServiceWeb.dto.RestaurantDto;
import com.example.projetServiceWeb.dto.restaurants.AddRestaurantResponseDto;
import com.example.projetServiceWeb.dto.restaurants.UpdateRestaurantDto;
import com.example.projetServiceWeb.service.EvaluationService;
import com.example.projetServiceWeb.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantDto> getRestaurants() {
        return this.restaurantService.getAll().stream()
                .map(RestaurantDto::fromEntity)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AddRestaurantResponseDto> addRestaurant(@Valid @RequestBody RestaurantDto restaurant) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AddRestaurantResponseDto.fromEntity(this.restaurantService.createRestaurant(restaurant.getName(), restaurant.getAdress())));
    }

    @GetMapping("/{id}")
    public RestaurantDto getRestaurantById(@PathVariable Long id) {
        return RestaurantDto.fromEntity(this.restaurantService.getRestaurantById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public RestaurantDto updateRestaurant(@PathVariable Long id, @Valid @RequestBody UpdateRestaurantDto restaurantDto){
        return RestaurantDto.fromEntity(this.restaurantService.updateRestaurant(id, restaurantDto.getName(), restaurantDto.getAdress()));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/image")
    public ImagePutDto getImageUploadUrl(@PathVariable Long id) {
        return new ImagePutDto(this.restaurantService.getImageUploadUrl(id));
    }


    @GetMapping("/{id}/image")
    public ResponseEntity<Void> getImage(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(this.restaurantService.getImageUrl(id)))
                .build();
    }


}
