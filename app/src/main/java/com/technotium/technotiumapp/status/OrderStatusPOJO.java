package com.technotium.technotiumapp.status;

import java.io.Serializable;

public class OrderStatusPOJO implements Serializable {
    public String pkid;
    public String order_id;

    public String getEx_name() {
        return ex_name;
    }

    public void setEx_name(String ex_name) {
        this.ex_name = ex_name;
    }

    public String ex_name;
    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus_type() {
        return status_type;
    }

    public void setStatus_type(String status_type) {
        this.status_type = status_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus_date() {
        return status_date;
    }

    public void setStatus_date(String status_date) {
        this.status_date = status_date;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getSt_image() {
        return st_image;
    }

    public void setSt_image(String st_image) {
        this.st_image = st_image;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String status_type;
    public String status;
    public String comment;
    public String status_date;
    public String active;
    public String st_image;
    public String expense;
}
