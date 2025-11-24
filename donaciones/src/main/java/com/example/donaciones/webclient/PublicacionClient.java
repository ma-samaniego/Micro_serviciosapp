package com.example.donaciones.webClient;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PublicacionClient {
    private final WebClient webClient;

    public PublicacionClient(@Value("${publicaciones-service.url}") String publiUrl) {
        this.webClient = WebClient.builder().baseUrl(publiUrl).build();
    }

    public Map<String, Object> getPublicacionById(Long id) {
        return this.webClient.get()
                .uri("/{id}", id) // Asume que el endpoint en demo es /api/publicaciones/{id}
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Publicación no encontrada"))))
                .bodyToMono(Map.class)
                .block();
    }
}