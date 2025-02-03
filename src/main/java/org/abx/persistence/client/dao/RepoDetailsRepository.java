package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.RepoDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoDetailsRepository extends JpaRepository<RepoDetails, Long> {

    @Override
    void delete(RepoDetails repoDetails);

}
