package org.abx.persistence.client.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "UserDetails", indexes = {
        @Index(name = "idx_username", columnList = "username") // Index for better query performance
})
public class UserDetails {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 100)
    private String username;


    public UserDetails() {
        super();
    }

    public UserDetails(final String username) {
        super();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String name) {
        this.username = name;
    }

    //userDetails  is the variable in Enrollment
    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    //userDetails  is the variable in DashboardDetails
    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
    private List<DashboardDetails> dashboardDetails;

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public Long getUserId() {
        return userId;
    }

    public List<DashboardDetails> getDashboardDetails() {
        return dashboardDetails;
    }

    public void setDashboardDetails(List<DashboardDetails> dashboardDetails) {
        this.dashboardDetails = dashboardDetails;
    }
}
