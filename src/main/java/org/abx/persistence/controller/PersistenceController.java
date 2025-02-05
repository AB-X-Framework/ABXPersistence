package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.PersistenceDataLoader;
import org.abx.persistence.client.model.RepoDetails;
import org.abx.persistence.client.model.SimSpecs;
import org.abx.persistence.client.model.UserDetails;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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
        RepoDetails repoDetails = dataLoader.createRepoIfNotFound(username, name, url, branch, creds);
        return repoDetails.getName();
    }

    @Secured("persistence")
    @RequestMapping(value = "/deleteRepo")
    public boolean deleteRepo(HttpServletRequest request, @RequestParam String name) {
        String username = request.getUserPrincipal().getName();
        return dataLoader.deleteRepo(username, name);
    }

    @Secured("persistence")
    @RequestMapping(value = "/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String repos(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        JSONArray jsonRepos = new JSONArray();
        UserDetails userDetails = dataLoader.createUserIfNotFound(username);
        for (RepoDetails repoDetails : userDetails.getRepoDetails()) {
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
    public long addSim(HttpServletRequest request, @RequestParam String name,
                       @RequestParam String folder,
                       @RequestParam String path,
                       @RequestParam String type) throws Exception {
        String username = request.getUserPrincipal().getName();
        return dataLoader.createSimSpecs(username, name, folder, path, type).getSimId();
    }

    @Secured("persistence")
    @RequestMapping(value = "/dropSims", produces = MediaType.APPLICATION_JSON_VALUE)
    public int dropSims(HttpServletRequest request) throws Exception {
        String username = request.getUserPrincipal().getName();
        return dataLoader.dropSims(username);
    }

    @Secured("persistence")
    @RequestMapping(value = "/sims", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sims(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        JSONArray jsonRepos = new JSONArray();
        UserDetails userDetails = dataLoader.createUserIfNotFound(username);
        for (SimSpecs repoDetails : userDetails.getSimSpecs()) {
            JSONObject jsonRepo = new JSONObject();
            jsonRepos.put(jsonRepo);
            jsonRepo.put("name", repoDetails.getName());
            jsonRepo.put("folder", repoDetails.getFolder());
            jsonRepo.put("path", repoDetails.getPath());
            jsonRepo.put("type", repoDetails.getType());
            jsonRepo.put("id", repoDetails.getSimId());
        }
        return jsonRepos.toString();
    }


    @Secured("persistence")
    @DeleteMapping(value = "/sim/{simId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean dropSim(HttpServletRequest request, @PathVariable long simId) throws Exception {
        String username = request.getUserPrincipal().getName();
        return dataLoader.dropSim(username, simId);
    }

    @Secured("persistence")
    @PatchMapping(value = "/sim/{simId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateSim(HttpServletRequest request,
                             @PathVariable long simId,
                             @RequestParam String name,
                             @RequestParam String folder,
                             @RequestParam String path,
                             @RequestParam String type) throws Exception {
        String username = request.getUserPrincipal().getName();
        return dataLoader.updateSim(simId, username, name, folder, path, type);
    }

}
