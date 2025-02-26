package org.abx.persistence.client.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "DashboardRepos")
public class DashboardRepo {

    @Id
    @Column(unique = true, nullable = false)
    private Long dashboardRepoId;
    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DashboardDetails dashboardDetails;
    @ManyToOne
    @JoinColumn(name = "repoId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RepoDetails repoDetails;

    public Long getDashboardRepoId() {
        return dashboardRepoId;
    }

    public void setDashboardRepoId(Long projectRepoId) {
        this.dashboardRepoId = projectRepoId;
    }


    public DashboardDetails getDashboardDetails() {
        return dashboardDetails;
    }

    public void setDashboardDetails(DashboardDetails projectDetails) {
        this.dashboardDetails = projectDetails;
    }

    public RepoDetails getRepoDetails() {
        return repoDetails;
    }

    public void setRepoDetails(RepoDetails repoDetails) {
        this.repoDetails = repoDetails;
    }
}
