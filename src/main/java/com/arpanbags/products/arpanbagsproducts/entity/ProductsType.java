package com.arpanbags.products.arpanbagsproducts.entity;


import com.arpanbags.products.arpanbagsproducts.entity.categories.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.util.List;

@Data
@Entity
@EqualsAndHashCode
public class ProductsType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String productType;

    private String productName;

    private String productLogo;

    private String offer;

    private Double productPrice;

   /* @ManyToOne
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;
*/
    @ElementCollection
    private List<String> imagePaths;

    @Column(name = "subcategory_id")
    private Long subcategoryId;
}
