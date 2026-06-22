package com.mariluz.carrito;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "jwt.secret=98PAzqkLUAElxtt+iNCg3GxPvqHI8ytzNfilW2S3RhQ=",
        "eureka.client.enabled=false",
        "eureka.client.register-with-eureka=false",
    }
)
class CarritoApplicationTests {

    @Test
    void contextLoads() {}
}
