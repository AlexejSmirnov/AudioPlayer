package com.pekadev.audioplayer.model.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pekadev.audioplayer.view.application.MyApplication

@Database(entities = [UriEntity::class], version = 1)
abstract class UriDatabase: RoomDatabase(){
    abstract fun uriDao(): UriDao

    companion object{
        private lateinit var DATABASE: UriDatabase
        fun getDatabase(): UriDatabase{
            if (!::DATABASE.isInitialized){
                DATABASE = Room.databaseBuilder(MyApplication.getApplicationContext(), UriDatabase::class.java, "MyDb").build()
            }
            return DATABASE
        }
    }
}