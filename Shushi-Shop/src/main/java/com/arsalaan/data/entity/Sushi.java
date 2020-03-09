package com.arsalaan.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "sushi")
public class Sushi {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="name")
    private String name;
    @Column(name="time_to_make")
    private int time_to_make;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime_to_make() {
        return time_to_make;
    }

    public void setTime_to_make(int time_to_make) {
        this.time_to_make = time_to_make;
    }


}
