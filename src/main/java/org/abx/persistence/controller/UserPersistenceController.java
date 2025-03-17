package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.DashboardPersistenceManager;
import org.abx.persistence.client.ProjectPersistenceManager;
import org.abx.persistence.client.UserPersistenceManager;
import org.abx.persistence.client.model.UserDetails;
import org.abx.spring.ErrorMessage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persistence")
public class UserPersistenceController {
    @Autowired
    private UserPersistenceManager userPersistenceManager;


    @Autowired
    private ProjectPersistenceManager projectPersistenceManager;

    @Autowired
    private DashboardPersistenceManager dashboardPersistenceManager;
    @Secured("Persistence")
    @RequestMapping(value = "/user")
    public String user(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        UserDetails details = userPersistenceManager.createUserIfNotFound(username);
        return details.getUsername();
    }

    @RequestMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({"UseABX"})
    public String menu(final HttpServletRequest request) throws Exception{
        try {
            String user = request.getUserPrincipal().getName();
            JSONObject jsonMenu = new JSONObject();
            jsonMenu.put("dashboards",dashboardPersistenceManager.getDashboards(user));
            jsonMenu.put("projects",projectPersistenceManager.projectEnrollments(user));
            return jsonMenu.toString();
        }catch (Exception e){
            return ErrorMessage.errorString("Cannot get user information");
        }
    }
}
