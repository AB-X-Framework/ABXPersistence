package org.abx.persistence.client.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "UserDetails")
public class UserDetails {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 100)
    private String name;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UserRepositories",
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "repoId", referencedColumnName = "repoId"))
    private Collection<RepoDetails> repoDetails;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UserSimulations",
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "simId", referencedColumnName = "simId"))
    private Collection<SimSpecs> simSpecs;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UserExecs",
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "execId", referencedColumnName = "execId"))
    private Collection<ExecDetails> execs;

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
