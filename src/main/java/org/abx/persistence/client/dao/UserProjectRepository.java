package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    UserProject findByUserProjectId(long userProjectId);

    @Override
    void delete(UserProject userProject);
}
