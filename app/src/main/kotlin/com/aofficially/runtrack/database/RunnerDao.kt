package com.aofficially.runtrack.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RunnerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUser(userList: List<RunnerEntity>)

    @Query("select * from runnerentity")
    suspend fun getAllRunner(): List<RunnerEntity>

    @Query("select * from runnerentity where run_Bid = :runBid")
    suspend fun getRunner(runBid: String): RunnerEntity

    @Query("SELECT * FROM runnerentity WHERE run_Bid LIKE :keyword OR run_Firstname LIKE :keyword OR run_Lastname LIKE :keyword")
    suspend fun findRunner(keyword: String): List<RunnerEntity>

    @Update()
    suspend fun updateRunner(runner: RunnerEntity)

    @Query("delete from runnerentity")
    suspend fun clearRunner()
}