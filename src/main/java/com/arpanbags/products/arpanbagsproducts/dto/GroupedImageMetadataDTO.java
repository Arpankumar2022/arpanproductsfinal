package com.arpanbags.products.arpanbagsproducts.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupedImageMetadataDTO {
    private List<String> fileUrls;
    private String productType;
    private String productName;
    private int subCategoryID;


}
