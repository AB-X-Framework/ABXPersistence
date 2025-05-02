package org.abx.persistence;

import org.abx.jwt.JWTUtils;
import org.abx.persistence.client.dao.ProjectRepoRepository;
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
class ProjectCRUDTest {

    @Value("${jwt.private}")
    private String privateKey;


    @Autowired
    private ProjectRepoRepository projectRepoRepository;

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

        ServiceRequest req = servicesClient.get("persistence", "/persistence/user");
        req.jwt(token);
        ServiceResponse resp = servicesClient.process(req);
        Assertions.assertEquals(username, resp.asString());

        int projectId = servicesClient.process(
                servicesClient.get("persistence", "/persistence/projects").jwt(token)
        ).asJSONArray().getJSONObject(0).getInt("projectId");

        String repoName = "myRepo";
        String branch = "main";
        String url = "git@github.com:AB-X-Framework/ABXPersistence.git";
        String creds = "{\"password\":\"123\"}";
        req = servicesClient.post("persistence", "/persistence/projects/" + projectId + "/repos");
        req.addPart("repoName", repoName);
        req.addPart("branch", branch);
        req.addPart("engine", "local");
        req.addPart("url", url);
        req.addPart("creds", creds);
        req.jwt(token);
        resp = servicesClient.process(req);
        Assertions.assertFalse(resp.asJSONObject().getBoolean("error"));

        req = servicesClient.get("persistence", "/persistence/projects/" + projectId + "/repos");
        req.jwt(token);
        resp = servicesClient.process(req);
        JSONArray repos = resp.asJSONObject().getJSONArray("repos");
        Assertions.assertEquals(1, repos.length());

        Assertions.assertEquals(repoName, repos.getJSONObject(0).getString("repoName"));
        Assertions.assertEquals(branch, repos.getJSONObject(0).getString("branch"));
        Assertions.assertEquals(url, repos.getJSONObject(0).getString("url"));
        Assertions.assertEquals(creds, repos.getJSONObject(0).getString("creds"));

        branch = "newBranch";
        req = servicesClient.post("persistence", "/persistence/projects/" + projectId + "/repos");
        req.addPart("repoName", repoName);
        req.addPart("branch", branch);
        req.addPart("engine", "git");
        req.addPart("url", url);
        req.addPart("creds", creds);
        req.jwt(token);
        resp = servicesClient.process(req);
        Assertions.assertTrue(resp.asBoolean());

        req = servicesClient.get("persistence", "/persistence/projects/" + projectId + "/repos");
        req.jwt(token);
        resp = servicesClient.process(req);
        repos = resp.asJSONObject().getJSONArray("repos");
        Assertions.assertEquals(1, repos.length());

        long repoId = repos.getJSONObject(0).getLong("id");
        Assertions.assertEquals(repoName, repos.getJSONObject(0).getString("repoName"));
        Assertions.assertEquals(branch, repos.getJSONObject(0).getString("branch"));
        Assertions.assertEquals(url, repos.getJSONObject(0).getString("url"));
        Assertions.assertEquals(creds, repos.getJSONObject(0).getString("creds"));


        req = servicesClient.delete("persistence", "/persistence/projects/" + projectId + "/repos/"+repoName);
        req.jwt(token);
        resp = servicesClient.process(req);
        boolean status = resp.asBoolean();
        Assertions.assertTrue(status);
        Assertions.assertNull(projectRepoRepository.findByProjectRepoId(repoId));

        req = servicesClient.get("persistence", "/persistence/projects/" + projectId + "/repos");
        req.jwt(token);
        resp = servicesClient.process(req);
        repos = resp.asJSONObject().getJSONArray("repos");
        Assertions.assertEquals(0, repos.length());


    }

    @AfterAll
    public static void teardown() {
        context.stop();
    }

}
