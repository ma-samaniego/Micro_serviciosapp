package com.example.notificaciones;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.notificaciones.repository.NotificationRepository;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
class NotificacionesApplicationTests {

	@MockBean
	private NotificationRepository notificationRepository;

	@Test
	void contextLoads() {
	}

}
