package com.arpanbags.products.arpanbagsproducts.entity;

import com.arpanbags.products.arpanbagsproducts.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<OrderItem> items=new ArrayList<>();;

    private String orderNumber;

    private Long userId;

    private OrderStatus orderStatus;

    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrders(this); // very important!
    }

}
