package org.abx.persistence.client;


import org.abx.persistence.client.dao.DashboardDetailsRepository;
import org.abx.persistence.client.dao.DashboardEnrollmentRepository;
import org.abx.persistence.client.dao.DashboardRepoRepository;
import org.abx.persistence.client.dao.RepoDetailsRepository;
import org.abx.persistence.client.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.abx.persistence.client.RepoPersistenceManager.Dashboard;
import static org.abx.persistence.client.RepoPersistenceManager.repoId;


@Component
public class DashboardPersistenceManager {

    @Autowired
    private DashboardEnrollmentRepository dashboardEnrollmentRepository;

    @Autowired
    private DashboardRepoRepository dashboardRepoRepository;

    @Autowired
    private UserPersistenceManager userPersistenceManager;

    @Autowired
    private DashboardDetailsRepository dashboardDetailsRepository;

    @Autowired
    private RepoDetailsRepository repoDetailsRepository;

    @Transactional
    public JSONArray getDashboards(String username) {
        UserDetails userDetails = userPersistenceManager.createOrFind(username);
        JSONArray jsonDashboards = new JSONArray();
        for (DashboardEnrollment dashboardEnrollment : userDetails.getDashboardEnrollments()) {
            JSONObject jsonDashboard = new JSONObject();
            jsonDashboards.put(jsonDashboard);
            DashboardDetails dashboardDetails = dashboardEnrollment.getDashboardDetails();
            jsonDashboard.put("dashboardId", dashboardDetails.getDashboardId());
            jsonDashboard.put("dashboardName", dashboardDetails.getDashboardName());
        }
        return jsonDashboards;
    }

    @Transactional
    public JSONObject getDashboard(long dashboardId, String username) {
        DashboardEnrollment dashboardEnrollment = dashboardEnrollmentRepository.findByDashboardDetailsDashboardIdAndUserDetailsUsername(dashboardId, username);
        if (dashboardEnrollment == null) {
            return null;
        }
        DashboardDetails dashboardDetails = dashboardEnrollment.getDashboardDetails();
        JSONObject jsonDashboard = new JSONObject();
        jsonDashboard.put("dashboardId", dashboardDetails.getDashboardId());
        jsonDashboard.put("dashboardName", dashboardDetails.getDashboardName());
        return jsonDashboard;
    }

    @Transactional
    public long createDashboard(String username, String dashboardName) {
        UserDetails userDetails = userPersistenceManager.createOrFind(username);
        DashboardDetails dd = new DashboardDetails();
        dd.setDashboardName(dashboardName);
        dashboardDetailsRepository.save(dd);
        DashboardEnrollment dashboardEnrollment = new DashboardEnrollment();
        dashboardEnrollment.setUserDetails(userDetails);
        dashboardEnrollment.setDashboardDetails(dd);
        dashboardEnrollment.setRole(ProjectRole.Owner.toString());
        dashboardEnrollmentRepository.save(dashboardEnrollment);
        return dd.getDashboardId();
    }


    /**
     * Deletes dashboard or enrollment
     *
     * @param dashboardId the dashboard enrollment
     * @param username
     * @return
     */
    @Transactional
    public boolean deleteDashboard(long dashboardId, String username) {
        DashboardEnrollment dashboardEnrollment = dashboardEnrollmentRepository.findByDashboardDetailsDashboardIdAndUserDetailsUsername(dashboardId, username);
        if (dashboardEnrollment == null) {
            return false;
        }
        return deleteDashboardByEnrollment(dashboardEnrollment.getDashboardEnrollmentId());
    }

    private boolean deleteDashboardByEnrollment(long dashboardEnrollmentId) {
        DashboardEnrollment dashboardEnrollment = dashboardEnrollmentRepository.findByDashboardEnrollmentId(dashboardEnrollmentId);
        if (dashboardEnrollment == null) {
            return false;
        }
        if (dashboardEnrollment.getRole().equals(ProjectRole.Owner.toString())) {
            DashboardDetails dashboardDetails = dashboardEnrollment.getDashboardDetails();
            dashboardDetailsRepository.delete(dashboardDetails);
        } else {
            dashboardEnrollmentRepository.delete(dashboardEnrollment);
        }
        return true;

    }

    @Transactional
    public RepoDetails createDashboardRepoIfNotFound(String username, long dashboardId, final String repoName, String url, String branch, String creds) {
        DashboardEnrollment dashboardEnrollment = dashboardEnrollmentRepository.findByDashboardDetailsDashboardIdAndUserDetailsUsername(dashboardId, username);
        if (dashboardEnrollment == null) {
            return null;
        }
        DashboardDetails dashboardDetails = dashboardEnrollment.getDashboardDetails();
        long repoId = repoId(Dashboard, dashboardId, repoName);
        RepoDetails repoDetails = repoDetailsRepository.findByRepoId(repoId);
        if (repoDetails == null) {
            repoDetails = new RepoDetails();
            repoDetails.setRepoId(repoId);
            repoDetails.setRepoName(repoName);
            repoDetails.setUrl(url);
            repoDetails.setBranch(branch);
            repoDetails.setCreds(creds);
            repoDetails = repoDetailsRepository.save(repoDetails);

            DashboardRepo projectRepo = new DashboardRepo();
            projectRepo.setDashboardRepoId(repoId);
            projectRepo.setDashboardDetails(dashboardDetails);
            projectRepo.setRepoDetails(repoDetails);
            dashboardRepoRepository.save(projectRepo);
        } else {
            repoDetails.setUrl(url);
            repoDetails.setBranch(branch);
            repoDetails.setCreds(creds);
            repoDetails = repoDetailsRepository.save(repoDetails);

        }
        return repoDetails;
    }

    @Transactional
    public boolean purgeDashboards(String username) {
        UserDetails userDetails = userPersistenceManager.createOrFind(username);
        for (DashboardEnrollment dashboardEnrollment : userDetails.getDashboardEnrollments()) {
            deleteDashboardByEnrollment(dashboardEnrollment.getDashboardEnrollmentId());
        }
        return true;
    }
}

