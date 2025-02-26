package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.ProjectEnrollment;
import org.abx.persistence.client.model.ProjectRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepoRepository extends JpaRepository<ProjectRepo, Long> {

    public ProjectRepo findByProjectRepoId(long id);
}
