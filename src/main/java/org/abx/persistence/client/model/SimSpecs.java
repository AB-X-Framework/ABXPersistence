package org.abx.persistence.client.model;

import jakarta.persistence.*;
@Entity
@Table(name = "simspecs")
public class SimSpecs {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sim_id;


    @ManyToOne
    @JoinTable(name = "user_simulations",
            joinColumns    = @JoinColumn(name = "sim_id", referencedColumnName = "sim_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
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
}
