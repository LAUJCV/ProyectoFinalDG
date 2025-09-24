package com.laura.easyflights.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laura.easyflights.dto.LoginRequest;
import com.laura.easyflights.model.Product;
import com.laura.easyflights.model.User;
import com.laura.easyflights.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // front
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailServer emailServer;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.findAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        Optional<User> optionalUser = userService.findByEmail(loginRequest.getEmaill());

        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        User user = optionalUser.get();

        if(!user.getPassword().equals(loginRequest.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        //No enviamos la contraseña en la respuesta
        Map<String, Object> userData = new HashMap<>();

        userData.put("id", user.getId());
        userData.put("firstName", user.getFirstName());
        userData.put("lastName", user.getLastName());
        userData.put("email", user.getEmail());
        userData.put("isAdmin", user.getIsAdmin());


        return ResponseEntity.ok(userData);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }
        
        if(user.getEmail().equalsIgnoreCase("admin@gmail.com")){
            user.setIsAdmin(true);
        } else {
            user.setIsAdmin(false);
        }

        //Enviar correo de confirmación
        emailServer.sendConfirmationEmail(user.getEmail(), user.getFirstName());

        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/resend-confirmation")
    public ResponseEntity<?> resendConfirmation(@RequestBody Map<String, String> request){
        String email = request.get("email");

        Optional<User> optUser = userService.findByEmail(email);

        if(optUser.isPresent()){
            User user = optUser.get();
            emailServer.sendConfirmationEmail(user.getEmail(), user.getFirstName());

            return ResponseEntity.ok("Mensaje enviado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @GetMapping("/{id}/favorites")
    public List<Product> getFavorites(@PathVariable Long id){
        return userService.getFavorites(id);
    }

    @PostMapping("/{userId}/favorites/{productId}")
    public void addFavorites(@PathVariable Long userId, @PathVariable Long productId){
        userService.addFavorites(userId, productId);
    }

    @DeleteMapping("/{userId}/favorites/{productId}")
    public void removeFavorites(@PathVariable Long userId, @PathVariable Long productId){
        userService.removeFavorite(userId, productId);
    }
}
