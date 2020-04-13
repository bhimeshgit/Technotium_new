package com.technotium.technotiumapp.after_sales.model;

public class MeterReadingPojo {
    String reading_img;
    String type;

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    String added_by;
    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    String order_id;

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    int active;
    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    String pkid;

    public String getReading_img() {
        return reading_img;
    }

    public void setReading_img(String reading_img) {
        this.reading_img = reading_img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInserttimestamp() {
        return inserttimestamp;
    }

    public void setInserttimestamp(String inserttimestamp) {
        this.inserttimestamp = inserttimestamp;
    }

    String inserttimestamp;
}
