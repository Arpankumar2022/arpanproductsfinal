package com.arpanbags.products.arpanbagsproducts.service;


import com.arpanbags.products.arpanbagsproducts.dto.OrderDTO;
import com.arpanbags.products.arpanbagsproducts.dto.OrderItemDTO;
import com.arpanbags.products.arpanbagsproducts.entity.OrderItem;
import com.arpanbags.products.arpanbagsproducts.entity.Orders;
import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import com.arpanbags.products.arpanbagsproducts.enums.OrderStatus;
import com.arpanbags.products.arpanbagsproducts.mapper.OrderItemMapper;
import com.arpanbags.products.arpanbagsproducts.mapper.OrderMapper;
import com.arpanbags.products.arpanbagsproducts.repository.OrderItemRepository;
import com.arpanbags.products.arpanbagsproducts.repository.OrderRepository;
import com.arpanbags.products.arpanbagsproducts.repository.ProductsTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductsTypeRepository productsTypeRepository;

    public OrderDTO createOrder(OrderDTO orderDto, long userId) {
        Orders order = new Orders();
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.BOOKED.getDescription());
        order.setUserId(userId);
        order.setOrderNumber("ORDER_ID_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        for (OrderItemDTO itemDTO : orderDto.getItemDTOs()) {
            ProductsType productsType = productsTypeRepository.findById(itemDTO.getProductID())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem item = new OrderItem();
            item.setProduct(productsType);
            item.setQuantity(itemDTO.getQuantity());
            order.addOrderItem(item); // also sets item.setOrder(order)
        }
        return OrderMapper.INSTANCE.toOrderDTO(orderRepository.save(order));
    }

    public Optional<Orders> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    Set<OrderItemDTO> itemDTOs = order.getItems().stream()
                            .map(OrderItemMapper.INSTANCE::toOrderItemDTO)
                            .collect(Collectors.toSet());
                    // Convert Order to DTO
                    OrderDTO orderDTO = OrderMapper.INSTANCE.toOrderDTO(order);
                    orderDTO.setItemDTOs(new ArrayList<>(itemDTOs));
                    return orderDTO;
                })
                .collect(Collectors.toList());
    }



    public OrderDTO updateOrder(OrderDTO orderDto, Long userId) {
        // Step 1: Fetch existing order
        Orders existingOrder = orderRepository.findById(orderDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Step 2: Validate user ownership
        if (!existingOrder.getUserId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to update this order.");
        }

        // Step 3: Update fields
        existingOrder.setOrderStatus(orderDto.getOrderStatus());
        existingOrder.setOrderDate(orderDto.getOrderDate() != null ? orderDto.getOrderDate() : existingOrder.getOrderDate());
        existingOrder.setOrderNumber(orderDto.getOrderNumber());

        // Update items - assume you have a utility method for this
        List<OrderItem> updatedItems = mapItemDTOsToEntities(orderDto.getItemDTOs());
        existingOrder.setItems(updatedItems);

        // Step 4: Save updated order
        Orders savedOrder = orderRepository.save(existingOrder);

        // Step 5: Convert to DTO and return
        return toDTO(savedOrder);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<OrderItem> mapItemDTOsToEntities(List<OrderItemDTO> itemDTOs) {
        if (itemDTOs == null) return new ArrayList<>();
        return itemDTOs.stream()
                .map(this::toItemEntity)
                .collect(Collectors.toList());
    }

    private OrderItemDTO toItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        //dto.setId(item.getId());
        //dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
       // dto.setPrice(item.getPrice());
        return dto;
    }

    private OrderItem toItemEntity(OrderItemDTO dto) {
        OrderItem item = new OrderItem();
       // item.setId(dto.get);
       // item.setProductId(dto.getProductId());
        item.setQuantity(dto.getQuantity());
       // item.setPrice(dto.getPrice());
        return item;
    }

    public OrderDTO toDTO(Orders order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setUserId(order.getId());

        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
        dto.setItemDTOs(itemDTOs);

        return dto;
    }
}
