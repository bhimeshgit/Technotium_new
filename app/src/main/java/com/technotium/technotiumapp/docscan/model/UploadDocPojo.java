package com.technotium.technotiumapp.docscan.model;

import android.graphics.Bitmap;

public class UploadDocPojo {
    String encodedPhotoString;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    Bitmap bitmap;
    public String getEncodedPhotoString() {
        return encodedPhotoString;
    }

    public void setEncodedPhotoString(String encodedPhotoString) {
        this.encodedPhotoString = encodedPhotoString;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String doc_type;

    int pkid,server_pkid;
    int order_id;

    public int getPkid() {
        return pkid;
    }

    public void setPkid(int pkid) {
        this.pkid = pkid;
    }

    public int getServer_pkid() {
        return server_pkid;
    }

    public void setServer_pkid(int server_pkid) {
        this.server_pkid = server_pkid;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getPush_flag() {
        return push_flag;
    }

    public void setPush_flag(int push_flag) {
        this.push_flag = push_flag;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    int push_flag;   //0=new,1=updated,2=successfully push
    public long modifiedDate;
}
