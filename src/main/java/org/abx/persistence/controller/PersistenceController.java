package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.RepositoryDataLoader;
import org.abx.persistence.client.model.RepoDetails;
import org.abx.persistence.client.model.UserDetails;
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
        UserDetails details = dataLoader.createUserIfNotFound(username);

        return details.getName();
    }

    @Secured("persistence")
    @RequestMapping(value = "/newRepo")
    public String addRepo(HttpServletRequest request, @RequestParam String name,
                          @RequestParam String url,
                          @RequestParam String branch,
                          @RequestParam String creds) {
        String username = request.getUserPrincipal().getName();
        UserDetails userDetails = dataLoader.createUserIfNotFound(username);
        RepoDetails repoDetails= dataLoader.createRepoIfNotFound(userDetails,name,url,branch,creds);
        return repoDetails.getName();
    }
}
