package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.PersistenceDataLoader;
import org.abx.persistence.client.model.RepoDetails;
import org.abx.persistence.client.model.SimSpecs;
import org.abx.persistence.client.model.UserDetails;
import org.abx.persistence.client.model.UserProject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @PostMapping(value = "/project/{projectId}/repo")
    public String addRepo(HttpServletRequest request,
                          @PathVariable long projectId,
                          @RequestParam String name,
                          @RequestParam String url,
                          @RequestParam String branch,
                          @RequestParam String creds) {
        String username = request.getUserPrincipal().getName();
        RepoDetails repoDetails = dataLoader.createRepoIfNotFound(username, projectId, name, url, branch, creds);
        return repoDetails.getName();
    }

    @Secured("persistence")
    @DeleteMapping(value = "/project/{projectId}/repo/{reponame}")
    public boolean deleteRepo(HttpServletRequest request,
                              @PathVariable long projectId,
                              @PathVariable String reponame) {
        String username = request.getUserPrincipal().getName();
        return dataLoader.deleteRepo(username, projectId, reponame);
    }

    @Secured("persistence")
    @GetMapping(value = "/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String repos(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        JSONArray jsonRepos = new JSONArray();
        UserDetails userDetails = dataLoader.createUserIfNotFound(username);
        for (UserProject projectDetails : userDetails.getUserProjects()) {
            for (RepoDetails repoDetails : projectDetails.getProjectDetails().getRepoDetails()) {
                JSONObject jsonRepo = new JSONObject();
                jsonRepos.put(jsonRepo);
                jsonRepo.put("name", repoDetails.getName());
                jsonRepo.put("branch", repoDetails.getBranch());
                jsonRepo.put("url", repoDetails.getUrl());
                jsonRepo.put("creds", repoDetails.getCreds());
            }
        }
        return jsonRepos.toString();
    }


    @Secured("persistence")
    @PostMapping(value = "/sim", produces = MediaType.APPLICATION_JSON_VALUE)
    public long addSim(HttpServletRequest request,
                       @RequestParam long projectId,
                       @RequestParam String name,
                       @RequestParam String folder,
                       @RequestParam String path,
                       @RequestParam String type) throws Exception {
        String username = request.getUserPrincipal().getName();
        return dataLoader.createSimSpecs(username, projectId, name, folder, path, type).getSimId();
    }

    @Secured("persistence")
    @DeleteMapping(value = "/sims", produces = MediaType.APPLICATION_JSON_VALUE)
    public int dropSims(HttpServletRequest request,
                        @RequestParam long projectId) throws Exception {
        String username = request.getUserPrincipal().getName();
        return dataLoader.dropSims(username, projectId);
    }

    @Secured("persistence")
    @GetMapping(value = "/sims", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sims(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        JSONArray jsonRepos = new JSONArray();
        UserDetails userDetails = dataLoader.createUserIfNotFound(username);
        for (UserProject userProject : userDetails.getUserProjects()) {
            for (SimSpecs repoDetails : userProject.getProjectDetails().getSimSpecs()) {
                JSONObject jsonRepo = new JSONObject();
                jsonRepos.put(jsonRepo);
                jsonRepo.put("name", repoDetails.getName());
                jsonRepo.put("folder", repoDetails.getFolder());
                jsonRepo.put("path", repoDetails.getPath());
                jsonRepo.put("type", repoDetails.getType());
                jsonRepo.put("id", repoDetails.getSimId());
            }
        }
        return jsonRepos.toString();
    }


    @Secured("persistence")
    @DeleteMapping(value = "/sim/{simId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean dropSim(HttpServletRequest request,
                           @RequestParam long projectId,@PathVariable long simId) throws Exception {
        String username = request.getUserPrincipal().getName();
        return dataLoader.dropSim(username,projectId, simId);
    }

    @Secured("persistence")
    @PatchMapping(value = "/project/{projectId}/sim/{simId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateSim(HttpServletRequest request,
                             @PathVariable long projectId,
                             @PathVariable long simId,
                             @RequestParam String name,
                             @RequestParam String folder,
                             @RequestParam String path,
                             @RequestParam String type) throws Exception {
        String username = request.getUserPrincipal().getName();
        return dataLoader.updateSim(projectId, simId, username, name, folder, path, type);
    }

    @Secured("persistence")
    @PostMapping(value = "/sim/{simId}/exec", produces = MediaType.APPLICATION_JSON_VALUE)
    public long addExec(HttpServletRequest request, @PathVariable long simId) throws Exception {
        String username = request.getUserPrincipal().getName();
        long execId = dataLoader.addExec(username, simId);
        if (execId == -1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource");
        }
        return execId;
    }

    @Secured("persistence")
    @PostMapping(value = "/sim/{simId}/exec/{execId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExec(HttpServletRequest request, @PathVariable long simId, @PathVariable long execId) throws Exception {
        String username = request.getUserPrincipal().getName();
        JSONObject exec = dataLoader.getExec(username, simId, execId);
        if (exec == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource");
        }
        return exec.toString();
    }
}
