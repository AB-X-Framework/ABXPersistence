package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.RepositoryDataLoader;
import org.abx.persistence.client.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persistence")
public class PersistenceController {

    @Autowired
    private RepositoryDataLoader dataLoader;

    @Secured("persistence")
    @RequestMapping(value = "/user")
    public String user(HttpServletRequest request) {

        String username = request.getUserPrincipal().getName();
        UserDetail details = dataLoader.createUserIfNotFound(username);

        return details.getName();
    }


}
