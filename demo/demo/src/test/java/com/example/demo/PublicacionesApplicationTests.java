package com.example.demo;

import com.example.demo.repository.PublicationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
class PublicacionesApplicationTests {

    @MockBean
    private PublicationRepository publicationRepository;

    @Test
    void contextLoads() {
    }

}
