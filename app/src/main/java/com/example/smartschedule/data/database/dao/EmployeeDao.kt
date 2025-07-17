package com.example.smartschedule.data.database.dao

import androidx.room.*
import com.example.smartschedule.data.database.entities.EmployeeEntity
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