package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.DashboardPersistenceManager;
import org.abx.persistence.client.ProjectPersistenceManager;
import org.abx.persistence.client.RepoPersistenceManager;
import org.abx.persistence.client.UserPersistenceManager;
import org.abx.persistence.client.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persistence")
public class UserPersistenceController {
    @Autowired
    private UserPersistenceManager userPersistenceManager;

    @Autowired
    private DashboardPersistenceManager dashboardPersistenceManager;
    @Secured("Persistence")
    @RequestMapping(value = "/user")
    public String user(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        UserDetails details = userPersistenceManager.createUserIfNotFound(username);
        return details.getUsername();
    }

}
