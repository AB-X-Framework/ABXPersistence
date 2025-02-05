package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.ProjectDetails;
import org.abx.persistence.client.model.RepoDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDetailsRepository extends JpaRepository<ProjectDetails, Long> {

    ProjectDetails findByProjectId(long projectDetailsId);

    @Override
    void delete(ProjectDetails projectDetails);
}
