// File: app/src/main/java/com/example/smartschedule/di/UseCaseModule.kt

package com.example.smartschedule.di

import com.example.smartschedule.domain.repository.EmployeeRepository
import com.example.smartschedule.domain.repository.UserRepository
import com.example.smartschedule.domain.usecase.AddEmployeeUseCase
import com.example.smartschedule.domain.usecase.LoginUseCase
import com.example.smartschedule.domain.usecase.LogoutUseCase
import com.example.smartschedule.domain.usecase.GetCurrentUserUseCase
import com.example.smartschedule.domain.usecase.RegisterUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    // ✅ EXISTING
    @Provides
    @Singleton
    fun provideAddEmployeeUseCase(
        employeeRepository: EmployeeRepository,
        userRepository: UserRepository
    ): AddEmployeeUseCase {
        return AddEmployeeUseCase(employeeRepository, userRepository)
    }

    // 🆕 NEW - Authentication Use Cases
    @Provides
    @Singleton
    fun provideLoginUseCase(
        userRepository: UserRepository
    ): LoginUseCase {
        return LoginUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        userRepository: UserRepository
    ): LogoutUseCase {
        return LogoutUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserUseCase(
        userRepository: UserRepository
    ): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideRegisterUserUseCase(
        userRepository: UserRepository
    ): RegisterUserUseCase {
        return RegisterUserUseCase(userRepository)
    }
}