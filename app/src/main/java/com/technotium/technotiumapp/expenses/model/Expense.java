package com.technotium.technotiumapp.expenses.model;

public class Expense {
    double amount;
    String order_id;

    public String getInsertuserid() {
        return insertuserid;
    }

    public void setInsertuserid(String insertuserid) {
        this.insertuserid = insertuserid;
    }

    String insertuserid;

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    int active;

    public String getInserttimestamp() {
        return inserttimestamp;
    }

    public void setInserttimestamp(String inserttimestamp) {
        this.inserttimestamp = inserttimestamp;
    }

    String inserttimestamp;
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(String expense_id) {
        this.expense_id = expense_id;
    }

    public String getExp_img() {
        return exp_img;
    }

    public void setExp_img(String exp_img) {
        this.exp_img = exp_img;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    String expense_id;
    String exp_img;
    String comment;
}
