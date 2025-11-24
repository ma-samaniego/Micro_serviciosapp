package com.example.comentarios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.comentarios.repository.ComentarioRepository;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
class ComentariosApplicationTests {

	@MockBean
	private ComentarioRepository comentarioRepository;

	@Test
	void contextLoads() {
	}

}
