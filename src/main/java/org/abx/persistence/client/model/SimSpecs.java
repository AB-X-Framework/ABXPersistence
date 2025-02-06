package org.abx.persistence.client.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "SimSpecs")
public class SimSpecs {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long simId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "ProjectSimulations",
            joinColumns = @JoinColumn(name = "simId", referencedColumnName = "simId"),
            inverseJoinColumns = @JoinColumn(name = "projectId", referencedColumnName = "projectId"))
    private ProjectDetails projectDetails;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SimExecs",
            joinColumns = @JoinColumn(name = "simId", referencedColumnName = "simId"),
            inverseJoinColumns = @JoinColumn(name = "execId", referencedColumnName = "execId"))
    private Collection<ExecDetails> execs;

    public SimSpecs() {
        super();
    }

    @Column
    private String simName;

    @Column
    private String folder;

    @Column
    private String path;


    @Column(length = 100)
    private String type;


    public void setProjectDetails(ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
    }

    public ProjectDetails getProjectDetails() {
        return projectDetails;
    }

    public String getSimName() {
        return simName;
    }

    public void setSimName(String name) {
        this.simName = name;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSimId() {
        return simId;
    }

}
