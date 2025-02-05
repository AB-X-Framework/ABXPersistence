package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Enrollment findByUserProjectId(long userProjectId);

    @Override
    void delete(Enrollment enrollment);

    boolean existsByUserDetailsUserIdAndProjectDetailsProjectId(Long userId, Long projectId);
}
