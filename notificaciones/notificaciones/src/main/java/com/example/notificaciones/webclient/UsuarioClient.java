package com.example.notificaciones.webclient;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

@Component
public class UsuarioClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public UsuarioClient(@Value("${usuario-service.url}") String usuarioServiceUrl){
        this.restTemplate = new RestTemplate();
        this.baseUrl = usuarioServiceUrl;
    }

    public Map<String, Object> getUsuarioById(Long id) {
        ResponseEntity<Map<String, Object>> response = this.restTemplate.exchange(
            this.baseUrl + "/{usuario_id}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {},
            id
        );
        return response.getBody();
    }

}