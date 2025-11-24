package com.example.comentarios.webClient;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PublicacionClient { // <-- Nombre Corregido

    private final WebClient webClient;

    public PublicacionClient(@Value("${publicaciones-service.url}") String publicacionesServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(publicacionesServiceUrl).build();
    }

    public Map<String, Object> getPublicacionById(Long id) {
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Publicación no encontrada"))))
                .bodyToMono(Map.class)
                .block();
    }
}