package org.abx.persistence.client;


import org.abx.persistence.client.dao.DashboardDetailsRepository;
import org.abx.persistence.client.dao.DashboardEnrollmentRepository;
import org.abx.persistence.client.dao.UserDetailsRepository;
import org.abx.persistence.client.model.DashboardDetails;
import org.abx.persistence.client.model.DashboardEnrollment;
import org.abx.persistence.client.model.ProjectRole;
import org.abx.persistence.client.model.UserDetails;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DashboardDataLoader {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private DataLoaderUtils dataLoaderUtils;
    @Autowired
    private DashboardDetailsRepository dashboardDetailsRepository;
    @Autowired
    private DashboardEnrollmentRepository dashboardEnrollmentRepository;

    @Transactional
    public JSONArray getDashboards(String username) {
        UserDetails userDetails = dataLoaderUtils.createOrFind(username);
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
        DashboardEnrollment dashboardEnrollment = dashboardEnrollmentRepository.findByDashboardDetailsDashboardIdAndUserDetailsUsername(dashboardId,username);
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
        UserDetails userDetails = dataLoaderUtils.createOrFind(username);
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
     * @param dashboardEnrollmentId the dashboard enrollment
     * @param username
     * @return
     */
    @Transactional
    public boolean deleteDashboard(long dashboardId,String username) {
        DashboardEnrollment dashboardEnrollment = dashboardEnrollmentRepository.findByDashboardDetailsDashboardIdAndUserDetailsUsername(dashboardId,username);
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
        if (dashboardEnrollment.getRole().equals( ProjectRole.Owner.toString())) {
            DashboardDetails dashboardDetails = dashboardEnrollment.getDashboardDetails();
            dashboardDetailsRepository.delete(dashboardDetails);
        }else {
            dashboardEnrollmentRepository.delete(dashboardEnrollment);
        }
        return true;

    }

    @Transactional
    public boolean purgeDashboards(String username) {
        UserDetails userDetails = dataLoaderUtils.createOrFind(username);
        for (DashboardEnrollment dashboardEnrollment : userDetails.getDashboardEnrollments()) {
            deleteDashboardByEnrollment(dashboardEnrollment.getDashboardEnrollmentId());
        }
        return true;
    }
}

