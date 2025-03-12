package org.abx.persistence.client.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RepoDetails")
public class RepoDetails {

    @Id
    @Column(unique = true, nullable = false)
    private Long repoId;

    @Column(length = 10)
    private String engine;

    @Column(length = 100)
    private String repoName;


    @Column
    private String url;

    @Column(length = 100)
    private String branch;

    @Column(length = 1000)
    private String creds;

    public RepoDetails() {
        super();
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(final String name) {
        this.repoName = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCreds() {
        return creds;
    }

    public void setCreds(String creds) {
        this.creds = creds;
    }

    public Long getRepoId() {
        return repoId;
    }

    public void setRepoId(Long repoId) {
        this.repoId = repoId;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String repoType) {
        this.engine = repoType;
    }
}
