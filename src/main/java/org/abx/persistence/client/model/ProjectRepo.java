package org.abx.persistence.client.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "ProjectRepos")
public class ProjectRepo {

    @Id
    @Column(unique = true, nullable = false)
    private Long projectRepoId;
    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectDetails projectDetails;
    @ManyToOne
    @JoinColumn(name = "repoId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RepoDetails repoDetails;

    public Long getProjectRepoId() {
        return projectRepoId;
    }

    public void setProjectRepoId(Long projectRepoId) {
        this.projectRepoId = projectRepoId;
    }


    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
    }

    public RepoDetails getRepoDetails() {
        return repoDetails;
    }

    public void setRepoDetails(RepoDetails repoDetails) {
        this.repoDetails = repoDetails;
    }
}
