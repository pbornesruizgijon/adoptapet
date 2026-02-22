package com.example.adoptapet.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * Servicio encargado de la gestión de recursos multimedia mediante la API de Cloudinary.
 * Proporciona la lógica necesaria para procesar la subida de archivos binarios 
 * y obtener localizadores de recursos uniformes (URLs) seguros para su almacenamiento en base de datos.
 */
@Service
public class CloudinaryService {

    /** Bean configurado de Cloudinary para la interacción con el servicio en la nube. */
    @Autowired
    private Cloudinary cloudinary;

    /**
     * Procesa la subida de un archivo de imagen al servidor de Cloudinary.
     * Convierte el archivo multipart recibido en la petición HTTP a un flujo de bytes,
     * lo sube utilizando un preset preconfigurado y recupera la URL segura.
     *
     * @param file El archivo binario de la mascota subido mediante el formulario.
     * @return Una cadena con la URL segura (HTTPS) del archivo alojado, o null si el archivo está vacío o falla la subida.
     * @throws IOException Si ocurre un error en la lectura de los bytes del archivo original.
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // Validación de integridad del archivo de entrada
        if (file.isEmpty()) {
            return null;
        }

        try {
            /** * Configuración de parámetros de subida:
             * - upload_preset: Define el comportamiento predeterminado en Cloudinary.
             * - resource_type: "auto" permite detectar automáticamente si es imagen o vídeo.
             */
            Map params = ObjectUtils.asMap(
                    "upload_preset", "ml_default",
                    "resource_type", "auto"
            );

            // Ejecución de la subida al cloud mediante el SDK oficial
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

            /**
             * Retorno de 'secure_url'.
             * Se prioriza HTTPS para garantizar la compatibilidad con navegadores modernos
             * y cumplir con los estándares de seguridad web.
             */
            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            // Registro de errores técnicos en el flujo de subida
            System.err.println("ERROR CLOUDINARY: " + e.getMessage());
            return null;
        }
    }
}