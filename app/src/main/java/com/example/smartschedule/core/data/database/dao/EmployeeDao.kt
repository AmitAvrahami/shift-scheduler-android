package com.example.smartschedule.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartschedule.core.data.database.entities.EmployeeEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employees")
    fun getAllEmployees(): Flow<List<EmployeeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeeEntity)

    @Delete
    suspend fun deleteEmployee(employee: EmployeeEntity)

    @Query("SELECT * FROM employees WHERE id = :id")
    suspend fun getEmployeeById(id: String): EmployeeEntity?

    @Query("SELECT * FROM employees WHERE employeeNumber = :employeeNumber")
    suspend fun getEmployeeByEmployeeNumber(employeeNumber: String): EmployeeEntity?

}