package io.driverdoc.testapp.data.local.dao

import androidx.room.*
import io.driverdoc.testapp.data.model.ItemSong

@Dao
interface SongDao {
    @Query(value = "SELECT * FROM ItemSong WHERE ItemSong.id = :songId")
    fun findOne(songId: Int): ItemSong

    @Query(value = "SELECT * FROM ItemSong")
    fun findAll(): MutableList<ItemSong>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(item: ItemSong): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemSong: MutableList<ItemSong>): MutableList<Long>
}