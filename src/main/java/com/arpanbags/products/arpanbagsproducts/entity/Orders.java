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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id") // this creates the FK in OrderItem table
    private List<OrderItem> items = new ArrayList<>();

    private String orderNumber;

    private Long userId;

    private String orderStatus;

    public void addOrderItem(OrderItem item) {
        items.add(item);
    }

}
