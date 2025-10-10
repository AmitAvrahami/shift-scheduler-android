package com.smartschedule.data.local.dao

import androidx.room.*
import com.smartschedule.data.local.entities.ShiftExchangeEntity

@Dao
interface ShiftExchangeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShiftExchange(exchange: ShiftExchangeEntity)

    @Update
    suspend fun updateShiftExchange(exchange: ShiftExchangeEntity)

    @Delete
    suspend fun deleteShiftExchange(exchange: ShiftExchangeEntity)

    @Query("SELECT * FROM shift_exchanges")
    suspend fun getAllShiftExchanges(): List<ShiftExchangeEntity>

    @Query("SELECT * FROM shift_exchanges WHERE from_employee_id = :employeeId OR to_employee_id = :employeeId")
    suspend fun getExchangesByEmployee(employeeId: Long): List<ShiftExchangeEntity>
}
