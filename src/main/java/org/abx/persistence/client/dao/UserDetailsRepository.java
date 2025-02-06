package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    UserDetails findByUsername(String name);

    @Override
    void delete(UserDetails userDetails);


}
