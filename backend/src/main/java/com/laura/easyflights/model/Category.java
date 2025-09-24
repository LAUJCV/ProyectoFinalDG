package com.laura.easyflights.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties({"category", "features", "lstReservationDate"})
    private List<Product> products;

    @OneToOne(mappedBy = "category", cascade = CascadeType.ALL)
    private CategoryImage image;

    // Constructor vacío
    public Category() {
    }

    public Category(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public CategoryImage getImage(){
        return image;
    }

    public void setImage(CategoryImage image){
        this.image = image;
        if(image != null) {
            image.setCategory(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts(){
        return products;
    }

    public void setProducts(List<Product> products){
        this.products = products;
    }

}
