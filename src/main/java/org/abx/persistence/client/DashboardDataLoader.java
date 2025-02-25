package org.abx.persistence.client;


import org.abx.persistence.client.dao.DashboardDetailsRepository;
import org.abx.persistence.client.dao.UserDetailsRepository;
import org.abx.persistence.client.model.DashboardDetails;
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

    @Transactional
    public JSONArray getDashboards(String username) {
        UserDetails userDetails = dataLoaderUtils.createOrFind(username);
        JSONArray jsonDashboards = new JSONArray();
        for (DashboardDetails dashboardDetails : userDetails.getDashboardDetails()) {
            JSONObject jsonDashboard = new JSONObject();
            jsonDashboards.put(jsonDashboard);
            jsonDashboard.put("id", dashboardDetails.getDashboardId());
            jsonDashboard.put("name", dashboardDetails.getDashboardName());
        }
        return jsonDashboards;
    }

    @Transactional
    public JSONObject getDashboard(long dashboardId, String username) {
        DashboardDetails dashboardDetails = dashboardDetailsRepository.findByDashboardId(dashboardId);
        if (dashboardDetails == null) {
            return null;
        }
        UserDetails userDetails = dashboardDetails.getUserDetails();
        if (!username.equals(userDetails.getUsername())) {
            return null;
        }
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
        dd.setUserDetails(userDetails);
        dashboardDetailsRepository.save(dd);
        return dd.getDashboardId();
    }


    @Transactional
    public boolean deleteDashboard(long dashboardId,String username) {
        DashboardDetails dashboardDetails = dashboardDetailsRepository.findByDashboardId(dashboardId);
        if (dashboardDetails == null) {
            return false;
        }
        UserDetails userDetails = dashboardDetails.getUserDetails();
        if (!username.equals(userDetails.getUsername())) {
            return false;
        }
        dashboardDetailsRepository.delete(dashboardDetails);
        return true;

    }


    @Transactional
    public boolean purgeDashboards(String username) {
        UserDetails userDetails = dataLoaderUtils.createOrFind(username);
        dashboardDetailsRepository.deleteDashboardDetailsByUserDetailsUserId(userDetails.getUserId());
        return true;
    }
}

