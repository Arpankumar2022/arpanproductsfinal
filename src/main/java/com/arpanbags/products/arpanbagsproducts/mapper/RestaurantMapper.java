package com.arpanbags.products.arpanbagsproducts.mapper;


import com.arpanbags.products.arpanbagsproducts.dto.RestaurantDTO;
import com.arpanbags.products.arpanbagsproducts.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantMapper {
    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    Restaurant mapRestaurantDTOToRestaurant(RestaurantDTO restaurantDTO);
    RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant);

}
