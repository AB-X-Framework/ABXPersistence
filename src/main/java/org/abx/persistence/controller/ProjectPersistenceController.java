package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.DashboardPersistenceManager;
import org.abx.persistence.client.ProjectPersistenceManager;
import org.abx.persistence.client.RepoPersistenceManager;
import org.abx.persistence.client.UserPersistenceManager;
import org.abx.persistence.client.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.abx.persistence.client.RepoPersistenceManager.Project;


@RestController
@RequestMapping("/persistence")
public class ProjectPersistenceController {

    @Autowired
    private ProjectPersistenceManager projectPersistenceManager;

    @Autowired
    private UserPersistenceManager userPersistenceManager;

    @Autowired
    private RepoPersistenceManager repoPersistenceManager;


    @Secured("Persistence")
    @GetMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public String projectEnrollments(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.projectEnrollments(username).toString();
    }

    @Secured("Persistence")
    @PostMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long addProject(HttpServletRequest request,
                           @RequestParam String projectData) {
        String username = request.getUserPrincipal().getName();
        JSONObject jsonProject = new JSONObject(projectData);
        long id = projectPersistenceManager.addProject(username, jsonProject.getString("name"));
        JSONArray repos = jsonProject.getJSONArray("repos");
        for (int i = 0; i < repos.length(); ++i) {
            JSONObject repo = repos.getJSONObject(i);
            repoPersistenceManager.createProjectRepoIfNotFound(
                    username, id,
                    repo.getString("name"),
                    repo.getString("engine"),
                    repo.getString("url"),
                    repo.getString("branch"),
                    repo.getString("creds")
            );
        }
        return id;
    }

    @Secured("Persistence")
    @GetMapping(value = "/projects/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProject(HttpServletRequest request,
                             @PathVariable long projectId) {
        String username = request.getUserPrincipal().getName();
        JSONObject repoDetails = projectPersistenceManager.getProjectDetails(username, projectId);
        return repoDetails.toString();
    }

    @Secured("Persistence")
    @PatchMapping(value = "/projects/{projectId}/name")
    public boolean updateProjectName(HttpServletRequest request,
                                     @PathVariable long projectId,
                                     @RequestParam String projectName) {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.updateProjectName(username, projectId, projectName);
    }

    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}")
    public boolean deleteProject(HttpServletRequest request,
                                 @PathVariable long projectId) {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.deleteProject(username, projectId);
    }

    @Secured("Persistence")
    @PostMapping(value = "/projects/{projectId}/repo")
    public String addRepo(HttpServletRequest request,
                          @PathVariable long projectId,
                          @RequestParam String repoName,
                          @RequestParam String engine,
                          @RequestParam String url,
                          @RequestParam String branch,
                          @RequestParam String creds) {
        String username = request.getUserPrincipal().getName();
        RepoDetails repoDetails = repoPersistenceManager.createProjectRepoIfNotFound(username, projectId, repoName, engine, url, branch, creds);
        return repoDetails.getRepoName();
    }


    @Secured("Persistence")
    @PatchMapping(value = "/projects/{projectId}/repo")
    public String updateRepo(HttpServletRequest request,
                             @PathVariable long projectId,
                             @RequestParam String repoName,
                             @RequestParam String newName,
                             @RequestParam String engine,
                             @RequestParam String url,
                             @RequestParam String branch,
                             @RequestParam String creds) {
        String username = request.getUserPrincipal().getName();
        if (!repoName.equals(newName)) {
            repoPersistenceManager.deleteRepo(Project,username,projectId,repoName);
        }
        RepoDetails repoDetails = repoPersistenceManager.createProjectRepoIfNotFound(username, projectId, newName, engine, url, branch, creds);
        return repoDetails.getRepoName();
    }

    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}/repos/{repoName}")
    public boolean deleteRepo(HttpServletRequest request,
                              @PathVariable long projectId,
                              @PathVariable String repoName) {
        String username = request.getUserPrincipal().getName();
        return repoPersistenceManager.deleteRepo(Project, username, projectId, repoName);
    }

    @Secured("Persistence")
    @GetMapping(value = "/projects/{projectId}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String repos(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        JSONArray jsonRepos = new JSONArray();
        UserDetails userDetails = userPersistenceManager.createUserIfNotFound(username);
        for (ProjectEnrollment projectDetails : userDetails.getProjectEnrollments()) {
            for (ProjectRepo projectRepo : projectDetails.getProjectDetails().getProjectRepos()) {
                RepoDetails repoDetails = projectRepo.getRepoDetails();
                JSONObject jsonRepo = new JSONObject();
                jsonRepos.put(jsonRepo);
                jsonRepo.put("id", repoDetails.getRepoId());
                jsonRepo.put("repoName", repoDetails.getRepoName());
                jsonRepo.put("branch", repoDetails.getBranch());
                jsonRepo.put("url", repoDetails.getUrl());
                jsonRepo.put("creds", repoDetails.getCreds());
            }
        }
        return jsonRepos.toString();
    }


    @Secured("Persistence")
    @PostMapping(value = "/projects/{projectId}/sim", produces = MediaType.APPLICATION_JSON_VALUE)
    public long addSim(HttpServletRequest request,
                       @PathVariable long projectId,
                       @RequestParam String simName,
                       @RequestParam String folder,
                       @RequestParam String path,
                       @RequestParam String type) throws Exception {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.createSimSpecs(username, projectId, simName, folder, path, type).getSimId();
    }

    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}/sims", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean dropSims(HttpServletRequest request,
                            @RequestParam long projectId) throws Exception {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.dropSims(username, projectId);
    }

    @Secured("Persistence")
    @GetMapping(value = "/projects/{projectId}/sims", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sims(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        JSONArray jsonRepos = new JSONArray();
        UserDetails userDetails = userPersistenceManager.createUserIfNotFound(username);
        for (ProjectEnrollment projectEnrollment : userDetails.getProjectEnrollments()) {
            for (SimSpecs repoDetails : projectEnrollment.getProjectDetails().getSimSpecs()) {
                JSONObject jsonRepo = new JSONObject();
                jsonRepos.put(jsonRepo);
                jsonRepo.put("name", repoDetails.getSimName());
                jsonRepo.put("folder", repoDetails.getFolder());
                jsonRepo.put("path", repoDetails.getPath());
                jsonRepo.put("type", repoDetails.getType());
                jsonRepo.put("id", repoDetails.getSimId());
            }
        }
        return jsonRepos.toString();
    }


    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}/sim/{simId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean dropSim(HttpServletRequest request,
                           @PathVariable long projectId,
                           @PathVariable long simId) throws Exception {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.dropSim(username, projectId, simId);
    }

    @Secured("Persistence")
    @PatchMapping(value = "/projects/{projectId}/sim/{simId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean updateSim(HttpServletRequest request,
                             @PathVariable long projectId,
                             @PathVariable long simId,
                             @RequestParam String simName,
                             @RequestParam String folder,
                             @RequestParam String path,
                             @RequestParam String type) throws Exception {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.updateSim(username, projectId, simId, simName, folder, path, type);
    }

    @Secured("Persistence")
    @PostMapping(value = "/projects/{projectId}/sim/{simId}/exec", produces = MediaType.APPLICATION_JSON_VALUE)
    public long addExec(HttpServletRequest request,
                        @PathVariable long projectId, @PathVariable long simId) throws Exception {
        String username = request.getUserPrincipal().getName();
        long execId = projectPersistenceManager.addExec(username, projectId, simId);
        if (execId == -1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource");
        }
        return execId;
    }

    @Secured("Persistence")
    @PostMapping(value = "/projects/{projectId}/sim/{simId}/exec/{execId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExec(HttpServletRequest request,
                          @PathVariable long projectId,
                          @PathVariable long simId,
                          @PathVariable long execId) throws Exception {
        String username = request.getUserPrincipal().getName();
        JSONObject exec = projectPersistenceManager.getExec(username, projectId, simId, execId);
        if (exec == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource");
        }
        return exec.toString();
    }


}
