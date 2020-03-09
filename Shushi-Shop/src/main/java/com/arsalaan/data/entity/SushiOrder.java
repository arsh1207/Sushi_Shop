package com.arsalaan.data.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sushi_order")
public class SushiOrder {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "status_id")
    private int status_id;

    @Column(name = "sushi_id")
    private int sushi_id;

   @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "time_to_prepare")
    private int time_to_prepare;

    public int getTime_to_prepare() {
        return time_to_prepare;
    }

    public void setTime_to_prepare(int time_to_prepare) {
        this.time_to_prepare = time_to_prepare;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getSushi_id() {
        return sushi_id;
    }

    public void setSushi_id(int sushi_id) {
        this.sushi_id = sushi_id;
    }

   public Date getCreatedAt() {
       return createdAt;
   }

   public void setCreatedAt() {
        this.createdAt = new Date();    }


}
