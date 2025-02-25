package org.abx.persistence.client;

import org.abx.persistence.client.dao.ProjectEnrollmentRepository;
import org.abx.persistence.client.dao.ProjectDetailsRepository;
import org.abx.persistence.client.dao.UserDetailsRepository;
import org.abx.persistence.client.model.ProjectEnrollment;
import org.abx.persistence.client.model.ProjectDetails;
import org.abx.persistence.client.model.ProjectRole;
import org.abx.persistence.client.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoaderUtils {
    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private ProjectEnrollmentRepository projectEnrollmentRepository;

    public UserDetails createOrFind(final String name) {
        UserDetails userDetails = userDetailsRepository.findByUsername(name);
        if (userDetails == null) {
            userDetails = new UserDetails(name);
            userDetails = userDetailsRepository.save(userDetails);
            ProjectDetails projectDetails = new ProjectDetails();
            projectDetails.setProjectName("Personal");
            projectDetailsRepository.save(projectDetails);
            ProjectEnrollment projectEnrollment = new ProjectEnrollment();
            projectEnrollment.setProjectDetails(projectDetails);
            projectEnrollment.setUserDetails(userDetails);
            projectEnrollment.setRole(ProjectRole.Owner.name());
            projectEnrollmentRepository.save(projectEnrollment);
            userDetails.setEnrollments(List.of(projectEnrollment));
            userDetails.setDashboardDetails(new ArrayList<>());
        }
        return userDetails;
    }
}
