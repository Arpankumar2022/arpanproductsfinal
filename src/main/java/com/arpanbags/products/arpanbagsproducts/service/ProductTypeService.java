package com.arpanbags.products.arpanbagsproducts.service;


import com.arpanbags.products.arpanbagsproducts.dto.ProductsTypeDTO;
import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import com.arpanbags.products.arpanbagsproducts.entity.categories.SubCategory;
import com.arpanbags.products.arpanbagsproducts.mapper.ProductsTypeMapper;
import com.arpanbags.products.arpanbagsproducts.mapper.RestaurantMapper;
import com.arpanbags.products.arpanbagsproducts.repository.ProductsTypeRepository;
import com.arpanbags.products.arpanbagsproducts.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTypeService {

    private final ProductsTypeRepository productsTypeRepository;

    private final SubcategoryRepository subcategoryRepository;

    public List<ProductsTypeDTO> getAllProductTypes() {
        List<ProductsType>  productsTypeList = productsTypeRepository.findAll();

        productsTypeList.stream().map(product -> {
            ProductsTypeDTO dto = new ProductsTypeDTO();
            dto.setId(product.getId());
            dto.setProductType(product.getProductType());
            dto.setProductName(product.getProductName());
            dto.setProductLogo(product.getProductLogo());
            dto.setOffer(product.getOffer());
            dto.setProductPrice(product.getProductPrice());
            List<String> imagePaths = getImagePath("C:\\Users\\arpan\\uploads\\",product.getProductName());
           // dto.setImages(imagePaths);
            //dto.setImages(product.getImagePaths()); // already stored as List<String>

            // Subcategory is an entity; get only its ID
            if (product.getSubcategoryId() != null) {
                dto.setSubCategoryId(product.getSubcategoryId());
            }

            return dto;
        }).collect(Collectors.toList());
        return new ArrayList<>();
    }

    public ProductsTypeDTO getProductTypeById(Long id) {
        Optional<ProductsType> optionalProductType = productsTypeRepository.findById(id);
        // return optionalProductType.orElseThrow(() -> new RuntimeException("ProductType not found with id: " + id));
        if (optionalProductType.isPresent()) {
            return ProductsTypeMapper.INSTANCE.mapProductsTypeToProductsTypeDTO(optionalProductType.get());
        } else {
            throw new RuntimeException("ProductType not found with id: " + id);
        }
    }

    public ProductsTypeDTO createProductType(ProductsTypeDTO productsTypeDTO) {
        SubCategory subCategory = subcategoryRepository.findById(productsTypeDTO.getSubCategoryId())
                .orElseThrow(() -> new RuntimeException("Subcategory not found"));
        if (Objects.nonNull(subCategory)) {
            ProductsType productType = new ProductsType();
            productType.setProductName(productsTypeDTO.getProductName());
            productType.setProductType(productsTypeDTO.getProductType());
            productType.setProductLogo(productsTypeDTO.getProductLogo());
            productType.setOffer(productsTypeDTO.getOffer());
            productType.setProductPrice(productsTypeDTO.getProductPrice());
            List<String> imagePaths = saveOrGetImages(productsTypeDTO.getImages(),productsTypeDTO.getProductName() ,true);
            productType.setImagePaths(imagePaths);
            //productType.setSubcategoryId(productsTypeDTO.getSubCategoryId());
            productType.setSubcategoryId(productsTypeDTO.getSubCategoryId());
            return ProductsTypeMapper.INSTANCE.mapProductsTypeToProductsTypeDTO(productsTypeRepository.save(productType));
        }
        return null;
    }

    public ProductsTypeDTO updateProductType(ProductsTypeDTO productsTypeDTO) {
        Optional<ProductsType> optionalProductsType = productsTypeRepository.findById(productsTypeDTO.getId());
        if (optionalProductsType.isPresent()) {
            ProductsType productsType = optionalProductsType.get();
            productsType.setProductType(productsTypeDTO.getProductType());
            productsType.setProductName(productsTypeDTO.getProductName());
            productsTypeRepository.save(productsType);
            return ProductsTypeMapper.INSTANCE.mapProductsTypeToProductsTypeDTO(productsType);
        } else {
            throw new RuntimeException("ProductType not found with id: " + productsTypeDTO.getId());
        }
    }

    public void deleteProductType(Long id) {
        if (!productsTypeRepository.existsById(id)) {
            throw new RuntimeException("ProductType not found with id: " + id);
        }
        productsTypeRepository.deleteById(id);
    }


    public ProductsTypeDTO getProductTypeByProductName(String productName) {
        return ProductsTypeMapper.INSTANCE
                .mapProductsTypeToProductsTypeDTO(productsTypeRepository
                        .findByProductName(productName).get());

    }

    public List<String> saveOrGetImages(List<MultipartFile> images, String productName, boolean isProductSave) {
        List<String> imagePaths = new ArrayList<>();

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String fileName = UUID.randomUUID() + "_" +productName+"_"+image.getOriginalFilename();
                    Path filePath = Paths.get("C:\\Users\\arpan\\uploads\\" + fileName);
                    if(isProductSave) {
                        Files.copy(image.getInputStream(), filePath);
                    }
                    imagePaths.add(filePath.toString()); // or use relative path or URL
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image: " + image.getOriginalFilename(), e);
                }
            }
        }
        return imagePaths;
    }

    public List<String> getImagePath(String folderPath, String productName) {
        File folder = new File(folderPath);
        List<String> imagePaths = new ArrayList<>();

        if (folder.exists() && folder.isDirectory()) {
           // return  Arrays.stream(folder.listFiles()).anyMatch(file -> file.getName().contains(productName)).

          //  }

            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    imagePaths.add(file.getName());
                }
            }
        }
        return imagePaths.stream().filter(imagePath->imagePath.contains(productName)).toList();
    }



}
