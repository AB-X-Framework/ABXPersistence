package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetail, Long> {

    UserDetail findByName(String name);

    @Override
    void delete(UserDetail userDetail);


}
