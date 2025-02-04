package org.abx.persistence.client.model;

import jakarta.persistence.*;

@Entity
@Table(name = "repodetails")
public class RepoDetails {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long repoId;


    @ManyToOne
    @JoinTable(name = "userRepositories",
            joinColumns    = @JoinColumn(name = "repoId", referencedColumnName = "repoId"),
            inverseJoinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"))
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

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
