package org.abx.persistence.client.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "userdetail")
public class UserDetails {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;


    @OneToMany
    @JoinTable(name = "user_repositories",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "repo_id", referencedColumnName = "id"))
    private Collection<RepoDetails> repoDetails;

    public UserDetails() {
        super();
    }

    public UserDetails(final String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Collection<RepoDetails> getRepoDetails() {
        return repoDetails;
    }

    public void setRepoDetails(Collection<RepoDetails> repoDetails) {
        this.repoDetails = repoDetails;
    }
}
