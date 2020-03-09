package com.arsalaan.business;

import java.util.Date;

public class OrderPlacement {

    private int sushi_id;
    private String sushi_name;
    private int time_to_make;
    private int status_id;

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    private String status_name;
    private Date createdAt;
    private int order_id;

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }



    public int getSushi_id() {
        return sushi_id;
    }

    public void setSushi_id(int sushi_id) {
        this.sushi_id = sushi_id;
    }

    public String getSushi_name() {
        return sushi_name;
    }

    public void setSushi_name(String sushi_name) {
        this.sushi_name = sushi_name;
    }

    public int getTime_to_make() {
        return time_to_make;
    }

    public void setTime_to_make(int time_to_make) {
        this.time_to_make = time_to_make;
    }



}
