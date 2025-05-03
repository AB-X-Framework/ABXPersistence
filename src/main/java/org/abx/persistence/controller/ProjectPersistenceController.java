package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.ProjectPersistenceManager;
import org.abx.persistence.client.RepoPersistenceManager;
import org.abx.persistence.client.UserPersistenceManager;
import org.abx.persistence.client.model.ProjectEnrollment;
import org.abx.persistence.client.model.SimSpecs;
import org.abx.persistence.client.model.UserDetails;
import org.abx.spring.ErrorMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.abx.persistence.client.RepoPersistenceManager.Project;

/**
 * A controller to handle project request
 */
@RestController
@RequestMapping("/persistence")
public class ProjectPersistenceController {

    @Autowired
    private ProjectPersistenceManager projectPersistenceManager;

    @Autowired
    private UserPersistenceManager userPersistenceManager;

    @Autowired
    private RepoPersistenceManager repoPersistenceManager;

    /**
     * Gets all user enrollment
     * @param request THe full HTTP Request
     * @return THe user enrollment in JSON format of projectName, projectId
     */
    @Secured("Persistence")
    @GetMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_VALUE)
    public String projectEnrollments(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.projectEnrollments(username).toString();
    }

    /**
     * Creates a new project
     * @param request THe full HTTP Request
     * @param projectData THe project data in JSON format
     * @return The project id
     */
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
                    repo.getString("repoName"),
                    repo.getString("engine"),
                    repo.getString("url"),
                    repo.getString("branch"),
                    repo.getString("creds")
            );
        }
        return id;
    }

    /**
     * Gets project name from id
     * @param request THe full HTTP Request
     * @param projectId The project id
     * @return Project name
     */
    @Secured("Persistence")
    @GetMapping(value = "/projects/{projectId}/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProjectName(HttpServletRequest request,
                                 @PathVariable long projectId) {
        String username = request.getUserPrincipal().getName();
        JSONObject repoDetails = projectPersistenceManager.getProjectName(username, projectId);
        return repoDetails.toString();
    }
    /**
     * Gets project repos from id
     * @param request THe full HTTP Request
     * @param projectId The project id
     * @return Project repos in JSON format
     */
    @Secured("Persistence")
    @GetMapping(value = "/projects/{projectId}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProjectRepos(HttpServletRequest request,
                                  @PathVariable long projectId) {
        String username = request.getUserPrincipal().getName();
        JSONObject repoDetails = projectPersistenceManager.getProjectRepos(username, projectId);
        return repoDetails.toString();
    }

    /**
     * Updates project name from id
     * @param request THe full HTTP Request
     * @param projectId The project id
     * @return if project was updated
     */
    @Secured("Persistence")
    @PatchMapping(value = "/projects/{projectId}/name")
    public boolean updateProjectName(HttpServletRequest request,
                                     @PathVariable long projectId,
                                     @RequestParam String projectName) {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.updateProjectName(username, projectId, projectName);
    }

    /**
     * Gets user enrollment status of enrollment of other users if allowed to see them
     * @param request The full HTTP Request
     * @param projectId The project Id
     * @return A JSON object with project enrollments
     */
    @Secured("Persistence")
    @GetMapping(value = "/projects/{projectId}/enrollment", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getEnrollment(HttpServletRequest request,
                                @PathVariable long projectId) {
        String username = request.getUserPrincipal().getName();
        JSONObject repoDetails = projectPersistenceManager.getEnrollment(username, projectId);
        return repoDetails.toString();
    }

    /**
     * Deletes a project with give id
     * @param request The full HTTP Request
     * @param projectId The project id
     * @return If project was deleted
     */
    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}")
    public boolean deleteProject(HttpServletRequest request,
                                 @PathVariable long projectId) {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.deleteProject(username, projectId);
    }

    /**
     * Adds a repo from project id
     * @param request The full HTTP Request
     * @param projectId  The project id
     * @param repoName The repo name
     * @param engine The repo engine
     * @param url The repo url
     * @param branch  The repo branch
     * @param creds  The repo creds
     * @return A json with error: true if there was a failure
     */
    @Secured("Persistence")
    @PostMapping(value = "/projects/{projectId}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addRepo(HttpServletRequest request,
                          @PathVariable long projectId,
                          @RequestParam String repoName,
                          @RequestParam String engine,
                          @RequestParam String url,
                          @RequestParam String branch,
                          @RequestParam String creds) {
        String username = request.getUserPrincipal().getName();
        if (projectPersistenceManager.getRepoNames(username, projectId).contains(repoName)) {
            return ErrorMessage.errorString("Repo already exists");
        }
        return createRepoGetStatus(projectId, repoName, engine, url, branch, creds, username);
    }

    /**
     * Internal method to create repo and get status
     * @param projectId The project id
     * @param repoName The repo name
     * @param engine The repo engine
     * @param url The repo url
     * @param branch The repo branch
     * @param creds The repo creds
     * @param username The repo username
     * @return A JSON with error:true if the repo was not created
     */
    private String createRepoGetStatus(long projectId, String repoName, String engine, String url,
                                       String branch, String creds, String username) {
        boolean created = repoPersistenceManager.createProjectRepoIfNotFound(username, projectId,
                repoName, engine, url, branch, creds);
        if (!created) {
            return ErrorMessage.errorString("Cannot create repo");
        }
        JSONObject status = new JSONObject();
        status.put("error", false);
        return status.toString();
    }
    /**
     * Adds a repo from project id
     * @param request The full HTTP Request
     * @param projectId  The project id
     * @param repoName The original repo name
     * @param newName The new repo name
     * @param engine The repo engine
     * @param url The repo url
     * @param branch  The repo branch
     * @param creds  The repo creds
     * @return A json with error: true if there was a failure
     */
    @Secured("Persistence")
    @PatchMapping(value = "/projects/{projectId}/repos/{repoName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateRepo(HttpServletRequest request,
                             @PathVariable long projectId,
                             @PathVariable String repoName,
                             @RequestParam String newName,
                             @RequestParam String engine,
                             @RequestParam String url,
                             @RequestParam String branch,
                             @RequestParam String creds) {
        String username = request.getUserPrincipal().getName();
        if (!repoName.equals(newName)) {
            if (projectPersistenceManager.getRepoNames(username, projectId).contains(newName)) {
                return ErrorMessage.errorString("Repo name already exists");
            }
            repoPersistenceManager.deleteRepo(Project, username, projectId, repoName);
        }
        return createRepoGetStatus(projectId, newName, engine, url, branch, creds, username);
    }

    /**
     * Deletes a repo from a project
     * @param request The full HTTP Request
     * @param projectId The project id
     * @param repoName The repo name
     * @return true if the repo was deleted
     */
    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}/repos/{repoName}")
    public boolean deleteRepo(HttpServletRequest request,
                              @PathVariable long projectId,
                              @PathVariable String repoName) {
        String username = request.getUserPrincipal().getName();
        return repoPersistenceManager.deleteRepo(Project, username, projectId, repoName);
    }


    /**
     * Adds a new simulation to the project
     * @param request  The full HTTP Request
     * @param projectId The project id
     * @param simName A simulation name
     * @param folder The folder path
     * @param path The initial script path inside the folder
     * @param type The simulation type
     * @return the id of the simulation
     */
    @Secured("Persistence")
    @PostMapping(value = "/projects/{projectId}/sim", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addSim(HttpServletRequest request,
                       @PathVariable long projectId,
                       @RequestParam String simName,
                       @RequestParam String folder,
                       @RequestParam String path,
                       @RequestParam String type) {
        String username = request.getUserPrincipal().getName();
        try {
            long simId = projectPersistenceManager.createSimSpecs(username, projectId, simName, folder, path, type);
            JSONObject sim = new JSONObject();
            sim.put("error", false);
            sim.put("id", simId);
            return sim.toString();
        } catch (Exception e) {
            return ErrorMessage.errorString(e.getMessage());
        }
    }

    /**
     * Drops all simulations of a project id
     * @param request The full HTTP Request
     * @param projectId The project id
     * @return true if all simulations were dropped
     */
    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}/sims", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean dropSims(HttpServletRequest request,
                            @RequestParam long projectId) {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.dropSims(username, projectId);
    }

    /**
     * Gets all simulation from a user
     * @param request The full HTTP Request
     * @return A JSON Array of all simulation
     */
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

    /**
     * Drops a simulation id
     * @param request The full HTTP Request
     * @param projectId The project id
     * @param simId The simulation id
     * @return true if the simulation was successfully remove
     */
    @Secured("Persistence")
    @DeleteMapping(value = "/projects/{projectId}/sim/{simId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean dropSim(HttpServletRequest request,
                           @PathVariable long projectId,
                           @PathVariable long simId) {
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
                             @RequestParam String type) {
        String username = request.getUserPrincipal().getName();
        return projectPersistenceManager.updateSim(username, projectId, simId, simName, folder, path, type);
    }

    @Secured("Persistence")
    @PostMapping(value = "/projects/{projectId}/sim/{simId}/exec", produces = MediaType.APPLICATION_JSON_VALUE)
    public long addExec(HttpServletRequest request,
                        @PathVariable long projectId, @PathVariable long simId) {
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
                          @PathVariable long execId)  {
        String username = request.getUserPrincipal().getName();
        JSONObject exec = projectPersistenceManager.getExec(username, projectId, simId, execId);
        if (exec == null) {
            return ErrorMessage.errorString("You are not allowed to access this resource");
        }
        return exec.toString();
    }


}
