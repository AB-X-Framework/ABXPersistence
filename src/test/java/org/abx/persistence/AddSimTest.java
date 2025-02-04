package org.abx.persistence;

import org.abx.jwt.JWTUtils;
import org.abx.persistence.spring.ABXPersistenceEntry;
import org.abx.services.ServiceRequest;
import org.abx.services.ServiceResponse;
import org.abx.services.ServicesClient;
import org.json.JSONArray;
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
public class AddSimTest {

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
        String username = "SimUser";
        String token = JWTUtils.generateToken(username, privateKey, 60,
                List.of("persistence"));


        ServiceRequest req = servicesClient.get("persistence", "/persistence/dropSims");
        req.jwt(token);
        servicesClient.process(req);


        req = servicesClient.post("persistence", "/persistence/addSim");
        req.jwt(token);
        String simName = "basic";
        String folder = "src/folder/";
        String path = "script.js";
        String type = "test";
        req.addPart("name", simName);
        req.addPart("folder", folder);
        req.addPart("path", path);
        req.addPart("type", type);
        ServiceResponse resp = servicesClient.process(req);
        long id = resp.asLong();

        req = servicesClient.get("persistence", "/persistence/sims");
        req.jwt(token);
        resp = servicesClient.process(req);
        String data = resp.asString();
        JSONArray sims = new JSONArray(data);

        Assertions.assertEquals(1, sims.length());


        Assertions.assertEquals(folder, sims.getJSONObject(0).getString("folder"));
        Assertions.assertEquals(path, sims.getJSONObject(0).getString("path"));
        Assertions.assertEquals(simName, sims.getJSONObject(0).getString("name"));
        Assertions.assertEquals(type, sims.getJSONObject(0).getString("type"));
        Assertions.assertEquals(id, sims.getJSONObject(0).getLong("id"));
    }

    @AfterAll
    public static void teardown() {
        context.stop();
    }
}
