package com.arpanbags.products.arpanbagsproducts.controller;


import com.arpanbags.products.arpanbagsproducts.dto.OrderDTO;
import com.arpanbags.products.arpanbagsproducts.entity.Orders;
import com.arpanbags.products.arpanbagsproducts.enums.OrderStatus;
import com.arpanbags.products.arpanbagsproducts.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDto) {
        OrderDTO createdOrder = orderService.createOrder(orderDto);
        return new ResponseEntity<OrderDTO>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
        Optional<Orders> order = orderService.getOrderById(id);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orders> updateOrder(@PathVariable Long id, @RequestBody Orders orderDetails) {
        Optional<Orders> existingOrder = orderService.getOrderById(id);
        if (existingOrder.isPresent()) {
            Orders orderToUpdate = existingOrder.get();
            orderToUpdate.setOrderNumber(orderDetails.getOrderNumber());
            orderToUpdate.setUserId(10L);
            // Update other fields as needed
            orderToUpdate.setOrderStatus(OrderStatus.SHIPPED);
            Orders updatedOrder = orderService.updateOrder(orderToUpdate);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.getOrderById(id).isPresent()) {
            orderService.deleteOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
