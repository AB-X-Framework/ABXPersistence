package org.abx.persistence.client;

import org.abx.persistence.client.dao.RepoDetailsRepository;
import org.abx.persistence.client.dao.UserDetailsRepository;
import org.abx.persistence.client.model.RepoDetails;
import org.abx.persistence.client.model.UserDetails;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Component
public class PersistenceDataLoader {

    @Autowired
    private UserDetailsRepository userDetailsRepository;


    @Autowired
    private RepoDetailsRepository repoDetailsRepository;


    @Transactional
    public UserDetails createUserIfNotFound(final String name) {
        UserDetails userDetails = userDetailsRepository.findByName(name);
        if (userDetails == null) {
            userDetails = new UserDetails(name);
            userDetails = userDetailsRepository.save(userDetails);
        }
        return userDetails;
    }


    @Transactional
    public RepoDetails createRepoIfNotFound(UserDetails userDetails, final String name, String url,
                                            String branch, String creds) {
        String globalName = userDetails.getName() + "/" + name;
        RepoDetails repoDetails = repoDetailsRepository.findByGlobalName(globalName);
        if (repoDetails == null) {
            repoDetails = new RepoDetails(globalName);
            repoDetails.setUserDetails(userDetails);
            repoDetails.setName(name);
            repoDetails.setUrl(url);
            repoDetails.setBranch(branch);
            repoDetails.setCreds(creds);
            repoDetails = repoDetailsRepository.save(repoDetails);
        } else {
            repoDetails.setUrl(url);
            repoDetails.setBranch(branch);
            repoDetails.setCreds(creds);
            repoDetails = repoDetailsRepository.save(repoDetails);
        }
        return repoDetails;
    }


    @Transactional
    public boolean deleteRepo(UserDetails userDetails, final String name) {
        String globalName = userDetails.getName() + "/" + name;
        RepoDetails repoDetails = repoDetailsRepository.findByGlobalName(globalName);
        if (repoDetails == null) {
            return false;
        }
        repoDetailsRepository.delete(repoDetails);
        return true;
    }
}

