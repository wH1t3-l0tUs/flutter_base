package io.driverdoc.testapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.driverdoc.testapp.data.local.dao.SongDao
import io.driverdoc.testapp.data.model.ItemSong

@Database(entities = arrayOf(ItemSong::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}