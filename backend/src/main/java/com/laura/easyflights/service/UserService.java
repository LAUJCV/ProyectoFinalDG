package com.laura.easyflights.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laura.easyflights.model.Product;
import com.laura.easyflights.model.User;
import com.laura.easyflights.repository.ProductRepository;
import com.laura.easyflights.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    //Método para registrar un usuario
    public User registerUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public List<Product> getFavorites(Long userId){
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return user.getFavorites();
    }

    public void addFavorites(Long userId, Long productId){
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if(!user.getFavorites().contains(product)){
            user.getFavorites().add(product);
            userRepository.save(user);
        }
    }

    public void removeFavorite(Long userId, Long productId){
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Product product = productRepository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        user.getFavorites().remove(product);
        userRepository.save(user);

    }
}