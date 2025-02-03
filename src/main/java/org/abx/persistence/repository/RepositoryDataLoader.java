package org.abx.persistence.repository;

import org.abx.persistence.repository.dao.UserDetailsRepository;
import org.abx.persistence.repository.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Component
public class RepositoryDataLoader {

    @Autowired
    private UserDetailsRepository userRepository;




    @Transactional(transactionManager = "repositoryTransactionManager")
    public UserDetail createUserIfNotFound(final String name) {
        UserDetail userDetail = userRepository.findByName(name);
        if (userDetail == null) {
            userDetail = new UserDetail(name);
        }
        userDetail = userRepository.save(userDetail);
        return userDetail;
    }

}

