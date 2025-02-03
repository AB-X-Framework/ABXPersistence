package org.abx.persistence.repository.dao;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import org.abx.persistence.repository.model.RepoDetails;
import org.abx.persistence.repository.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserDetailsRepository extends JpaRepository<UserDetail, Long> {

    UserDetail findByName(String name);

    @Override
    void delete(UserDetail userDetail);


}
