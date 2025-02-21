package org.abx.persistence.client;

import org.abx.persistence.client.dao.EnrollmentRepository;
import org.abx.persistence.client.dao.ProjectDetailsRepository;
import org.abx.persistence.client.dao.UserDetailsRepository;
import org.abx.persistence.client.model.Enrollment;
import org.abx.persistence.client.model.ProjectDetails;
import org.abx.persistence.client.model.ProjectRole;
import org.abx.persistence.client.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoaderUtils {
    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public UserDetails createOrFind(final String name) {
        UserDetails userDetails = userDetailsRepository.findByUsername(name);
        if (userDetails == null) {
            userDetails = new UserDetails(name);
            userDetails = userDetailsRepository.save(userDetails);
            ProjectDetails projectDetails = new ProjectDetails();
            projectDetails.setProjectName("Personal");
            projectDetailsRepository.save(projectDetails);
            Enrollment enrollment = new Enrollment();
            enrollment.setProjectDetails(projectDetails);
            enrollment.setUserDetails(userDetails);
            enrollment.setRole(ProjectRole.Owner.name());
            enrollmentRepository.save(enrollment);
            userDetails.setEnrollments(List.of(enrollment));
        }
        return userDetails;
    }
}
