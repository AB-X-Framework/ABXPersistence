package org.abx.persistence.client.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserProjects")
public class UserProjects {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserDetails userDetails;


    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    private ProjectDetails projectDetails;

    @Column
    private String role;

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
    }
}
