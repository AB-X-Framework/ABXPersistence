package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.PersistenceDataLoader;
import org.abx.persistence.client.model.RepoDetails;
import org.abx.persistence.client.model.UserDetails;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persistence")
public class PersistenceController {

    @Autowired
    private PersistenceDataLoader dataLoader;

    @Secured("persistence")
    @RequestMapping(value = "/user")
    public String user(HttpServletRequest request) {

        String username = request.getUserPrincipal().getName();
        UserDetails details = dataLoader.createUserIfNotFound(username);

        return details.getName();
    }

    @Secured("persistence")
    @RequestMapping(value = "/newRepo")
    public String addRepo(HttpServletRequest request, @RequestParam String name,
                          @RequestParam String url,
                          @RequestParam String branch,
                          @RequestParam String creds) {
        String username = request.getUserPrincipal().getName();
        UserDetails userDetails = dataLoader.createUserIfNotFound(username);
        RepoDetails repoDetails = dataLoader.createRepoIfNotFound(userDetails, name, url, branch, creds);
        return repoDetails.getName();
    }

    @Secured("persistence")
    @RequestMapping(value = "/deleteRepo")
    public boolean deleteRepo(HttpServletRequest request, @RequestParam String name) {
        String username = request.getUserPrincipal().getName();
        UserDetails userDetails = dataLoader.createUserIfNotFound(username);
        return dataLoader.deleteRepo(userDetails, name);
    }

    @Secured("persistence")
    @RequestMapping(value = "/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String repos(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        JSONArray jsonRepos = new JSONArray();
        for (RepoDetails repoDetails : dataLoader.repoDetails(username)) {
            JSONObject jsonRepo = new JSONObject();
            jsonRepos.put(jsonRepo);
            jsonRepo.put("name", repoDetails.getName());
            jsonRepo.put("branch", repoDetails.getBranch());
            jsonRepo.put("url", repoDetails.getUrl());
            jsonRepo.put("creds", repoDetails.getCreds());
        }
        return jsonRepos.toString();
    }


    @Secured("persistence")
    @RequestMapping(value = "/addSim", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addSim(HttpServletRequest request, @RequestParam String name,
                         @RequestParam String folder,
                         @RequestParam String path,
                         @RequestParam String type) throws Exception {
        String username = request.getUserPrincipal().getName();
        UserDetails userDetails = dataLoader.createUserIfNotFound(username);
        throw new RuntimeException("NOt ready");
    }
}
