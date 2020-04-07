package com.technotium.technotiumapp.material.model;

public class MaterialPojo {
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(int material_id) {
        this.material_id = material_id;
    }

    String material ="",quantity="";
    int active , order_id,material_id;

    public String getInserttimestamp() {
        return inserttimestamp;
    }

    public void setInserttimestamp(String inserttimestamp) {
        this.inserttimestamp = inserttimestamp;
    }

    String inserttimestamp;

    public int getInsertuserid() {
        return insertuserid;
    }

    public void setInsertuserid(int insertuserid) {
        this.insertuserid = insertuserid;
    }

    int insertuserid;

    public String getInsertBy() {
        return insertBy;
    }

    public void setInsertBy(String insertBy) {
        this.insertBy = insertBy;
    }

    String insertBy="";
}
