package org.abx.persistence.client;

import org.abx.persistence.client.dao.*;
import org.abx.persistence.client.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class ProjectPersistenceManager {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private ProjectEnrollmentRepository projectEnrollmentRepository;

    @Autowired
    private ExecDetailsRepository execDetailsRepository;

    @Autowired
    private RepoDetailsRepository repoDetailsRepository;

    @Autowired
    private SimSpecsRepository simSpecsRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private UserPersistenceManager userPersistenceManager;


    private Long addProject(UserDetails userDetails, String projectName) {
        ProjectDetails projectDetails = new ProjectDetails();
        projectDetails.setProjectName(projectName);
        projectDetailsRepository.save(projectDetails);
        ProjectEnrollment projectEnrollment = new ProjectEnrollment();
        projectEnrollment.setProjectDetails(projectDetails);
        projectEnrollment.setUserDetails(userDetails);
        projectEnrollment.setRole(ProjectRole.Owner.name());
        projectEnrollmentRepository.save(projectEnrollment);
        return projectDetails.getProjectId();
    }

    @Transactional
    public JSONArray projectEnrollments(final String username) {
        JSONArray jsonEnrollments = new JSONArray();
        for (ProjectEnrollment projectEnrollment : userPersistenceManager.
                createOrFind(username).getProjectEnrollments()) {
            JSONObject jsonEnrollment = new JSONObject();
            jsonEnrollments.put(jsonEnrollment);
            ProjectDetails projectDetails = projectEnrollment.getProjectDetails();
            jsonEnrollment.put("projectName", projectDetails.getProjectName());
            jsonEnrollment.put("projectId", projectDetails.getProjectId());
        }
        return jsonEnrollments;
    }

    /**
     * Gets user role and other enrollments if user can see them
     *
     * @param username  The username
     * @param projectId The project id
     * @return The json with role and other enrollments if user can see them, null otherwise
     */
    @Transactional
    public JSONObject getEnrollment(final String username, final long projectId) {
        JSONObject jsonEnrollment = new JSONObject();
        ProjectEnrollment projectEnrollment = projectEnrollmentRepository.
                findByProjectDetailsProjectIdAndUserDetailsUsername(projectId, username);
        if (projectEnrollment == null) {
            return null;
        }
        String role = projectEnrollment.getRole();
        ProjectRole projectRole = ProjectRole.valueOf(role);
        boolean canSeeEnrollments = projectRole.canSeeEnrollments();
        jsonEnrollment.put("role", role);
        jsonEnrollment.put("canSeeEnrollments", canSeeEnrollments);
        jsonEnrollment.put("canAddUser", projectRole.canSeeEnrollments());
        if (canSeeEnrollments) {
            JSONArray jsonEnrollments = new JSONArray();
            jsonEnrollment.put("enrollments", jsonEnrollments);
            for (ProjectEnrollment pe : projectEnrollment.getProjectDetails().getEnrollment()) {
                JSONObject userEnrollment = new JSONObject();
                userEnrollment.put("username", pe.getUserDetails().getUsername());
                userEnrollment.put("projectId", pe.getRole());
            }
        }
        return jsonEnrollment;
    }

    /**
     * The project name from project id if user can see it
     *
     * @param username  The username
     * @param projectId The project id
     * @return A Json with project name if user can see it, otherwise null
     */
    @Transactional
    public JSONObject getProjectName(final String username, final long projectId) {
        ProjectEnrollment projectEnrollment = projectEnrollmentRepository.
                findByProjectDetailsProjectIdAndUserDetailsUsername(projectId, username);
        if (projectEnrollment == null) {
            return null;
        }
        JSONObject jsonProjectDetails = new JSONObject();
        ProjectDetails projectDetails = projectEnrollment.getProjectDetails();
        jsonProjectDetails.put("projectName", projectDetails.getProjectName());
        jsonProjectDetails.put("projectId", projectId);
        return jsonProjectDetails;
    }

    /**
     * Returns the project repositories if user can access them
     *
     * @param username  The username
     * @param projectId The project id
     * @return The repositories in JSON format
     */
    @Transactional
    public JSONObject getProjectRepos(String username, final long projectId) {
        ProjectEnrollment projectEnrollment = projectEnrollmentRepository.
                findByProjectDetailsProjectIdAndUserDetailsUsername(projectId, username);
        if (projectEnrollment == null) {
            return null;
        }
        JSONObject jsonProjectDetails = new JSONObject();
        ProjectDetails projectDetails = projectEnrollment.getProjectDetails();
        JSONArray jsonRepos = new JSONArray();
        jsonProjectDetails.put("repos", jsonRepos);
        for (ProjectRepo projectRepo : projectDetails.getProjectRepos()) {
            RepoDetails repo = projectRepo.getRepoDetails();
            JSONObject jsonRepo = new JSONObject();
            jsonRepos.put(jsonRepo);
            jsonRepo.put("repoName", repo.getRepoName());
            jsonRepo.put("engine", repo.getEngine());
            jsonRepo.put("id", repo.getRepoId());
            jsonRepo.put("repoName", repo.getRepoName());
            jsonRepo.put("branch", repo.getBranch());
            jsonRepo.put("url", repo.getUrl());
            jsonRepo.put("creds", repo.getCreds());
        }
        return jsonProjectDetails;
    }

    /**
     * Gets the list of repository names of project if user can access them
     *
     * @param username  The username
     * @param projectId The project id
     * @return A Set with repository names
     */
    @Transactional
    public Set<String> getRepoNames(String username, final long projectId) {
        ProjectEnrollment projectEnrollment = projectEnrollmentRepository.
                findByProjectDetailsProjectIdAndUserDetailsUsername(projectId, username);
        if (projectEnrollment == null) {
            return null;
        }
        Set<String> repos = new HashSet<>();
        ProjectDetails projectDetails = projectEnrollment.getProjectDetails();
        for (ProjectRepo projectRepo : projectDetails.getProjectRepos()) {
            repos.add(projectRepo.getRepoDetails().getRepoName());
        }
        return repos;
    }

    /**
     * Deletes a project if user is owner
     *
     * @param username  The username
     * @param projectId The project id
     * @return True if the project was successfully deleted
     */
    @Transactional
    public boolean deleteProject(String username, final long projectId) {
        ProjectEnrollment projectEnrollment =
                projectEnrollmentRepository.findByProjectDetailsProjectIdAndUserDetailsUsername(projectId, username);
        if (projectEnrollment == null) {
            return false;
        }
        if (Objects.equals(projectEnrollment.getRole(), ProjectRole.Owner.name())) {
            ProjectDetails projectDetails = projectEnrollment.getProjectDetails();
            for (ProjectRepo projectRepo : projectDetails.getProjectRepos()) {
                RepoDetails repo = projectRepo.getRepoDetails();
                repoDetailsRepository.delete(repo);
            }
            projectDetailsRepository.delete(projectEnrollment.getProjectDetails());
        } else {
            projectEnrollmentRepository.delete(projectEnrollment);
        }
        return true;
    }

    /**
     * Adds a project with username name and project name
     *
     * @param username    The username
     * @param projectName The project name
     * @return The project id
     */
    @Transactional
    public Long addProject(final String username, String projectName) {
        UserDetails userDetails = userPersistenceManager.createOrFind(username);
        return addProject(userDetails, projectName);
    }

    /**
     * Updates project name if user can rename it
     *
     * @param username    The username
     * @param projectId   The project id
     * @param projectName The new  project name
     * @return True if the project was renamed
     */
    @Transactional
    public boolean updateProjectName(final String username, long projectId, String projectName) {
        ProjectEnrollment projectEnrollment = projectEnrollmentRepository.
                findByProjectDetailsProjectIdAndUserDetailsUsername(projectId, username);
        if (projectEnrollment == null) {
            return false;
        }
        ProjectRole role = ProjectRole.valueOf(projectEnrollment.getRole());
        if (!role.canRename()) {
            return false;
        }
        ProjectDetails projectDetails = projectEnrollment.getProjectDetails();
        projectDetails.setProjectName(projectName);
        projectDetailsRepository.save(projectDetails);
        return true;
    }


    /**
     * Adds a simulation if enrollment allows to
     *
     * @param username  The username
     * @param projectId The project id
     * @param simName   A new simulation name
     * @param folder    The resources folder path
     * @param path      The init script from the folder path
     * @param type      The simulation type
     * @return The simulation id
     */
    @Transactional
    public long createSimSpecs(String username, long projectId, String simName,
                               String folder, String path, String type) throws Exception {
        ProjectEnrollment pe =
                projectEnrollmentRepository.findByProjectDetailsProjectIdAndUserDetailsUsername(projectId, username);
        if (pe == null || !ProjectRole.valueOf(pe.getRole()).canAddSim()) {
            throw new Exception("User do not have enought permissions to add simulation");
        }
        ProjectDetails projectDetails = projectDetailsRepository.findByProjectId(projectId);
        SimSpecs specs = new SimSpecs();
        specs.setProjectDetails(projectDetails);
        specs.setSimName(simName);
        specs.setFolder(folder);
        specs.setPath(path);
        specs.setType(type);
        simSpecsRepository.save(specs);
        return specs.getSimId();
    }

    @Transactional
    public boolean dropSims(String username, long projectId) {
        UserDetails userDetails = userPersistenceManager.createOrFind(username);
        if (!projectEnrollmentRepository.existsByUserDetailsUsernameAndProjectDetailsProjectId
                (username, projectId)) {
            return false;
        }
        ProjectDetails projectDetails = projectDetailsRepository.findByProjectId(projectId);

        projectDetails.getSimSpecs().clear();
        userDetailsRepository.save(userDetails);
        return true;
    }

    @Transactional
    public boolean dropSim(String username, long projectId, long simId) {
        if (!projectEnrollmentRepository.existsByUserDetailsUsernameAndProjectDetailsProjectId
                (username, projectId)) {
            return false;
        }
        SimSpecs specs = validSimSpecs(username, projectId, simId);
        if (specs == null) {
            return false;
        }
        simSpecsRepository.delete(specs);
        return true;
    }

    private SimSpecs validSimSpecs(String username, long projectId, long simId) {
        if (!projectEnrollmentRepository.existsByUserDetailsUsernameAndProjectDetailsProjectId
                (username, projectId)) {
            return null;
        }
        SimSpecs specs = simSpecsRepository.findBySimId(simId);
        if (specs == null) {
            return null;
        }
        if (specs.getProjectDetails().getProjectId() != projectId) {
            return null;
        }
        return specs;
    }

    @Transactional
    public boolean updateSim(String username, long projectId, long simId,
                             String simName, String folder, String path, String type) {
        SimSpecs specs = validSimSpecs(username, projectId, simId);
        if (specs == null) {
            return false;
        }
        specs.setSimName(simName);
        specs.setFolder(folder);
        specs.setPath(path);
        specs.setType(type);
        simSpecsRepository.save(specs);
        return true;
    }

    @Transactional
    public long addExec(String username, long projectId, long simId) {
        SimSpecs specs = validSimSpecs(username, projectId, simId);
        if (specs == null) {
            return -1;
        }
        ExecDetails execDetails = new ExecDetails();
        execDetails.setExecName(specs.getSimName());
        execDetails.setPath(specs.getPath());
        execDetails.setFolder(specs.getFolder());
        execDetails.setProjectDetails(specs.getProjectDetails());
        execDetails.setSimSpecs(specs);
        execDetails.setOutput("");
        return execDetailsRepository.save(execDetails).getExecId();
    }

    /**
     * Gets the execution
     *
     * @param username  The username
     * @param projectId The project id
     * @param simId     The Simulation id
     * @param execId    The execution id
     * @return A JSON with the exectuion or null if the user does not have access to the project id
     */
    @Transactional
    public JSONObject getExec(String username, long projectId, long simId, long execId) {
        SimSpecs simSpecs = validSimSpecs(username, projectId, simId);
        if (simSpecs == null) {
            return null;
        }
        ExecDetails execDetails = execDetailsRepository.findByExecId(execId);
        if (execDetails == null) {
            return null;
        }
        if (simId != execDetails.getSimSpecs().getSimId()) {
            return null;
        }
        JSONObject exec = new JSONObject();
        exec.put("name", execDetails.getExecName());
        exec.put("path", execDetails.getPath());
        exec.put("folder", execDetails.getFolder());
        exec.put("type", execDetails.getType());
        exec.put("output", execDetails.getOutput());
        return exec;
    }
}

