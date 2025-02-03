package org.abx.persistence.repository.dao;

import org.abx.persistence.repository.model.RepoDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoDetailsRepository extends JpaRepository<RepoDetails, Long> {

    @Override
    void delete(RepoDetails repoDetails);

}
