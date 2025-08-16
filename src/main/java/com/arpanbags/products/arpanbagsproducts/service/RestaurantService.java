package com.arpanbags.products.arpanbagsproducts.service;


import com.arpanbags.products.arpanbagsproducts.dto.RestaurantDTO;
import com.arpanbags.products.arpanbagsproducts.entity.Restaurant;
import com.arpanbags.products.arpanbagsproducts.mapper.RestaurantMapper;
import com.arpanbags.products.arpanbagsproducts.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;


    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) {
        // Convert DTO to entity
        Restaurant restaurant = RestaurantMapper.INSTANCE.mapRestaurantDTOToRestaurant(restaurantDTO);
        // Save the restaurant entity
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        // Convert the saved entity back to DTO and return
        return RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(savedRestaurant);
    }

    // Read operation
    public RestaurantDTO getRestaurantById(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (optionalRestaurant.isPresent()) {
            return RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(optionalRestaurant.get());
        } else {
            throw new EntityNotFoundException("Restaurant not found with id: " + id);
        }
    }

    // Update operation
    public RestaurantDTO updateRestaurant(RestaurantDTO restaurantDTO) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantDTO.getId());
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            restaurant.setName(restaurantDTO.getName());
            restaurant.setAddress(restaurantDTO.getAddress());
            restaurant.setCity(restaurantDTO.getCity());
            restaurant.setRestaurantDescription(restaurantDTO.getRestaurantDescription());
            Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
            return RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(updatedRestaurant);
        } else {
            throw new EntityNotFoundException("Restaurant not found with id: " + restaurantDTO.getId());
        }
    }

    // Delete operation
    public void deleteRestaurant(Long id) {
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Restaurant not found with id: " + id);
        }
    }

    // Read all restaurants operation
    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        // Map all objects db to DTO
      return  restaurants.stream().map(restaurant -> RestaurantMapper.INSTANCE
                .mapRestaurantToRestaurantDTO(restaurant)).collect(Collectors.toList());
    }


}




