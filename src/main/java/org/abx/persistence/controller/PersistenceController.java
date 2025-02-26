package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.DashboardDataLoader;
import org.abx.persistence.client.PersistenceDataLoader;
import org.abx.persistence.client.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.abx.persistence.client.PersistenceDataLoader.Project;

@RestController
@RequestMapping("/persistence")
public class PersistenceController {

    @Autowired
    private PersistenceDataLoader dataLoader;

    @Autowired
    private DashboardDataLoader dashboardDataLoader;
    @Secured("Persistence")
    @RequestMapping(value = "/user")
    public String user(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        UserDetails details = dataLoader.createUserIfNotFound(username);
        return details.getUsername();
    }

    @Secured("Persistence")
    @GetMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public String projectEnrollments(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return dataLoader.projectEnrollments(username).toString();
    }
    @Secured("Persistence")
    @PostMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long addProject(HttpServletRequest request,
                              @RequestParam String projectName) {
        String username = request.getUserPrincipal().getName();
        return dataLoader.addProject(username,projectName);
    }

    @Secured("Persistence")
    @GetMapping(value = "/projects/{projectId}")
    public String getProject(HttpServletRequest request,
                          @PathVariable long projectId) {
        String username = request.getUserPrincipal().getName();
        JSONObject repoDetails = dataLoader.getProjectDetails(username, projectId);
        return repoDetails.toString();
    }

    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}")
    public boolean deleteProject(HttpServletRequest request,
                             @PathVariable long projectId) {
        String username = request.getUserPrincipal().getName();
        return  dataLoader.deleteProject(username, projectId);
    }

    @Secured("Persistence")
    @PostMapping(value = "/projects/{projectId}/repo")
    public String addRepo(HttpServletRequest request,
                          @PathVariable long projectId,
                          @RequestParam String repoName,
                          @RequestParam String url,
                          @RequestParam String branch,
                          @RequestParam String creds) {
        String username = request.getUserPrincipal().getName();
        RepoDetails repoDetails = dataLoader.createProjectRepoIfNotFound(username, projectId, repoName, url, branch, creds);
        return repoDetails.getRepoName();
    }

    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}/repo/{repoName}")
    public boolean deleteRepo(HttpServletRequest request,
                              @PathVariable long projectId,
                              @PathVariable String repoName) {
        String username = request.getUserPrincipal().getName();
        return dataLoader.deleteRepo(Project,username, projectId, repoName);
    }

    @Secured("Persistence")
    @GetMapping(value = "/projects/{projectId}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String repos(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        JSONArray jsonRepos = new JSONArray();
        UserDetails userDetails = dataLoader.createUserIfNotFound(username);
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
        return dataLoader.createSimSpecs(username, projectId, simName, folder, path, type).getSimId();
    }

    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}/sims", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean dropSims(HttpServletRequest request,
                        @RequestParam long projectId) throws Exception {
        String username = request.getUserPrincipal().getName();
        return dataLoader.dropSims(username, projectId);
    }

    @Secured("Persistence")
    @GetMapping(value = "/projects/{projectId}/sims", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sims(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        JSONArray jsonRepos = new JSONArray();
        UserDetails userDetails = dataLoader.createUserIfNotFound(username);
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
        return dataLoader.dropSim(username, projectId, simId);
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
        return dataLoader.updateSim(username, projectId, simId, simName, folder, path, type);
    }

    @Secured("Persistence")
    @PostMapping(value = "/projects/{projectId}/sim/{simId}/exec", produces = MediaType.APPLICATION_JSON_VALUE)
    public long addExec(HttpServletRequest request,
                        @PathVariable long projectId, @PathVariable long simId) throws Exception {
        String username = request.getUserPrincipal().getName();
        long execId = dataLoader.addExec(username, projectId, simId);
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
        JSONObject exec = dataLoader.getExec(username, projectId, simId, execId);
        if (exec == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource");
        }
        return exec.toString();
    }

    @Secured("Persistence")
    @GetMapping(value = "/dashboards", produces = MediaType.APPLICATION_JSON_VALUE)
    public String dashboards(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return dashboardDataLoader.getDashboards(username).toString();
    }

    @Secured("Persistence")
    @GetMapping(value = "/dashboards/{dashboardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDashboard(HttpServletRequest request,
                            @PathVariable long dashboardId) {
        String username = request.getUserPrincipal().getName();
        return dashboardDataLoader.getDashboard(dashboardId, username).toString();
    }

    @Secured("Persistence")
    @PostMapping(value = "/dashboards", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long createDashboard(HttpServletRequest request,
                                @RequestParam String dashboardName) {
        String username = request.getUserPrincipal().getName();
        return  dashboardDataLoader.createDashboard(username,dashboardName);
    }

    @Secured("Persistence")
    @DeleteMapping(value = "/dashboards/{dashboardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteDashboard(HttpServletRequest request,
                                @PathVariable long dashboardId) {
        String username = request.getUserPrincipal().getName();
        return  dashboardDataLoader.deleteDashboard(dashboardId,username);
    }

    @Secured("Persistence")
    @DeleteMapping(value = "/dashboards", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteDashboard(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return  dashboardDataLoader.purgeDashboards(username);
    }

    @Secured("Persistence")
    @PostMapping(value = "/dashboards/{dashboardId}/repo")
    public String addDashboardRepo(HttpServletRequest request,
                          @PathVariable long dashboardId,
                          @RequestParam String repoName,
                          @RequestParam String url,
                          @RequestParam String branch,
                          @RequestParam String creds) {
        String username = request.getUserPrincipal().getName();
        RepoDetails repoDetails = dataLoader.createProjectRepoIfNotFound(username, dashboardId, repoName, url, branch, creds);
        return repoDetails.getRepoName();
    }
}
