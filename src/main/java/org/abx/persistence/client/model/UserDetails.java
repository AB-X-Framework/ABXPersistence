package org.abx.persistence.client.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "userdetails")
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

    @OneToMany
    @JoinTable(name = "user_simulations",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "sim_id", referencedColumnName = "id"))
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

    public void setSimSpecs(Collection<SimSpecs> simSpecs) {
        this.simSpecs = simSpecs;
    }
}
