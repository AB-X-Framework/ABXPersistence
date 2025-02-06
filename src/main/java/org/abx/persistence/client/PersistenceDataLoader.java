package org.abx.persistence.client;

import org.abx.persistence.client.dao.*;
import org.abx.persistence.client.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PersistenceDataLoader {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ExecDetailsRepository execDetailsRepository;

    @Autowired
    private RepoDetailsRepository repoDetailsRepository;

    @Autowired
    private SimSpecsRepository simSpecsRepository;
    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    private UserDetails createOrFind(final String name) {
        UserDetails userDetails = userDetailsRepository.findByName(name);
        if (userDetails == null) {
            userDetails = new UserDetails(name);
            userDetails = userDetailsRepository.save(userDetails);
            ProjectDetails projectDetails = new ProjectDetails();
            projectDetails.setProjectName("Personal");
            projectDetailsRepository.save(projectDetails);

        }
        return userDetails;
    }

    @Transactional
    public UserDetails createUserIfNotFound(final String name) {
        return createOrFind(name);
    }

    @Transactional
    public JSONArray enrollments(final String username) {
        JSONArray jsonEnrollments = new JSONArray();
        for (Enrollment enrollment: createOrFind(username).getUserProjects()){
            JSONObject jsonEnrollment = new JSONObject();
            jsonEnrollments.put(jsonEnrollment);
            jsonEnrollment.put("projectName",enrollment.getProjectDetails().getProjectName());
            jsonEnrollment.put("id",enrollment.getProjectDetails().getProjectId());
        }
        return jsonEnrollments;
    }


    @Transactional
    public RepoDetails createRepoIfNotFound(String username, long projectId, final String name, String url, String branch, String creds) {
        ProjectDetails projectDetails = projectDetailsRepository.findByProjectId(projectId);
        String globalName = username + "/" + name;
        UserDetails userDetails = createOrFind(username);
        RepoDetails repoDetails = repoDetailsRepository.findByGlobalName(globalName);
        if (repoDetails == null) {
            repoDetails = new RepoDetails(globalName);
            repoDetails.setProjectDetails(projectDetails);
            repoDetails.setRepoName(name);
            repoDetails.setUrl(url);
            repoDetails.setBranch(branch);
            repoDetails.setCreds(creds);
            repoDetails = repoDetailsRepository.save(repoDetails);
        } else {
            repoDetails.setUrl(url);
            repoDetails.setBranch(branch);
            repoDetails.setCreds(creds);
            repoDetails = repoDetailsRepository.save(repoDetails);
        }
        return repoDetails;
    }

    @Transactional
    public boolean deleteRepo(String username, long projectId, String name) {
        String globalName = projectId + "/" + name;
        RepoDetails repoDetails = repoDetailsRepository.findByGlobalName(globalName);
        if (repoDetails == null) {
            return false;
        }
        repoDetailsRepository.delete(repoDetails);
        return true;
    }

    @Transactional
    public SimSpecs createSimSpecs(String username, long projectId, String name, String folder, String path, String type) {
        UserDetails userDetails = createOrFind(username);
        ProjectDetails projectDetails = projectDetailsRepository.findByProjectId(projectId);
        SimSpecs specs = new SimSpecs();
        specs.setProjectDetails(projectDetails);
        specs.setSimName(name);
        specs.setFolder(folder);
        specs.setPath(path);
        specs.setType(type);
        simSpecsRepository.save(specs);
        return specs;
    }

    @Transactional
    public int dropSims(String username, long projectId) {
        ProjectDetails projectDetails = projectDetailsRepository.findByProjectId(projectId);
        UserDetails userDetails = createOrFind(username);
        int size = projectDetails.getSimSpecs().size();
        projectDetails.getSimSpecs().clear();
        userDetailsRepository.save(userDetails);
        return size;
    }

    @Transactional
    public boolean dropSim(String username, long projectId, long simId) {
        ProjectDetails projectDetails = projectDetailsRepository.findByProjectId(projectId);
        SimSpecs specs = simSpecsRepository.findBySimId(simId);
        if (specs == null) {
            return false;
        }
       /* U/serDetails user = specs.getUserDetails();
        if (!username.equals(user.getName())) {
            return false;
        }*/
        simSpecsRepository.delete(specs);
        return true;
    }

    private SimSpecs validSimSpecs(String username, long projectId, long simId) {
        UserDetails userDetails = createOrFind(username);
        if (!enrollmentRepository.existsByUserDetailsUserIdAndProjectDetailsProjectId
                (userDetails.getUserId(), projectId)) {
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
                             String name, String folder, String path, String type) {
        SimSpecs specs = validSimSpecs(username, projectId, simId);
        if (specs == null){
            return false;
        }
        specs.setSimName(name);
        specs.setFolder(folder);
        specs.setPath(path);
        specs.setType(type);
        simSpecsRepository.save(specs);
        return true;
    }

    @Transactional
    public long addExec(String username,  long projectId, long simId) {
        SimSpecs specs = validSimSpecs(username, projectId, simId);
        if (specs == null){
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
        if (simSpecs == null){
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

