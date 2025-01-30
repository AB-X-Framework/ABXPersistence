package org.abx.persistence.repository.dao;

import org.abx.persistence.repository.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetail, Long> {

    UserDetail findByName(String name);

    @Override
    void delete(UserDetail userDetail);

}
