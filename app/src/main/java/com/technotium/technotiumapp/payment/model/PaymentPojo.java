package com.technotium.technotiumapp.payment.model;

public class PaymentPojo {
    String payment_id;
    String order_id;

    public String getPay_bank() {
        return pay_bank;
    }

    public void setPay_bank(String pay_bank) {
        this.pay_bank = pay_bank;
    }

    String pay_bank;
    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPay_image() {
        return pay_image;
    }

    public void setPay_image(String pay_image) {
        this.pay_image = pay_image;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    String payment_mode;
    String comment;
    String pay_image;
    String payment_date;
    String amount;

    public int getTotal_paid() {
        return total_paid;
    }

    public void setTotal_paid(int total_paid) {
        this.total_paid = total_paid;
    }

    int total_paid;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    String active;
}
