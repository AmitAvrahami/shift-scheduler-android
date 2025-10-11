package com.smartschedule.data.local.dao

import androidx.room.*
import com.smartschedule.data.local.entities.ShiftTemplateEntity

/**
 * Data Access Object for the shift_templates table.
 */
@Dao
interface ShiftTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShiftTemplate(template: ShiftTemplateEntity)

    @Update
    suspend fun updateShiftTemplate(template: ShiftTemplateEntity)

    @Delete
    suspend fun deleteShiftTemplate(template: ShiftTemplateEntity)

    @Query("SELECT * FROM shift_templates")
    suspend fun getAllShiftTemplates(): List<ShiftTemplateEntity>

    @Query("SELECT * FROM shift_templates WHERE id = :id")
    suspend fun getShiftTemplateById(id: Long): ShiftTemplateEntity?
}
