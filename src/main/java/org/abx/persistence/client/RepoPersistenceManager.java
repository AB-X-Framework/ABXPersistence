package org.abx.persistence.client;

import org.abx.persistence.client.dao.*;
import org.abx.persistence.client.model.ProjectDetails;
import org.abx.persistence.client.model.ProjectRepo;
import org.abx.persistence.client.model.RepoDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RepoPersistenceManager {
    public final static String Project = "Project";
    public final static String Dashboard = "Dashboard";

    @Autowired
    private ProjectEnrollmentRepository projectEnrollmentRepository;

    @Autowired
    private RepoDetailsRepository repoDetailsRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private ProjectRepoRepository projectRepoRepository;


    public static long repoId(String type, long projectId, String repoName) {
        return (type + "/" + projectId + "/" + repoName).hashCode();
    }

    /**
     * Creates a repository if it does not exists
     * @param username The username
     * @param projectId The project id
     * @param name The repo name
     * @param type The repo type
     * @param url The repo url
     * @param branch The repo branch
     * @param creds The creds
     * @return true if the repo was created/updated
     */
    @Transactional
    public boolean createProjectRepoIfNotFound(String username, long projectId, final String name,
                                               String type, String url, String branch, String creds) {
        if (!projectEnrollmentRepository.existsByUserDetailsUsernameAndProjectDetailsProjectId
                (username, projectId)) {
            return false;
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
            repoDetails.setEngine(type);
            repoDetailsRepository.save(repoDetails);

            ProjectRepo projectRepo = new ProjectRepo();
            projectRepo.setProjectRepoId(repoId);
            projectRepo.setProjectDetails(projectDetails);
            projectRepo.setRepoDetails(repoDetails);
            projectRepoRepository.save(projectRepo);
        } else {
            repoDetails.setUrl(url);
            repoDetails.setBranch(branch);
            repoDetails.setCreds(creds);
            repoDetails.setEngine(type);
            repoDetailsRepository.save(repoDetails);
        }
        return true;
    }

    @Transactional
    public boolean deleteRepo(String type, String username, long projectId, String name) {
        if (!projectEnrollmentRepository.existsByUserDetailsUsernameAndProjectDetailsProjectId
                (username, projectId)) {
            return false;
        }
        long repoId = repoId(type, projectId, name);
        ProjectRepo projectRepo = projectRepoRepository.findByProjectRepoId(repoId);
        if (projectRepo == null) {
            return false;
        }
        projectRepoRepository.delete(projectRepo);
        RepoDetails repoDetails = repoDetailsRepository.findByRepoId(repoId);
        if (repoDetails == null) {
            return false;
        }
        repoDetailsRepository.delete(repoDetails);
        return true;
    }
}
