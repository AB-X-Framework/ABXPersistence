package org.abx.persistence.client;

import org.abx.persistence.client.dao.UserDetailsRepository;
import org.abx.persistence.client.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RepositoryDataLoader {

    @Autowired
    private UserDetailsRepository userRepository;




    @Transactional(transactionManager = "persistenceTransactionManager")
    public UserDetail createUserIfNotFound(final String name) {
        UserDetail userDetail = userRepository.findByName(name);
        if (userDetail == null) {
            userDetail = new UserDetail(name);
        }
        userDetail = userRepository.save(userDetail);
        return userDetail;
    }

}

