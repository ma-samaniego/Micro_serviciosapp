package com.example.demo.webClient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NotificacionClient {
    private final WebClient webcliente;

    public NotificacionClient(@Value("${notificacion.service.url:http://localhost:8084}") String notificacionServiceUrl) {
        this.webcliente = WebClient.builder().baseUrl(notificacionServiceUrl).build();
    }

    /**
     * Envía una notificación al servicio de notificaciones y retorna la respuesta como Map<String,Object>.
     * Método bloqueante (sincrónico) para mantener el mismo estilo que `UsuarioClient`.
     */
    public Map<String, Object> sendNotification(Map<String, Object> payload) {
        return this.webcliente.post()
                .uri("/api/notificaciones")
                .bodyValue(payload)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Error en notificación: " + body)))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

}
