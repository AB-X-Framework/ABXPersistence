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
class ABXPersistenceTest {

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
                List.of("persistence"));

        ServiceRequest req = servicesClient.get("persistence", "/persistence/user");
        req.jwt(token);
        ServiceResponse resp = servicesClient.process(req);
        Assertions.assertEquals(username, resp.asString());

        String repoName = "myRepo";
        String branch = "main";
        String url = "git@github.com:AB-X-Framework/ABXPersistence.git";
        String creds = "{\"password\":\"123\"}";
        req = servicesClient.post("persistence", "/persistence/newRepo");
        req.addPart("name", repoName);
        req.addPart("branch", branch);
        req.addPart("url", url);
        req.addPart("creds", creds);
        req.jwt(token);
        resp = servicesClient.process(req);
        Assertions.assertEquals(repoName, resp.asString());

        req = servicesClient.get("persistence", "/persistence/repos");
        req.jwt(token);
        resp = servicesClient.process(req);
        JSONArray repos = resp.asJSONArray();
        Assertions.assertEquals(1, repos.length());

        Assertions.assertEquals(repoName, repos.getJSONObject(0).getString("name"));
        Assertions.assertEquals(branch, repos.getJSONObject(0).getString("branch"));
        Assertions.assertEquals(url, repos.getJSONObject(0).getString("url"));
        Assertions.assertEquals(creds, repos.getJSONObject(0).getString("creds"));

        branch = "newBranch";
        req = servicesClient.post("persistence", "/persistence/newRepo");
        req.addPart("name", repoName);
        req.addPart("branch", branch);
        req.addPart("url", url);
        req.addPart("creds", creds);
        req.jwt(token);
        servicesClient.process(req);

        req = servicesClient.get("persistence", "/persistence/repos");
        req.jwt(token);
        resp = servicesClient.process(req);
         repos = resp.asJSONArray();
        Assertions.assertEquals(1, repos.length());

        Assertions.assertEquals(repoName, repos.getJSONObject(0).getString("name"));
        Assertions.assertEquals(branch, repos.getJSONObject(0).getString("branch"));
        Assertions.assertEquals(url, repos.getJSONObject(0).getString("url"));
        Assertions.assertEquals(creds, repos.getJSONObject(0).getString("creds"));

    }

    @AfterAll
    public static void teardown() {
        context.stop();
    }

}
