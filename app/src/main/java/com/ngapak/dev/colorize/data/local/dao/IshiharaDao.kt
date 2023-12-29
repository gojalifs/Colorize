package com.ngapak.dev.colorize.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ngapak.dev.colorize.data.local.entities.IshiharaEntities

@Dao
interface IshiharaDao {
    @Query("SELECT * FROM ishihara")
    suspend fun getIshiharaTestData(): List<IshiharaEntities>

    @Query("SELECT * FROM ishihara WHERE id = :id")
    suspend fun getIshiharaById(id: Int): List<IshiharaEntities>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(ishiharaEntities: IshiharaEntities)

    @Query("UPDATE ishihara SET imgPath = :imgPath WHERE id = :id")
    suspend fun updateDataById(id: Int, imgPath: String)
}