package com.health.keephealth.helper.vo;

import java.util.Date;

/**
 * Created by Administrator on 2015/7/10 0010.
 */
public class WeightEntity {

    private int id;
    private float weight;
    private Date add_time;
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Date getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Date add_time) {
        this.add_time = add_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public WeightEntity() {
    }

    public WeightEntity(float weight, Date add_time,String comment) {
        this.comment = comment;
        this.weight = weight;
        this.add_time = add_time;
    }

    public Object[] toArr(){
        Object [] objs = new Object[3];
        objs[0] = weight;
        objs[1] = add_time;
        objs[2] = comment;
        return objs;
    }
}
