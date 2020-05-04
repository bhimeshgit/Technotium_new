package com.technotium.technotiumapp.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.technotium.technotiumapp.config.InAppConstant;
import com.technotium.technotiumapp.db.entities.Docs;
import com.technotium.technotiumapp.db.pojo.ModifiedMaxDate;

import java.util.List;
@Dao
public interface DocsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertDocs(Docs docs);

    @Query("SELECT * FROM docs")
    List<Docs> getAllDocs();

    @Query("SELECT * FROM docs WHERE pushflag IN("+InAppConstant.PUSH_FLAG_NEW+","+InAppConstant.PUSH_FLAG_UPDATE+") LIMIT 1")
    List<Docs> getAllDocsForPush();

    @Query("SELECT * FROM docs WHERE pkid=:pkid")
    Docs getDocs(String pkid);

    @Delete
    void deleteDoc(Docs docs);

    @Update
    void updateDocs(Docs docs);

    @Query("SELECT MAX(modifiedDate) as maxModifiedDate FROM docs")
    ModifiedMaxDate getMaxModifiedDateTime();

}
