package com.example.comentarios.webClient;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class UsuarioClient {

    private static final Logger log = LoggerFactory.getLogger(UsuarioClient.class);

    private final WebClient webClient;

    public UsuarioClient(@Value("${usuario-service.url}") String usuarioServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(usuarioServiceUrl).build();
    }

    /**
     * Obtiene datos del usuario por ID. Devuelve null si la respuesta es 404.
     * Lanza RuntimeException con detalle en otros casos de error.
     */
    public Map<String, Object> getUsuarioById(Long id) {
        try {
            return this.webClient.get()
                    .uri("/{usuario_id}", id)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(),
                            response -> response.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("Usuario service returned 4xx: " + body)))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (WebClientResponseException.NotFound nf) {
            log.info("Usuario no encontrado (404): {}", id);
            return null;
        } catch (WebClientResponseException we) {
            String resp = we.getResponseBodyAsString();
            log.error("Error from usuario service: status={} body={}", we.getRawStatusCode(), resp);
            throw new RuntimeException("Usuario service error: " + we.getRawStatusCode() + " - " + resp, we);
        } catch (Exception e) {
            log.error("Error calling usuario service: {}", e.getMessage());
            throw new RuntimeException("Error al llamar servicio de usuarios: " + e.getMessage(), e);
        }
    }

}