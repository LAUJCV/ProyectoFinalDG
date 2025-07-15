package com.laura.easyflights.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.laura.easyflights.service.ProductService;
import com.laura.easyflights.model.Product;
import com.laura.easyflights.model.Category;
import com.laura.easyflights.model.Feature;
import com.laura.easyflights.model.ProductImage;
import com.laura.easyflights.repository.CategoryRepository;
import com.laura.easyflights.repository.FeatureRepository;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductService service;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FeatureRepository featureRepository;

    public ProductController(ProductService service) {
        this.service = service;
    }


    // Método REST para obtener todos los productos
    @GetMapping
    public List<Product> getAllProducts() {
        List<Product> allProducts = service.getAllProducts();
        Collections.shuffle(allProducts);
        return allProducts;
    }

    // Método para obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = service.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Método POST para agregar un producto
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<Product> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("price") Integer price,
            @RequestParam("features") List<Long> featuresId,
            @RequestParam("images") MultipartFile[] images) {

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        List<Feature> features = featureRepository.findAllById(featuresId);
        product.setFeatures(features);

        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()){
            product.setCategory(category.get());
        } else {
            return ResponseEntity.badRequest().build();
        }

;
        List<ProductImage> imageList = new ArrayList<>();

        String assetsDir = "/Users/lauracamargo/Documents/ProyectoDG/backend/uploads";
        File dir = new File(assetsDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (MultipartFile image : images) {
            String fileName = image.getOriginalFilename();
            String path = assetsDir + "/" + fileName;
            try {
                image.transferTo(new File(path));

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            ProductImage img = new ProductImage();
            img.setUrl("/uploads/" + fileName);
            img.setProduct(product);
            imageList.add(img);
        }

        product.setImages(imageList);

        Product saved = service.addProduct(product);

        return ResponseEntity.ok(saved);
    }

    // Método REST para eliminar un producto
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
    }

}
