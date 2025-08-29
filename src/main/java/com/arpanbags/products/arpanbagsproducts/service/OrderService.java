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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductsTypeRepository productsTypeRepository;

    public OrderDTO createOrder(OrderDTO orderDto) {
        Orders order = new Orders();
        order.setOrderDate(orderDto.getOrderDate());
        order.setOrderStatus(OrderStatus.BOOKED.getDescription());
        order.setUserId(orderDto.getUserId());
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


    public Orders updateOrder(Orders order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
