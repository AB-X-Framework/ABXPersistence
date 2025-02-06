package org.abx.persistence.client.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ExecDetails")
public class ExecDetails {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long execId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "UserExecs",
            joinColumns = @JoinColumn(name = "execId", referencedColumnName = "execId"),
            inverseJoinColumns = @JoinColumn(name = "projectId", referencedColumnName = "projectId"))
    private ProjectDetails projectDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "SimExecs",
            joinColumns = @JoinColumn(name = "execId", referencedColumnName = "execId"),
            inverseJoinColumns = @JoinColumn(name = "simId", referencedColumnName = "simId"))
    private SimSpecs simSpecs;

    @Column
    private String name;

    @Column
    private String folder;

    @Column
    private String path;


    @Column(length = 100)
    private String type;

    @Column(length = 10000)
    private String output;

    public void setProjectDetails(ProjectDetails projectDetails) {
        this.projectDetails = projectDetails;
    }

    public ProjectDetails getUserDetails() {
        return projectDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getExecId() {
        return execId;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public SimSpecs getSimSpecs() {
        return simSpecs;
    }

    public void setSimSpecs(SimSpecs simSpecs) {
        this.simSpecs = simSpecs;
    }
}
