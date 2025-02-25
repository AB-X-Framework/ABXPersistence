package org.abx.persistence.client.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DashboardEnrollment")
public class DashboardEnrollment {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dashboardEnrollmentId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userId", nullable = false)
    private UserDetails userDetails;


    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "dashboardId", nullable = false)
    private DashboardDetails dashboardDetails;

    @Column
    private String role;

    public DashboardDetails getDashboardDetails() {
        return dashboardDetails;
    }

    public void setDashboardDetails(DashboardDetails dashboardDetails) {
        this.dashboardDetails = dashboardDetails;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public Long getDashboardEnrollmentId() {
        return dashboardEnrollmentId;
    }

    public void setDashboardEnrollmentId(Long dashboardEnrollmentId) {
        this.dashboardEnrollmentId = dashboardEnrollmentId;
    }
}
