package com.pekadev.audioplayer.model.room

import androidx.room.*
@Dao
interface UriDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUri(uriEntity: UriEntity)

    @Update
    fun updateUri(uriEntity: UriEntity)

    @Delete
    fun removeUri(uriEntity: UriEntity)

    @Query("Select * from UriEntity")
    fun selectAllUri(): List<UriEntity>
}