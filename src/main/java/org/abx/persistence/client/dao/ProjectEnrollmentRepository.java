package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.ProjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectEnrollmentRepository extends JpaRepository<ProjectEnrollment, Long> {
    ProjectEnrollment findByProjectEnrollmentId(long projectEnrollmentId);

    @Override
    void delete(ProjectEnrollment projectEnrollment);

    boolean existsByUserDetailsUserIdAndProjectDetailsProjectId(Long userId, Long projectId);
}
