package com.laura.easyflights.model;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @ManyToOne
    @JsonIgnoreProperties("products") // Evita serializar los productos dentro de la categoría
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JsonIgnoreProperties("products")
    @JoinTable(
        name = "product_feature",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private List<Feature> features;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("product")
    private List<ReservationDate> lstReservationDate = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();




    // Constructores (uno vacío para que JPA cree objetos de manera automática)
    public Product() {
    }

    public Product(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Setters y Getters

    public List<ReservationDate> getReservationDates(){
        return lstReservationDate;
    }

    public void setReservationDate(List<ReservationDate> lstReservationDate){
        this.lstReservationDate = lstReservationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public Category getCategory(){
        return category;
    }

    public void setCategory(Category category){
        this.category = category;
    }
    
    public List<Feature> getFeatures(){
        return features;
    }

    public void setFeatures(List<Feature> features){
        this.features = features;
    }

    public List<Review> getReviews() {
        return reviews;
    }

}
