package com.technotium.technotiumapp.db.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.technotium.technotiumapp.db.entities.Docs;

import java.util.List;

public interface DocsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDocs(Docs docs);

    @Query("SELECT * FROM docs")
    List<Docs> getAllDocs();

    @Query("SELECT * FROM docs WHERE pkid=:pkid")
    Docs getDocs(String pkid);

    @Delete
    void deleteDoc(Docs docs);

    @Update
    void updateDocs(Docs docs);

}
