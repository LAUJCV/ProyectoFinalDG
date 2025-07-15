package com.laura.easyflights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;

@Service
public class EmailServer {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String toEmail, String toUsername) {

        String loginUrl = "http://localhost:3000/login";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("¡Bienvenido a EasyFlight App!");
        message.setText("Hola " + toUsername +
                " ,\n\n Tu registro ha sido exitoso.\n\n Nombre de usuario: "
                + toUsername + "\n Email: " + toEmail +
                "\n\n Puedes iniciar sesión aquí: " + loginUrl +
                "\n\n Gracias por unirte a EasyFlight.\n\n ✈️");

        message.setFrom("easyfligth@gmail.com");

        mailSender.send(message);
    }
}
