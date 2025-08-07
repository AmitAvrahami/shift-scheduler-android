package com.example.smartschedule.auth.domain.models.user

import android.util.Log
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.EmployeePermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.ReportPermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.RulePermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.ShiftPermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.UserInteractionPermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.ViewPermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.VIEW_PERSONAL_REPORT
import com.example.smartschedule.auth.domain.models.user.Permissions.VIEW_PERSONAL_SCHEDULE
import kotlin.collections.plus

data class User(
    val id: String = "", //TODO : MAKE IT UNIQUE
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val infoDetails: InfoDetail = InfoDetail(),
    val userRole: UserRole = UserRole.ADMIN,
    val isActive : Boolean = false
){

    fun User.hasPermission(permission: Permissions): Boolean {
        Log.d("User", "User has permission")
        return userRole.permissions.contains(permission)
    }

    fun User.login(){
        Log.d("User", "User logged in")
    }

    fun User.logout(){
        Log.d("User", "User logged out")
    }

}





