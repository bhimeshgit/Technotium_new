package com.technotium.technotiumapp.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Docs {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="pkid")
    private int pkid;

    @ColumnInfo(name="server_pkid")
    private int server_pkid;

    @ColumnInfo(name="doc_name")
    private String doc_name;

    @ColumnInfo(name="doc_path")
    private String doc_path;

    @ColumnInfo(name="order_id")
    private int order_id;

    @ColumnInfo(name="pushflag")
    private int pushflag;

    @ColumnInfo(name="modifiedDate")
    private long modifiedDate;

}
