package org.abx.persistence.client.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "ProjectDetails")
public class ProjectDetails {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;

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


    public Collection<RepoDetails> getRepoDetails() {
        return repoDetails;
    }

    public Collection<SimSpecs> getSimSpecs() {
        return simSpecs;
    }
}
