package com.example.adoptapet.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary; // Simular la conexión a la nube

    @Mock
    private Uploader uploader; // Simular el objeto que sube archivos

    @InjectMocks
    private CloudinaryService cloudinaryService; // El servicio que usa los mocks

    @Test
    void cuandoSubeArchivo_debeRetornarUrl() throws IOException {
        // GIVEN: Prepar los datos
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "contenido".getBytes());
        Map<String, String> resultadoSimulado = new HashMap<>();
        resultadoSimulado.put("secure_url", "https://res.cloudinary.com/foto-test.jpg");

        // Definir el comportamiento del Mock:
        // Cuando llamemos a cloudinary.uploader().upload(...), que devuelva el mapa simulado
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(), any())).thenReturn(resultadoSimulado);

        // WHEN: Ejecutar el método que queremos probar
        String url = cloudinaryService.uploadFile(file);

        // THEN: Verifir que el resultado es el esperado
        assertEquals("https://res.cloudinary.com/foto-test.jpg", url);
        verify(uploader, times(1)).upload(any(), any()); // Confirmamos que se llamó a la API una vez
    }
}