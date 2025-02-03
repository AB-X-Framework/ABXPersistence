package org.abx.persistence.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.abx.persistence.repository.RepositoryDataLoader;
import org.abx.persistence.repository.model.UserDetail;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
