package com.example.projetServiceWeb.service;

import com.example.projetServiceWeb.dto.RestaurantDto;
import com.example.projetServiceWeb.entities.RestaurantEntity;
import com.example.projetServiceWeb.exception.NotFoundException;
import com.example.projetServiceWeb.repositories.RestaurantRepository;
import com.example.projetServiceWeb.repositories.S3Repository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final S3Repository s3Repository;
    private static final String BUCKET = "restaurant";

    public List<RestaurantEntity> getAll() {
        return restaurantRepository.findAll();
    }

    public RestaurantEntity createRestaurant(String name, String address) {
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setName(name);
        restaurantEntity.setAdress(address);
        return  restaurantRepository.save(restaurantEntity);
    }

    public RestaurantEntity getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant introuvable"));
    }

    public RestaurantEntity updateRestaurant(Long id, String name, String adress) {
        RestaurantEntity restaurantEntity = this.getRestaurantById(id);
        restaurantEntity.setName(name);
        restaurantEntity.setAdress(adress);
        return this.restaurantRepository.save(restaurantEntity);
    }

    public String getImageKey(Long id){
        return String.valueOf(id);
    }


    public String getImageUrl(Long id){
        RestaurantEntity restaurantEntity = this.getRestaurantById(id);
        return this.s3Repository.getFileUrl(BUCKET, getImageKey(restaurantEntity.getId()));
    }

    public String getImageUploadUrl(Long id) {
        RestaurantEntity restaurantEntity = this.getRestaurantById(id);
        return this.s3Repository.getUploadFileUrl(BUCKET, getImageKey(restaurantEntity.getId()));
    }


}
