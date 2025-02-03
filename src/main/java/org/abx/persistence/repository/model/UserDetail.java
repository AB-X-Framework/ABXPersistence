package org.abx.persistence.repository.model;

import jakarta.persistence.*;

@Entity
@Table(name = "userdetail")
public class UserDetail {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    public UserDetail() {
        super();
    }

    public UserDetail(final String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
