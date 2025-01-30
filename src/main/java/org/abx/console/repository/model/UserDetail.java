package org.abx.console.repository.model;

import jakarta.persistence.*;

@Entity
@Table(name = "userdetail")
public class UserDetail {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

}
