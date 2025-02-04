package org.abx.persistence.client.model;

import jakarta.persistence.*;

@Entity
@Table(name = "repodetails")
public class RepoDetails {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long repo_id;


    @ManyToOne
    @JoinTable(name = "user_repositories",
            joinColumns    = @JoinColumn(name = "repo_id", referencedColumnName = "repo_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private UserDetails userDetails;

    @Column(unique = true)
    private String globalName;

    @Column
    private String name;
    @Column
    private String url;
    @Column
    private String branch;
    @Column
    private String creds;

    public RepoDetails() {
        super();
    }

    public RepoDetails(final String globalName) {
        super();
        this.globalName = globalName;
    }

    
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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

    public String getGlobalName() {
        return globalName;
    }

    public void setGlobalName(String globalName) {
        this.globalName = globalName;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
