package org.abx.persistence.client.model;

import jakarta.persistence.*;
@Entity
@Table(name = "simspecs")
public class SimSpecs {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long simId;


    @ManyToOne
    @JoinTable(name = "userSimulations",
            joinColumns    = @JoinColumn(name = "simId", referencedColumnName = "simId"),
            inverseJoinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"))
    private UserDetails userDetails;

    public SimSpecs(){
        super();
    }

    @Column
    private String name;

    @Column
    private String folder;

    @Column
    private String path;

    @Column
    private String type;


    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
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

    public Long getSimId() {
        return simId;
    }

}
