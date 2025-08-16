package com.arpanbags.products.arpanbagsproducts.entity.categories;

import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "subcategory_id")
    private List<ProductsType> productsTypes;

    @Column(name = "category_id")
    private Long categoryId;

}
