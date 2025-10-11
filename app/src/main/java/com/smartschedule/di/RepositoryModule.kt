package com.smartschedule.di

import com.smartschedule.data.local.dao.ConstraintDao
import com.smartschedule.data.local.dao.EmployeeDao
import com.smartschedule.data.local.dao.RecurringConstraintDao
import com.smartschedule.data.local.dao.ShiftAssignmentDao
import com.smartschedule.data.local.dao.ShiftDao
import com.smartschedule.data.local.dao.ShiftExchangeDao
import com.smartschedule.data.local.dao.ShiftTemplateDao
import com.smartschedule.data.local.dao.StandingAssignmentDao
import com.smartschedule.data.local.dao.WorkScheduleDao
import com.smartschedule.data.repository.ConstraintRepositoryImpl
import com.smartschedule.data.repository.EmployeeRepositoryImpl
import com.smartschedule.data.repository.RecurringConstraintRepositoryImpl
import com.smartschedule.data.repository.ShiftAssignmentRepositoryImpl
import com.smartschedule.data.repository.ShiftExchangeRepositoryImpl
import com.smartschedule.data.repository.ShiftRepositoryImpl
import com.smartschedule.data.repository.ShiftTemplateRepositoryImpl
import com.smartschedule.data.repository.StandingAssignmentRepositoryImpl
import com.smartschedule.data.repository.WorkScheduleRepositoryImpl
import com.smartschedule.domain.repositories.ConstraintRepository
import com.smartschedule.domain.repositories.EmployeeRepository
import com.smartschedule.domain.repositories.RecurringConstraintRepository
import com.smartschedule.domain.repositories.ShiftAssignmentRepository
import com.smartschedule.domain.repositories.ShiftExchangeRepository
import com.smartschedule.domain.repositories.ShiftRepository
import com.smartschedule.domain.repositories.ShiftTemplateRepository
import com.smartschedule.domain.repositories.StandingAssignmentRepository
import com.smartschedule.domain.repositories.WorkScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideEmployeeRepository(dao: EmployeeDao): EmployeeRepository =
        EmployeeRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideShiftRepository(dao: ShiftDao): ShiftRepository =
        ShiftRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideConstraintRepository(dao: ConstraintDao): ConstraintRepository =
        ConstraintRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideWorkScheduleRepository(dao: WorkScheduleDao): WorkScheduleRepository =
        WorkScheduleRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideShiftExchangeRepository(dao: ShiftExchangeDao): ShiftExchangeRepository =
        ShiftExchangeRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideShiftTemplateRepository(dao: ShiftTemplateDao): ShiftTemplateRepository =
        ShiftTemplateRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideShiftAssignmentRepository(dao: ShiftAssignmentDao): ShiftAssignmentRepository =
        ShiftAssignmentRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideStandingAssignmentRepository(dao: StandingAssignmentDao): StandingAssignmentRepository =
        StandingAssignmentRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideRecurringConstraintRepository(dao: RecurringConstraintDao): RecurringConstraintRepository =
        RecurringConstraintRepositoryImpl(dao)
}
