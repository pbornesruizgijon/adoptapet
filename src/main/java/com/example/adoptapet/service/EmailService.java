package com.example.adoptapet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio especializado en la gestión y envío de notificaciones por correo electrónico.
 * Utiliza la infraestructura de Spring Mail para comunicarse con un servidor SMTP 
 * y notificar a los adoptantes sobre el éxito de sus operaciones.
 *
 */
@Service
public class EmailService {

    /** * Componente central de Spring para el envío de correos. 
     * Se configura externamente mediante las propiedades del servidor de correo (Host, Port, User, Pass).
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Construye y envía un correo electrónico de confirmación tras una adopción exitosa.
     * El mensaje incluye información dinámica como el nombre de la mascota, un enlace 
     * a su fotografía en Cloudinary y un identificador de seguimiento único basado en timestamp.
     *
     *
     * @param destinatario Dirección de correo del usuario (recuperada del perfil local o GitHub).
     * @param nombreMascota Nombre del animal que ha sido adoptado.
     * @param urlImagen Enlace directo a la imagen de la mascota almacenada en la nube.
     */
    public void enviarCorreoAdopcion(String destinatario, String nombreMascota, String urlImagen) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("CONFIRMACIÓN DE ADOPCIÓN - " + nombreMascota.toUpperCase());
        
        // Lógica de seguridad para el manejo de enlaces de imagen
        String fotoLink = (urlImagen != null && !urlImagen.isBlank()) ? urlImagen : "Imagen no disponible";
        
        // Construcción del cuerpo del mensaje con formato informativo
        String cuerpo = "ESTIMADO/A USUARIO/A,\n\n"
                + "Nos complace informarle que la adopción de " + nombreMascota + " ha sido procesada.\n\n"
                + "FOTO DE TU NUEVA MASCOTA:\n"
                + fotoLink + "\n\n"
                + "DETALLES DEL REGISTRO:\n"
                + "-------------------------------------------\n"
                + "Estado: Procesado con éxito\n"
                + "ID de Seguimiento: #" + System.currentTimeMillis() / 1000 + "\n"
                + "-------------------------------------------\n\n"
                + "Gracias por confiar en AdoptaPet.\n\n"
                + "Atentamente,\n"
                + "Equipo de Gestión - AdoptaPet";

        message.setText(cuerpo);
        
        /**
         * Ejecución del envío mediante el protocolo SMTP.
         * Esta operación es bloqueante; en entornos de alta carga se recomendaría @Async.
         */
        mailSender.send(message);
    }
}