package org.abx.persistence.client;

import org.abx.persistence.client.dao.*;
import org.abx.persistence.client.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
public class ProjectPersistenceManager {

    public final static String Project = "Project";
    public final static String Dashboard = "Dashboard";
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

    @Autowired
    private ProjectRepoRepository projectRepoRepository;

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
    public UserDetails createUserIfNotFound(final String name) {
        return userPersistenceManager.createOrFind(name);
    }

    @Transactional
    public JSONArray projectEnrollments(final String username) {
        JSONArray jsonEnrollments = new JSONArray();
        for (ProjectEnrollment projectEnrollment : userPersistenceManager.createOrFind(username).getProjectEnrollments()) {
            JSONObject jsonEnrollment = new JSONObject();
            jsonEnrollments.put(jsonEnrollment);
            ProjectDetails projectDetails = projectEnrollment.getProjectDetails();
            jsonEnrollment.put("projectName", projectDetails.getProjectName());
            jsonEnrollment.put("projectId", projectDetails.getProjectId());
        }
        return jsonEnrollments;
    }

    @Transactional
    public JSONObject getProjectDetails(String username, final long projectId) {
        ProjectEnrollment projectEnrollment = projectEnrollmentRepository.findByProjectDetailsProjectIdAndUserDetailsUsername(projectId,username);
        if (projectEnrollment == null) {
            return null;
        }
        JSONObject jsonEnrollments = new JSONObject();
        ProjectDetails projectDetails = projectEnrollment.getProjectDetails();
        jsonEnrollments.put("projectName", projectDetails.getProjectName());
        jsonEnrollments.put("projectId", projectId);
        return jsonEnrollments;
    }


    @Transactional
    public boolean deleteProject(String username, final long projectId) {
        ProjectEnrollment projectEnrollment =
                projectEnrollmentRepository.findByProjectDetailsProjectIdAndUserDetailsUsername(projectId,username);
        if (projectEnrollment == null) {
            return false;
        }
        if (Objects.equals(projectEnrollment.getRole(), ProjectRole.Owner.name())) {
            projectDetailsRepository.delete(projectEnrollment.getProjectDetails());
        }else {
            projectEnrollmentRepository.delete(projectEnrollment);
        }
        return true;
    }

    @Transactional
    public Long addProject(final String username, String projectName) {
        UserDetails userDetails = userPersistenceManager.createOrFind(username);
        return addProject(userDetails, projectName);
    }

    @Transactional
    public boolean updateProject(final String username, long projectId, String projectName) {
        UserDetails userDetails = userPersistenceManager.createOrFind(username);
        ProjectEnrollment projectEnrollment = projectEnrollmentRepository.
                findByProjectDetailsProjectIdAndUserDetailsUsername(projectId,username);
        if (projectEnrollment == null){
            return false;
        }
        ProjectRole role = ProjectRole.valueOf(projectEnrollment.getRole());
        if (!role.canRename()){
            return false;
        }
        ProjectDetails projectDetails = projectEnrollment.getProjectDetails();
        projectDetails.setProjectName(projectName);
        projectDetailsRepository.save(projectDetails);
        return true;
    }

    public static long repoId(String type, long projectId, String repoName) {
        return (type +"/"+projectId + "/" + repoName).hashCode();
    }

    @Transactional
    public RepoDetails createProjectRepoIfNotFound(String username, long projectId, final String name, String url, String branch, String creds) {
        if (!projectEnrollmentRepository.existsByUserDetailsUsernameAndProjectDetailsProjectId
                (username, projectId)) {
            return null;
        }
        ProjectDetails projectDetails = projectDetailsRepository.findByProjectId(projectId);
        long repoId = repoId(Project, projectId, name);
        RepoDetails repoDetails = repoDetailsRepository.findByRepoId(repoId);
        if (repoDetails == null) {
            repoDetails = new RepoDetails();
            repoDetails.setRepoId(repoId);
            repoDetails.setRepoName(name);
            repoDetails.setUrl(url);
            repoDetails.setBranch(branch);
            repoDetails.setCreds(creds);
            repoDetails = repoDetailsRepository.save(repoDetails);

            ProjectRepo projectRepo = new ProjectRepo();
            projectRepo.setProjectRepoId(repoId);
            projectRepo.setProjectDetails(projectDetails);
            projectRepo.setRepoDetails(repoDetails);
            projectRepoRepository.save(projectRepo);
        } else {
            repoDetails.setUrl(url);
            repoDetails.setBranch(branch);
            repoDetails.setCreds(creds);
            repoDetails = repoDetailsRepository.save(repoDetails);

        }
        return repoDetails;
    }

    @Transactional
    public boolean deleteRepo(String type, String username, long projectId, String name) {
        if (!projectEnrollmentRepository.existsByUserDetailsUsernameAndProjectDetailsProjectId
                (username, projectId)) {
            return false;
        }
        long repoId = repoId(type, projectId, name);
        RepoDetails repoDetails = repoDetailsRepository.findByRepoId(repoId);
        if (repoDetails == null) {
            return false;
        }
        repoDetailsRepository.delete(repoDetails);
        return true;
    }

    @Transactional
    public SimSpecs createSimSpecs(String username, long projectId, String simName, String folder, String path, String type) {
        if (!projectEnrollmentRepository.existsByUserDetailsUsernameAndProjectDetailsProjectId
                (username, projectId)) {
            return null;
        }
        ProjectDetails projectDetails = projectDetailsRepository.findByProjectId(projectId);
        SimSpecs specs = new SimSpecs();
        specs.setProjectDetails(projectDetails);
        specs.setSimName(simName);
        specs.setFolder(folder);
        specs.setPath(path);
        specs.setType(type);
        simSpecsRepository.save(specs);
        return specs;
    }

    @Transactional
    public boolean dropSims(String username, long projectId) {
        UserDetails userDetails = userPersistenceManager.createOrFind(username);
        if (!projectEnrollmentRepository.existsByUserDetailsUsernameAndProjectDetailsProjectId
                (username, projectId)) {
            return false;
        }
        ProjectDetails projectDetails = projectDetailsRepository.findByProjectId(projectId);
        projectDetails.getSimSpecs().size();
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

    @Transactional
    public JSONObject getExec(String username, long projectId, long simId, long execId) throws Exception {
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

