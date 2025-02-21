package org.abx.persistence;

import org.abx.jwt.JWTUtils;
import org.abx.persistence.spring.ABXPersistenceEntry;
import org.abx.services.ServiceRequest;
import org.abx.services.ServiceResponse;
import org.abx.services.ServicesClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootTest(classes = ABXPersistenceEntry.class)
public class AddGetDashboardTest {

    @Value("${jwt.private}")
    private String privateKey;


    private static ConfigurableApplicationContext context;
    @Autowired
    ServicesClient servicesClient;

    @BeforeAll
    public static void setup() {
        context = SpringApplication.run(ABXPersistenceEntry.class);
    }

    @Test
    public void doBasicTest() throws Exception {
        String username = "root";
        String token = JWTUtils.generateToken(username, privateKey, 60,
                List.of("Persistence"));

        ServiceRequest req = servicesClient.delete("persistence", "/persistence/dashboards");
        req.jwt(token);
        ServiceResponse resp = servicesClient.process(req);
        Assertions.assertEquals(true, resp.asString());



    }

    @AfterAll
    public static void teardown() {
        context.stop();
    }

}
