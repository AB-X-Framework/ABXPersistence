package org.abx.persistence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.abx.persistence.client.DashboardPersistenceManager;
import org.abx.persistence.client.RepoPersistenceManager;
import org.abx.persistence.client.model.RepoDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static org.abx.persistence.client.RepoPersistenceManager.Dashboard;

@RestController
@RequestMapping("/persistence")
public class DashboardPersistenceController {

    @Autowired
    private RepoPersistenceManager repoPersistenceManager;

    @Autowired
    private DashboardPersistenceManager dashboardPersistenceManager;

    @Secured("Persistence")
    @GetMapping(value = "/dashboards", produces = MediaType.APPLICATION_JSON_VALUE)
    public String dashboards(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return dashboardPersistenceManager.getDashboards(username).toString();
    }

    @Secured("Persistence")
    @GetMapping(value = "/dashboards/{dashboardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDashboard(HttpServletRequest request,
                               @PathVariable long dashboardId) {
        String username = request.getUserPrincipal().getName();
        return dashboardPersistenceManager.getDashboard(dashboardId, username).toString();
    }

    @Secured("Persistence")
    @PostMapping(value = "/dashboards", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long createDashboard(HttpServletRequest request,
                                @RequestParam String dashboardName) {
        String username = request.getUserPrincipal().getName();
        return dashboardPersistenceManager.createDashboard(username, dashboardName);
    }

    @Secured("Persistence")
    @DeleteMapping(value = "/dashboards/{dashboardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteDashboard(HttpServletRequest request,
                                   @PathVariable long dashboardId) {
        String username = request.getUserPrincipal().getName();
        return dashboardPersistenceManager.deleteDashboard(dashboardId, username);
    }

    @Secured("Persistence")
    @DeleteMapping(value = "/dashboards", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean deleteDashboard(HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        return dashboardPersistenceManager.purgeDashboards(username);
    }

    @Secured("Persistence")
    @PostMapping(value = "/dashboards/{dashboardId}/repo")
    public String addDashboardRepo(HttpServletRequest request,
                                   @PathVariable long dashboardId,
                                   @RequestParam String repoName,
                                   @RequestParam String url,
                                   @RequestParam String branch,
                                   @RequestParam String creds) {
        String username = request.getUserPrincipal().getName();
        RepoDetails repoDetails = dashboardPersistenceManager.createDashboardRepoIfNotFound(username, dashboardId, repoName, url, branch, creds);
        return repoDetails.getRepoName();
    }


    @Secured("Persistence")
    @DeleteMapping(value = "/dashboards/{dashboardId}/repo/{repoName}")
    public boolean deleteDashboardRepo(HttpServletRequest request,
                                       @PathVariable long dashboardId,
                                       @PathVariable String repoName) {
        String username = request.getUserPrincipal().getName();
        return repoPersistenceManager.deleteRepo(Dashboard, username, dashboardId, repoName);
    }
}
