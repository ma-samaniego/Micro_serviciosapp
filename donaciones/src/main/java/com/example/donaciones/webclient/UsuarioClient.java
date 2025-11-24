package com.example.donaciones.webClient;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UsuarioClient {
    private final WebClient webClient;

    public UsuarioClient(@Value("${usuario-service.url}") String usuarioUrl){
        this.webClient = WebClient.builder().baseUrl(usuarioUrl).build();
    }

    public Map<String, Object> getUsuarioById(Long id) {
        return this.webClient.get()
                .uri("/{usuario_id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                              .flatMap(body -> Mono.error(new RuntimeException("Usuario inválido"))))
                .bodyToMono(Map.class)
                .block();
    }
}