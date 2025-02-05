package org.abx.persistence.client.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "UserDetails")
public class UserDetails {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 100)
    private String name;


    public UserDetails() {
        super();
    }

    public UserDetails(final String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    public List<Enrollment> getUserProjects() {
        return enrollments;
    }

    public void setUserProjects(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public Long getUserId() {
        return userId;
    }

}
