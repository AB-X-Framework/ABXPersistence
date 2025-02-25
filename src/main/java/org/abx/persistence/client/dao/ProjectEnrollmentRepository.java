package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.ProjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectEnrollmentRepository extends JpaRepository<ProjectEnrollment, Long> {
    ProjectEnrollment findByProjectEnrollmentId(long projectEnrollmentId);

    ProjectEnrollment findByProjectDetailsProjectIdAndUserDetailsUsername(long projectEnrollmentId,String username);
    @Override
    void delete(ProjectEnrollment projectEnrollment);

    boolean existsByUserDetailsUserIdAndProjectDetailsProjectId(Long userId, Long projectId);
}
