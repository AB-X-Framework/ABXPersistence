package org.abx.persistence.client.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "userdetails")
public class UserDetails {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String name;


    @OneToMany
    @JoinTable(name = "userRepositories",
            joinColumns    = @JoinColumn(name = "userId", referencedColumnName = "userId"),
            inverseJoinColumns   = @JoinColumn(name = "repoId", referencedColumnName = "repoId"))
    private Collection<RepoDetails> repoDetails;

    @OneToMany
    @JoinTable(name = "userSimulations",
            joinColumns   = @JoinColumn(name = "userId", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "simId", referencedColumnName = "simId"))
    private Collection<SimSpecs> simSpecs;


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

    public Collection<SimSpecs> getSimSpecs() {
        return simSpecs;
    }
}
