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
    @JoinTable(name = "ProjectRepositories",
            joinColumns = @JoinColumn(name = "projectId", referencedColumnName = "projectId"),
            inverseJoinColumns = @JoinColumn(name = "repoId", referencedColumnName = "repoId"))
    private Collection<RepoDetails> repoDetails;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ProjectSimulations",
            joinColumns = @JoinColumn(name = "projectId", referencedColumnName = "projectId"),
            inverseJoinColumns = @JoinColumn(name = "simId", referencedColumnName = "simId"))
    private Collection<SimSpecs> simSpecs;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ProjectExecs",
            joinColumns = @JoinColumn(name = "projectId", referencedColumnName = "projectId"),
            inverseJoinColumns = @JoinColumn(name = "execId", referencedColumnName = "execId"))
    private Collection<ExecDetails> execs;

    @OneToMany(mappedBy = "projectDetails", cascade = CascadeType.ALL)
    private Collection<Enrollment> enrollment;

    public Collection<RepoDetails> getRepoDetails() {
        return repoDetails;
    }

    public Collection<SimSpecs> getSimSpecs() {
        return simSpecs;
    }


    @Column
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Enrollment> getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Collection<Enrollment> enrollment) {
        this.enrollment = enrollment;
    }

    public Long getProjectId() {
        return projectId;
    }

}
