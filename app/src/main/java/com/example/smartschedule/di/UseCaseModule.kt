package com.example.smartschedule.di

import com.example.smartschedule.domain.repository.EmployeeRepository
import com.example.smartschedule.domain.repository.UserRepository
import com.example.smartschedule.domain.usecase.AddEmployeeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    fun provideAddEmployeeUseCase(
        employeeRepository: EmployeeRepository,
        userRepository: UserRepository
    ): AddEmployeeUseCase {
        return AddEmployeeUseCase(employeeRepository,userRepository)
    }
}