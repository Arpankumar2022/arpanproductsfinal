package com.arpanbags.products.arpanbagsproducts.controller;

import com.arpanbags.products.arpanbagsproducts.dto.RestaurantDTO;
import com.arpanbags.products.arpanbagsproducts.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/create")
    public ResponseEntity<RestaurantDTO> createItem(@RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO reataurant = restaurantService.createRestaurant(restaurantDTO);
      //  return ResponseEntity.created(URI.create("/api/items/" + newItem.getId())).body(newItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(reataurant);
    }

    // Read operation
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        RestaurantDTO restaurantDTO = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurantDTO);
    }

    // Update operation
    @PutMapping
    public ResponseEntity<RestaurantDTO> updateItem(@RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO updatedItem = restaurantService.updateRestaurant(restaurantDTO);
        return ResponseEntity.ok(updatedItem);
    }

    // Delete operation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    // Read all restaurants operation
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<RestaurantDTO> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

}
