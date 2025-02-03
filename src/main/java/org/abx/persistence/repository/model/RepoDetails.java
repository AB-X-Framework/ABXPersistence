package org.abx.persistence.repository.model;

import jakarta.persistence.*;

@Entity
@Table(name = "repodetails")
public class RepoDetails {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String name;


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
