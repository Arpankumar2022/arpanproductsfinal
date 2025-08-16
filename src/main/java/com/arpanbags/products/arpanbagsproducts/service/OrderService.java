package com.arpanbags.products.arpanbagsproducts.service;


import com.arpanbags.products.arpanbagsproducts.dto.OrderDTO;
import com.arpanbags.products.arpanbagsproducts.dto.OrderItemDTO;
import com.arpanbags.products.arpanbagsproducts.entity.OrderItem;
import com.arpanbags.products.arpanbagsproducts.entity.Orders;
import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import com.arpanbags.products.arpanbagsproducts.enums.OrderStatus;
import com.arpanbags.products.arpanbagsproducts.mapper.OrderMapper;
import com.arpanbags.products.arpanbagsproducts.repository.OrderItemRepository;
import com.arpanbags.products.arpanbagsproducts.repository.OrderRepository;
import com.arpanbags.products.arpanbagsproducts.repository.ProductsTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductsTypeRepository productsTypeRepository;

    public OrderDTO createOrder(OrderDTO orderDto) {
        Orders order = new Orders();
        order.setOrderDate(orderDto.getOrderDate());
        order.setOrderStatus(OrderStatus.BOOKED);
        order.setUserId(10L);
        order.setOrderNumber("ORDER_ID_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        for (OrderItemDTO itemDTO : orderDto.getItemDTOs()) {
            ProductsType productsType = productsTypeRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem item = new OrderItem();
            item.setProductType(productsType);
            item.setQuantity(itemDTO.getQuantity());

            order.addOrderItem(item); // also sets item.setOrder(order)
        }

        // Save order — will also save item1 because of CascadeType.ALL
        return OrderMapper.INSTANCE.toOrderDTO(orderRepository.save(order));
        /*orderItemRepository.saveAll(order.getItems());
      //  OrderDTO savedOrder= OrderMapper.INSTANCE.toOrderDTO(orderRepository.save(OrderMapper.INSTANCE.toOrders(orderDto)));
         */
    }

    /*private List<OrderItem> setCartItemsDTO(List<OrderItemDTO> items) {
        return items.stream().map(itemDTO->{
            OrderItem orderItem = new OrderItem();
            orderItem.setId(itemDTO.getId());
            orderItem.setPrice(itemDTO.getPrice());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setProductName(itemDTO.getProductName());
            return orderItem;
        }).collect(Collectors.toList());

    }*/

    public Optional<Orders> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> OrderMapper.INSTANCE.toOrderDTO(order)).toList();
    }

    public Orders updateOrder(Orders order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
