package org.abx.persistence.client.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DashboardDetails")
public class DashboardDetails {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dashboardId;

    @Column(length = 200)
    private String dashboardName;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserDetails userDetails;

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

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
