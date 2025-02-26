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
public class SimCRUDTest {

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
                List.of("Persistence"));

        int projectId = servicesClient.process(
                servicesClient.get("persistence", "/persistence/projects").jwt(token)
        ).asJSONArray().getJSONObject(0).getInt("projectId");

        ServiceRequest req = servicesClient.get("persistence", "/persistence/projects/"+projectId+"/sims");
        req.jwt(token);
        servicesClient.process(req);


        req = servicesClient.post("persistence", "/persistence/projects/"+projectId+"/sim");
        req.jwt(token);
        String simName = "basic";
        String folder = "src/folder/";
        String path = "script.js";
        String type = "test";
        req.addPart("simName", simName);
        req.addPart("folder", folder);
        req.addPart("path", path);
        req.addPart("type", type);
        ServiceResponse resp = servicesClient.process(req);
        long id = resp.asLong();

        req = servicesClient.get("persistence", "/persistence/projects/"+projectId+"/sims");
        req.jwt(token);
        resp = servicesClient.process(req);
        JSONArray sims = resp.asJSONArray();

        Assertions.assertEquals(1, sims.length());


        Assertions.assertEquals(folder, sims.getJSONObject(0).getString("folder"));
        Assertions.assertEquals(path, sims.getJSONObject(0).getString("path"));
        Assertions.assertEquals(simName, sims.getJSONObject(0).getString("name"));
        Assertions.assertEquals(type, sims.getJSONObject(0).getString("type"));
        Assertions.assertEquals(id, sims.getJSONObject(0).getLong("id"));

        simName = simName+"new";
        req = servicesClient.patch("persistence", "/persistence/projects/"+projectId+"/sim/"+id);
        req.jwt(token);
        req.addPart("simName", simName);
        req.addPart("folder", folder);
        req.addPart("path", path);
        req.addPart("type", type);
        resp = servicesClient.process(req);
        Assertions.assertTrue(resp.asBoolean());


        req = servicesClient.get("persistence", "/persistence/projects/"+projectId+"/sims");
        req.jwt(token);
        resp = servicesClient.process(req);
         sims = resp.asJSONArray();

        Assertions.assertEquals(1, sims.length());

        Assertions.assertEquals(folder, sims.getJSONObject(0).getString("folder"));
        Assertions.assertEquals(path, sims.getJSONObject(0).getString("path"));
        Assertions.assertEquals(simName, sims.getJSONObject(0).getString("name"));
        Assertions.assertEquals(type, sims.getJSONObject(0).getString("type"));
        Assertions.assertEquals(id, sims.getJSONObject(0).getLong("id"));

        req = servicesClient.delete("persistence", "/persistence/projects/"+projectId+"/sim/"+id);
        req.jwt(token);
        resp = servicesClient.process(req);
        String data = resp.asString();
        Assertions.assertTrue(resp.asBoolean());


        req = servicesClient.get("persistence", "/persistence/projects/"+projectId+"/sims");
        req.jwt(token);
        resp = servicesClient.process(req);
        Assertions.assertEquals(0,resp.asJSONArray().length());



    }

    @AfterAll
    public static void teardown() {
        context.stop();
    }
}
