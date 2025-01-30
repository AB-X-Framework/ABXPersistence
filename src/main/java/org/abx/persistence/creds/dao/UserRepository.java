package org.abx.persistence.creds.dao;


import org.abx.persistence.creds.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Override
    void delete(User user);

}
