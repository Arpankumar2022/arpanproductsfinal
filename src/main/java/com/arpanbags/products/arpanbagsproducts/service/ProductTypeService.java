package com.arpanbags.products.arpanbagsproducts.service;


import com.arpanbags.products.arpanbagsproducts.controller.ImageController;
import com.arpanbags.products.arpanbagsproducts.dto.GroupedImageMetadataDTO;
import com.arpanbags.products.arpanbagsproducts.dto.ProductsTypeDTO;
import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import com.arpanbags.products.arpanbagsproducts.entity.categories.SubCategory;
import com.arpanbags.products.arpanbagsproducts.mapper.ProductsTypeMapper;
import com.arpanbags.products.arpanbagsproducts.repository.ProductsTypeRepository;
import com.arpanbags.products.arpanbagsproducts.repository.SubcategoryRepository;
import com.arpanbags.products.arpanbagsproducts.util.ProductTypeUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductTypeService {

    private final ProductsTypeRepository productsTypeRepository;

    private final SubcategoryRepository subcategoryRepository;

    private final ProductTypeUtils productTypeUtils;

    private static final String IMAGE_UPLOAD_DIR = "/opt/arpanbags/uploads/images/";

    public List<ProductsTypeDTO> getAllProductTypes(HttpServletRequest request) {
        List<ProductsType> productsTypeList = productsTypeRepository.findAll();
        return productsTypeList.stream().map(productTypeUtils::getProductTypeDTO).collect(Collectors.toList());
    }

    public ProductsTypeDTO getProductTypeById(Long id) {
        Optional<ProductsType> optionalProductType = productsTypeRepository.findById(id);
        // return optionalProductType.orElseThrow(() -> new RuntimeException("ProductType not found with id: " + id));
        if (optionalProductType.isPresent()) {
            ProductsType productsType = optionalProductType.get();
            ProductsTypeDTO productsTypeDTO = new ProductsTypeDTO();
            productsTypeDTO.setProductLogo(productsType.getProductLogo());
            productsTypeDTO.setProductPrice(productsType.getProductPrice());
            productsTypeDTO.setProductType(productsType.getProductType());
            productsTypeDTO.setProductName(productsType.getProductName());
            productsTypeDTO.setId(productsType.getId());
            productsTypeDTO.setSubCategoryId(productsType.getSubcategoryId());
            productsTypeDTO.setFileUrls(productsTypeRepository.findImagePathsByProductTypeId(productsType.getId()));
            return productsTypeDTO;
        } else {
            throw new RuntimeException("ProductType not found with id: " + id);
        }
    }

    public ProductsTypeDTO createProductType(ProductsTypeDTO productsTypeDTO, HttpServletRequest request) {
        SubCategory subCategory = subcategoryRepository.findById(productsTypeDTO.getSubCategoryId())
                .orElseThrow(() -> new RuntimeException("Subcategory not found"));
        if (Objects.nonNull(subCategory)) {
            ProductsType productType = new ProductsType();
            productType.setProductName(productsTypeDTO.getProductName());
            productType.setProductType(productsTypeDTO.getProductType());
            productType.setProductLogo(productsTypeDTO.getProductLogo());
            productType.setOffer(productsTypeDTO.getOffer());
            productType.setProductPrice(productsTypeDTO.getProductPrice());
            // List<String> imagePaths = saveOrGetImages(productsTypeDTO.getImages(), productsTypeDTO.getProductName(), true);
            List<String> imagePaths = uploadImages(productsTypeDTO.getImagesFiles(), request, productsTypeDTO.getProductType(), productsTypeDTO.getProductName(), productsTypeDTO.getSubCategoryId().intValue());
            productType.setImagePaths(imagePaths);
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
        return imagePaths.stream().filter(imagePath -> imagePath.contains(productName)).toList();
    }


    private String getFileExtension(String filename) {
        return filename != null && filename.contains(".")
                ? filename.substring(filename.lastIndexOf("."))
                : "";
    }

    public List<String> uploadImages(MultipartFile[] files, HttpServletRequest request,
                                     String productType, String productName, int subCategoryID) {
        String baseUrl = ImageController.getBaseUrl(request);

        return Arrays.stream(files)
                .map(file -> {
                    try {
                        // Sanitize folder/file-friendly names
                        String safeProductType = productType.replaceAll("[^a-zA-Z0-9]", "_");
                        String safeProductName = productName.replaceAll("[^a-zA-Z0-9]", "_");
                        String fileExtension = getFileExtension(file.getOriginalFilename());
                        String shortId = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
                        String fileName = subCategoryID + "_" + safeProductType + "_" + safeProductName + "_" + shortId + fileExtension;

                        // LOCAL_DEV ---> "C:\\Users\\arpan\\uploads\\"
                        // Path folderPath = Path.of("C:\\Users\\arpan\\uploads\\", String.valueOf(subCategoryID), safeProductName);

                        //PROD_ENV--->
                        Path folderPath = Path.of(IMAGE_UPLOAD_DIR, String.valueOf(subCategoryID), safeProductName);
                        Files.createDirectories(folderPath); // Will create parent + child folders as needed

                        // Save file
                        Path filePath = folderPath.resolve(fileName);
                        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                        // File URL (keep subfolders in path)
                        String fileUrl = baseUrl + "/images/files/" + subCategoryID + "/" + safeProductName + "/" + fileName;
                        log.info("File URL ==> {} ", fileUrl);
                        String buildFileName = subCategoryID + "/" + safeProductName + "/" + fileName;
                        String buildFileUrl = ImageController.buildFileUrl(request, buildFileName);
                        log.info("Build File Url ==> {} ", buildFileUrl);

                        return buildFileUrl;
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload: " + file.getOriginalFilename(), e);
                    }
                })
                .toList();
    }


    public List<GroupedImageMetadataDTO> getImagesBySubCategoryID(int subCategoryID, HttpServletRequest request
    ) {

        String baseUrl = ImageController.getBaseUrl(request);
        String categoryIdStr = String.valueOf(subCategoryID);
        //PROD_ENV--->
        File categoryFolder = new File(IMAGE_UPLOAD_DIR, categoryIdStr);
        //LOCAL_DEV  --->
        // File categoryFolder = new File("C:\\Users\\arpan\\uploads\\", categoryIdStr);

        if (!categoryFolder.exists() || !categoryFolder.isDirectory()) {
            return Collections.emptyList();
        }

        File[] productFolders = Optional.ofNullable(categoryFolder.listFiles(File::isDirectory))
                .orElse(new File[0]);

        // Map to hold grouped results
        Map<String, List<String>> groupedFiles = new HashMap<>();

        for (File productFolder : productFolders) {
            String productName = productFolder.getName();

            File[] imageFiles = Optional.ofNullable(productFolder.listFiles(File::isFile))
                    .orElse(new File[0]);

            for (File file : imageFiles) {
                String fileName = file.getName();
                String productType = parseProductType(fileName);
                if (productType == null) continue;

                String groupKey = productName + "|" + productType;

                String fileUrl = baseUrl + "/images/files/" + subCategoryID + "/" + productName + "/" + fileName;
                groupedFiles.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(fileUrl);
            }
        }

        // Convert map to list of GroupedImageMetadata
        List<GroupedImageMetadataDTO> result = groupedFiles.entrySet().stream()
                .map(entry -> {
                    String[] keys = entry.getKey().split("\\|");
                    return new GroupedImageMetadataDTO(
                            entry.getValue(),
                            keys[1],         // productType
                            keys[0],         // productName
                            subCategoryID
                    );
                })
                .collect(Collectors.toList());

        return result;
    }

    private String parseProductType(String fileName) {
        String[] parts = fileName.split("_");
        return parts.length >= 3 ? parts[1] : null;
    }
}
