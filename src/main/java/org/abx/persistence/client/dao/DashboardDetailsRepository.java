package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.DashboardDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardDetailsRepository  extends JpaRepository<DashboardDetails, Long> {
    @Override
    void delete(DashboardDetails dashboardDetails);

    DashboardDetails findByDashboardId(long dashboardId);

}
