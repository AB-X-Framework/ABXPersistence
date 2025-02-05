package org.abx.persistence.client.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ExecDetails")
public class ExecDetails {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long execId;
}
