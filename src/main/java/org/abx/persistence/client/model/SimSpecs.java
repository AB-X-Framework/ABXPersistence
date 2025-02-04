package org.abx.persistence.client.model;

import jakarta.persistence.*;
@Entity
@Table(name = "simspecs")
public class SimSpecs {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinTable(name = "user_simulations",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "sim_id", referencedColumnName = "id"))
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

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

}
