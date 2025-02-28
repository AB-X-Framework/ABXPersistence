package org.abx.persistence.client;

import org.abx.persistence.client.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepoPersistenceManager {
    public final static String Project = "Project";
    public final static String Dashboard = "Dashboard";
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private ProjectEnrollmentRepository projectEnrollmentRepository;



    @Autowired
    private ExecDetailsRepository execDetailsRepository;

    @Autowired
    private RepoDetailsRepository repoDetailsRepository;

    @Autowired
    private SimSpecsRepository simSpecsRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private UserPersistenceManager userPersistenceManager;

    @Autowired
    private ProjectRepoRepository projectRepoRepository;
}
