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

    @OneToMany(mappedBy = "projectDetails")
    private Collection<ProjectEnrollment> projectEnrollment;

    public Collection<RepoDetails> getRepoDetails() {
        return repoDetails;
    }

    public Collection<SimSpecs> getSimSpecs() {
        return simSpecs;
    }


    @Column
    private String projectName;


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Collection<ProjectEnrollment> getEnrollment() {
        return projectEnrollment;
    }

    public void setEnrollment(Collection<ProjectEnrollment> projectEnrollment) {
        this.projectEnrollment = projectEnrollment;
    }

    public Long getProjectId() {
        return projectId;
    }

}
