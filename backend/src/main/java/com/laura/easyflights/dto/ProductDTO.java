package com.laura.easyflights.dto;

import java.util.List;

import com.laura.easyflights.model.ProductImage;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private double averageRating;
    private List<ProductImage> images;

    public ProductDTO(){};

    public ProductDTO(Long id, String name, String description, double price, List<ProductImage> images, double averageRating){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.images = images;
        this.averageRating = averageRating;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    
}


