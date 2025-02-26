package org.abx.persistence;

import org.abx.jwt.JWTUtils;
import org.abx.persistence.spring.ABXPersistenceEntry;
import org.abx.services.JWTServicesClient;
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
public class DashboardCRUDTest {

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
        String dashboardName = "My Dashboard";
        String token = JWTUtils.generateToken(username, privateKey, 60,
                List.of("Persistence"));
        JWTServicesClient jwtServicesClient = servicesClient.withJWT(token);

        ServiceRequest req = jwtServicesClient.delete("persistence", "/persistence/dashboards");
        ServiceResponse resp = servicesClient.process(req);
        Assertions.assertEquals(true, resp.asBoolean());


        req = jwtServicesClient.post("persistence", "/persistence/dashboards").
                addPart("dashboardName",dashboardName);
        resp = servicesClient.process(req);
        long id = resp.asLong();


        req = jwtServicesClient.get("persistence", "/persistence/dashboards");
        resp = servicesClient.process(req);
        Assertions.assertEquals(1, resp.asJSONArray().length());


        req = jwtServicesClient.get("persistence", "/persistence/dashboards/"+id);
        resp = servicesClient.process(req);
        Assertions.assertEquals(dashboardName, resp.asJSONObject().get("dashboardName"));


        req = jwtServicesClient.delete("persistence", "/persistence/dashboards/"+id);
        resp = servicesClient.process(req);
        Assertions.assertEquals(true, resp.asBoolean());


        req = jwtServicesClient.get("persistence", "/persistence/dashboards");
        resp = servicesClient.process(req);
        Assertions.assertEquals(0, resp.asJSONArray().length());


        req = jwtServicesClient.post("persistence", "/persistence/dashboards");
        req.addPart("dashboardName",dashboardName);
        resp = servicesClient.process(req);


        req = jwtServicesClient.get("persistence", "/persistence/dashboards");
        resp = servicesClient.process(req);
        Assertions.assertEquals(1, resp.asJSONArray().length());


        req = jwtServicesClient.delete("persistence", "/persistence/dashboards");
        resp = servicesClient.process(req);
        Assertions.assertEquals(true, resp.asBoolean());


        req = jwtServicesClient.get("persistence", "/persistence/dashboards");
        resp = servicesClient.process(req);
        Assertions.assertEquals(0, resp.asJSONArray().length());

    }

    @AfterAll
    public static void teardown() {
        context.stop();
    }

}
