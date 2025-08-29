package com.arpanbags.products.arpanbagsproducts.mapper;


import com.arpanbags.products.arpanbagsproducts.dto.OrderDTO;
import com.arpanbags.products.arpanbagsproducts.dto.OrderItemDTO;
import com.arpanbags.products.arpanbagsproducts.entity.OrderItem;
import com.arpanbags.products.arpanbagsproducts.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    OrderItem toOrderItem(OrderItemDTO orderItemDTO);

}
