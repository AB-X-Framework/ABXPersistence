package org.abx.persistence.client.dao;

import org.abx.persistence.client.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    UserDetails findByName(String name);

    @Override
    void delete(UserDetails userDetails);


}
