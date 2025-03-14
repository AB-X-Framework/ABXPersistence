package org.abx.persistence.client.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "DashboardDetails")
public class DashboardDetails {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dashboardId;

    @Column(length = 200)
    private String dashboardName;

    @OneToMany(mappedBy = "dashboardDetails")
    private Collection<DashboardEnrollment> dashboardEnrollments;

    @OneToMany(mappedBy = "dashboardDetails")
    private Collection<DashboardRepo> dashboardRepos;
    public DashboardDetails() {
        super();
    }


    public String getDashboardName() {
        return dashboardName;
    }

    public void setDashboardName(final String name) {
        this.dashboardName = name;
    }


    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
    }

    public Collection<DashboardEnrollment> getDashboardEnrollments() {
        return dashboardEnrollments;
    }

    public void setDashboardEnrollments(Collection<DashboardEnrollment> dashboardEnrollments) {
        this.dashboardEnrollments = dashboardEnrollments;
    }

    public Collection<DashboardRepo> getDashboardRepos() {
        return dashboardRepos;
    }

    public void setDashboardRepos(Collection<DashboardRepo> dashboardRepos) {
        this.dashboardRepos = dashboardRepos;
    }
}
