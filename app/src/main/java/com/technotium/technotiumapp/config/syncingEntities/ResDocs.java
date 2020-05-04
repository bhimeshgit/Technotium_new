package com.technotium.technotiumapp.config.syncingEntities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResDocs {
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

    @SerializedName("pkid")
    public int pkid;

    @SerializedName("server_pkid")
    public int server_pkid;

    @SerializedName("order_id")
    public int order_id;

    public List<ResDocs> getDocsList() {
        return docsList;
    }

    public void setDocsList(List<ResDocs> docsList) {
        this.docsList = docsList;
    }
    @SerializedName("docslist")
    public List<ResDocs>  docsList;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int success;
}
