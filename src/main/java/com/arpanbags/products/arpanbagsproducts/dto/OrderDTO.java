package com.arpanbags.products.arpanbagsproducts.dto;

import com.arpanbags.products.arpanbagsproducts.entity.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    private LocalDateTime orderDate;

    private List<OrderItemDTO> itemDTOs;

    private String orderNumber;

    private Long userId;
}
