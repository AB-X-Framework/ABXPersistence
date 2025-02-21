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
public class DashboardDataLoaderManager {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private DataLoaderUtils dataLoaderUtils;
    @Autowired
    private DashboardDetailsRepository dashboardDetailsRepository;

    @Transactional
    public JSONArray getDashboards(String username){
        UserDetails userDetails = dataLoaderUtils.createOrFind(username);
        JSONArray jsonDashboards  = new JSONArray();
        for (DashboardDetails dashboardDetails: userDetails.getDashboardDetails()){
            JSONObject jsonDashboard = new JSONObject();
            jsonDashboards.put(jsonDashboard);
            jsonDashboard.put("id",dashboardDetails.getDashboardId());
            jsonDashboard.put("name",dashboardDetails.getDashboardName());
        }
        return jsonDashboards;
    }

    @Transactional
    public long createDashboard(String username, String name){
        UserDetails userDetails = dataLoaderUtils.createOrFind(username);
        DashboardDetails dd = new DashboardDetails();
        dd.setDashboardName(name);
        dashboardDetailsRepository.save(dd);
        return dd.getDashboardId();
    }


    @Transactional
    public boolean deleteDashboard(String username, long id){


    }


}

