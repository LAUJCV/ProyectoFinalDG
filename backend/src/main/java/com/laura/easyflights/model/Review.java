package com.laura.easyflights.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;
    private String comment;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore 
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Review (){
        this.date = LocalDate.now();
    }

    public Review(int rating, String comment, Product product, User user){
        this.rating = rating;
        this.comment = comment;
        this.date = LocalDate.now();
        this.product = product;
        this.user = user;
    }

    public Long getId(){
        return id;
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public Product getProduct(){
        return product;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    
}
