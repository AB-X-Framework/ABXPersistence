package org.abx.persistence.client;

import org.abx.persistence.client.dao.ExecDetailsRepository;
import org.abx.persistence.client.dao.RepoDetailsRepository;
import org.abx.persistence.client.dao.SimSpecsRepository;
import org.abx.persistence.client.dao.UserDetailsRepository;
import org.abx.persistence.client.model.ExecDetails;
import org.abx.persistence.client.model.RepoDetails;
import org.abx.persistence.client.model.SimSpecs;
import org.abx.persistence.client.model.UserDetails;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PersistenceDataLoader {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private ExecDetailsRepository execDetailsRepository;

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
    public RepoDetails createRepoIfNotFound(String username, final String name, String url, String branch, String creds) {
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
        if (specs == null) {
            return false;
        }
        UserDetails user = specs.getUserDetails();
        if (!username.equals(user.getName())) {
            return false;
        }
        simSpecsRepository.delete(specs);
        return true;
    }

    @Transactional
    public boolean updateSim(long id, String username, String name, String folder, String path, String type) {
        UserDetails userDetails = createOrFind(username);
        SimSpecs specs = simSpecsRepository.findBySimId(id);
        if (specs == null) {
            return false;
        }
        if (!username.equals(specs.getUserDetails().getName())) {
            return false;
        }
        specs.setUserDetails(userDetails);
        specs.setName(name);
        specs.setFolder(folder);
        specs.setPath(path);
        specs.setType(type);
        simSpecsRepository.save(specs);
        return true;
    }

    @Transactional
    public long addExec( String username, long simId) {
        UserDetails userDetails = createOrFind(username);
        SimSpecs specs = simSpecsRepository.findBySimId(simId);
        if (specs == null) {
           return -1;
        }
        if (!username.equals(specs.getUserDetails().getName())) {
            return -1;
        }
        ExecDetails execDetails = new ExecDetails();
        execDetails.setName(specs.getName());
        execDetails.setPath(specs.getPath());
        execDetails.setFolder(specs.getFolder());
        execDetails.setUserDetails(userDetails);
        execDetails.setSimSpecs(specs);
        execDetails.setOutput("");
        return execDetailsRepository.save(execDetails).getExecId();
    }

    @Transactional
    public JSONObject getExec(String username, long simId, long execId) throws Exception{
        ExecDetails execDetails = execDetailsRepository.findByExecId(execId);
        if (execDetails == null) {
            return null;
        }
        if (!username.equals(execDetails.getUserDetails().getName())) {
            return null;
        }
        if (simId != execDetails.getSimSpecs().getSimId()) {
            return null;
        }
        JSONObject exec = new JSONObject();
        exec.put("name",execDetails.getName());
        exec.put("path",execDetails.getPath());
        exec.put("folder",execDetails.getFolder());
        exec.put("type",execDetails.getType());
        exec.put("output",execDetails.getOutput());
        return exec;
    }
}

