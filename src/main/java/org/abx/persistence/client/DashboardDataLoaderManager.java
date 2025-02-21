package org.abx.persistence.client;


import org.abx.persistence.client.dao.UserDetailsRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DashboardDataLoaderManager {

    @Autowired
    private UserDetailsRepository userDetailsRepository;



}

