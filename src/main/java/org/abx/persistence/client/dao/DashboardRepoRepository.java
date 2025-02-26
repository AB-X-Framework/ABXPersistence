package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.DashboardRepo;
import org.abx.persistence.client.model.ProjectRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardRepoRepository extends JpaRepository<DashboardRepo, Long> {

    public DashboardRepo findByDashboardRepoId(long id);
}
