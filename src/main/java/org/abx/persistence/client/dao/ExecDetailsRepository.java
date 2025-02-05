package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.ExecDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ExecDetailsRepository extends JpaRepository<ExecDetails, Long> {

    ExecDetails findByExecId(long execId);

    @Override
    void delete(ExecDetails execDetails);

    List<ExecDetails> findByUserDetailsUserIdOrderByExecIdDesc(Long exec, Pageable pageable);
}
