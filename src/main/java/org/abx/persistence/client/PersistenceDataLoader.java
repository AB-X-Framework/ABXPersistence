package org.abx.persistence.client;

import org.abx.persistence.client.dao.RepoDetailsRepository;
import org.abx.persistence.client.dao.SimSpecsRepository;
import org.abx.persistence.client.dao.UserDetailsRepository;
import org.abx.persistence.client.model.RepoDetails;
import org.abx.persistence.client.model.SimSpecs;
import org.abx.persistence.client.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PersistenceDataLoader {

    @Autowired
    private UserDetailsRepository userDetailsRepository;


    @Autowired
    private RepoDetailsRepository repoDetailsRepository;

    @Autowired
    private SimSpecsRepository simSpecsRepository;

    private UserDetails createOrFind(final String name) {
        UserDetails userDetails = userDetailsRepository.findByName(name);
        if (userDetails == null) {
            userDetails = new UserDetails(name);
            userDetails = userDetailsRepository.save(userDetails);
        }
        return userDetails;
    }

    @Transactional
    public UserDetails createUserIfNotFound(final String name) {
        return createOrFind(name);
    }


    @Transactional
    public RepoDetails createRepoIfNotFound(String username, final String name, String url,
                                            String branch, String creds) {
        String globalName = username + "/" + name;
        UserDetails userDetails = createOrFind(username);
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
    public boolean deleteRepo(String username, final String name) {
        String globalName = username + "/" + name;
        RepoDetails repoDetails = repoDetailsRepository.findByGlobalName(globalName);
        if (repoDetails == null) {
            return false;
        }
        repoDetailsRepository.delete(repoDetails);
        return true;
    }

    @Transactional
    public SimSpecs createSimSpecs(String username, String name, String folder, String path, String type) {
        UserDetails userDetails = createOrFind(username);
        SimSpecs specs = new SimSpecs();
        specs.setUserDetails(userDetails);
        specs.setName(name);
        specs.setFolder(folder);
        specs.setPath(path);
        specs.setType(type);
        simSpecsRepository.save(specs);
        return specs;
    }

    @Transactional
    public int dropSims(String username) {
        UserDetails userDetails = createOrFind(username);
        int size = userDetails.getSimSpecs().size();
        userDetails.getSimSpecs().clear();
        userDetailsRepository.save(userDetails);
        return size;
    }

    @Transactional
    public boolean dropSim(String username, long simId) {
        SimSpecs specs = simSpecsRepository.findBySimId(simId);
        if (specs == null){
            return false;
        }
        UserDetails user = specs.getUserDetails();
        if (!username.equals(user.getName())){
            return false;
        }
        simSpecsRepository.delete(specs);
        return true;
    }
}

