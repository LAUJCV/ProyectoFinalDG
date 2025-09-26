package com.laura.easyflights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;

@Service
public class EmailServer {

    @Autowired
    private JavaMailSender mailSender;

    // --- correo de bienvenida ---
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

    // --- correo de confirmación de reserva ---
    public void sendReservationEmail(
            String toEmail,
            String toName,
            String productName,
            String startDate,
            String endDate,
            String status) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("Confirmación de tu reserva en EasyFlights ✈️");

        String html = """
                    <div style="font-family: Arial, sans-serif; color: #333; max-width:600px; margin:auto;">
                        <h2 style="color:#187b7b;">¡Reserva confirmada!</h2>
                        <p>Hola <strong>%s</strong>,</p>
                        <p>Tu reserva ha sido creada con éxito en <strong>EasyFlights</strong>.</p>

                        <table style="width:100%%; border-collapse:collapse; margin:20px 0;">
                            <tr>
                                <td style="padding:8px; border:1px solid #ddd;"><b>Producto</b></td>
                                <td style="padding:8px; border:1px solid #ddd;">%s</td>
                            </tr>
                            <tr>
                                <td style="padding:8px; border:1px solid #ddd;"><b>Fecha inicio</b></td>
                                <td style="padding:8px; border:1px solid #ddd;">%s</td>
                            </tr>
                            <tr>
                                <td style="padding:8px; border:1px solid #ddd;"><b>Fecha fin</b></td>
                                <td style="padding:8px; border:1px solid #ddd;">%s</td>
                            </tr>
                            <tr>
                                <td style="padding:8px; border:1px solid #ddd;"><b>Estado</b></td>
                                <td style="padding:8px; border:1px solid #ddd;">%s</td>
                            </tr>
                        </table>

                        <h3 style="color:#187b7b;">📞 Información de contacto del proveedor</h3>
                        <p>
                            EasyFlights Colombia<br/>
                            Dirección: Calle 123 #45-67, Bogotá, Colombia<br/>
                            Teléfono: +57 1 234 5678<br/>
                            Correo: soporte@easyflights.com
                        </p>

                        <hr style="margin:20px 0;"/>
                        <p style="font-size:12px; color:#888; text-align:center;">
                            EasyFlights - Tu viaje empieza aquí ✈️
                        </p>
                    </div>
                """.formatted(toName, productName, startDate, endDate, status);

        helper.setText(html, true); // true = HTML
        helper.setFrom("easyflight@gmail.com");

        mailSender.send(message);
    }
}