package com.example.smartschedule.di

import com.example.smartschedule.data.repository.EmployeeRepositoryImpl
import com.example.smartschedule.data.repository.ShiftRepositoryImpl
import com.example.smartschedule.data.repository.UserRepositoryImpl
import com.example.smartschedule.domain.repository.UserRepository
import com.example.smartschedule.domain.repository.EmployeeRepository
import com.example.smartschedule.domain.repository.ShiftRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindEmployeeRepository(
        employeeRepositoryImpl: EmployeeRepositoryImpl
    ): EmployeeRepository

    @Binds
    @Singleton
    abstract fun bindShiftRepository(
        shiftRepositoryImpl: ShiftRepositoryImpl
    ): ShiftRepository
}