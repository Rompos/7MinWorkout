package com.rompos.a7minworkout

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao//Dao = Data Access Object
interface HistoryDao {

    @Insert//(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyEntity: HistoryEntity)

    @Delete
    suspend fun delete(historyEntity: HistoryEntity)

    @Query("SELECT * FROM `history-table`")
    fun fetchAllDates(): Flow<List<HistoryEntity>>//if i use Flow then i don't need the <suspend> fun fetchAllDates()

    @Query("Select * from `history-table` where id=:id")
    fun fetchHistoryById(id: Int): Flow<HistoryEntity>
}