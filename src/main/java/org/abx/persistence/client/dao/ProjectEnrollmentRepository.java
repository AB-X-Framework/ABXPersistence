package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.ProjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectEnrollmentRepository extends JpaRepository<ProjectEnrollment, Long> {
    ProjectEnrollment findByProjectEnrollmentId(long projectEnrollmentId);

    ProjectEnrollment findByProjectDetailsProjectIdAndUserDetailsUsername(long projectId,String username);
    @Override
    void delete(ProjectEnrollment projectEnrollment);

    boolean existsByUserDetailsUsernameAndProjectDetailsProjectId(String username, Long projectId);
}
