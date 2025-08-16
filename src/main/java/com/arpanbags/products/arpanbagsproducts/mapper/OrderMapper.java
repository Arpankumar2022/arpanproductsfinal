package com.arpanbags.products.arpanbagsproducts.mapper;

import com.arpanbags.products.arpanbagsproducts.dto.OrderDTO;
import com.arpanbags.products.arpanbagsproducts.dto.OrderItemDTO;
import com.arpanbags.products.arpanbagsproducts.entity.OrderItem;
import com.arpanbags.products.arpanbagsproducts.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO toOrderDTO(Orders save);
}
