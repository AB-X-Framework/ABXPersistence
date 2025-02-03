package org.abx.persistence.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/persistence")
public class PersistenceController {

    @Secured("persistence")
    @RequestMapping(value = "/user")
    public String user(HttpServletRequest request) {

        String username = request.getUserPrincipal().getName();
        return username;
    }


}
