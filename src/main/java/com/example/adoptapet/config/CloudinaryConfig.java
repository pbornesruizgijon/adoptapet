package com.example.adoptapet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * Clase de configuración para la integración con el servicio Cloudinary.
 * Esta clase se encarga de instanciar y configurar el bean principal de Cloudinary
 * utilizando las credenciales externas definidas en el archivo de propiedades.
 *
 */
@Configuration
public class CloudinaryConfig {

    /** Nombre del "cloud" asignado en la consola de Cloudinary. */
    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    /** Clave de API para la autenticación en el servicio. */
    @Value("${cloudinary.api_key}")
    private String apiKey;

    /** Secreto de API para la firma de peticiones seguras. */
    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    /**
     * Define y configura el Bean de Cloudinary para ser utilizado en toda la aplicación.
     * Configura el cliente para usar protocolos seguros (HTTPS) en la generación de URLs.
     *
     * * @return Una instancia configurada del objeto {@link Cloudinary}.
     */
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true // Generar URLs seguras (HTTPS)
        ));
    }
}