package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.SimSpecs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimSpecsRepository extends JpaRepository<SimSpecs, Long> {

    @Override
    void delete(SimSpecs userDetails);


}
