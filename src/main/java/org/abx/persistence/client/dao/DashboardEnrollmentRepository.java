package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.DashboardEnrollment;
import org.abx.persistence.client.model.ProjectEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardEnrollmentRepository extends JpaRepository<DashboardEnrollment, Long> {
    DashboardEnrollment findByDashboardEnrollmentId(long dashboardEnrollmentId);

    @Override
    void delete(DashboardEnrollment projectEnrollment);

    DashboardEnrollment findByDashboardDetailsDashboardIdAndUserDetailsUsername(long dashboardId, String userName);

    boolean existsByUserDetailsUserIdAndDashboardDetailsDashboardId(Long userId, Long projectId);
}
