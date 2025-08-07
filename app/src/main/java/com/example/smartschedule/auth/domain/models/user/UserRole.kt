package com.example.smartschedule.auth.domain.models.user

import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.EmployeePermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.ReportPermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.RulePermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.ShiftPermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.UserInteractionPermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.PermissionGroups.ViewPermissions
import com.example.smartschedule.auth.domain.models.user.Permissions.VIEW_PERSONAL_REPORT
import com.example.smartschedule.auth.domain.models.user.Permissions.VIEW_PERSONAL_SCHEDULE

enum class UserRole(val permissions: Set<Permissions> = emptySet()) {
    ADMIN(Permissions.entries.toSet()),
    EMPLOYEE(UserInteractionPermissions + ViewPermissions.filter {
        it == VIEW_PERSONAL_SCHEDULE || it == VIEW_PERSONAL_REPORT
    }.toSet()),
    MANAGER(
        EmployeePermissions +
                ShiftPermissions +
                RulePermissions +
                ReportPermissions +
                ViewPermissions
    );

}