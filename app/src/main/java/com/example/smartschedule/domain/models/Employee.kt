package com.example.smartschedule.domain.models

data class Employee(
    override val id: String,
    override val name: String,
    override val email: String,
    val employeeNumber: String,
    val maxShiftPerWeek: Int = 5,
    override val type: UserType = UserType.EMPLOYEE
): User(id,name,email,type){

    fun canWorkShift(shift: Shift): Boolean {
        return shift.assignedEmployeeId == null || shift.assignedEmployeeId == this.id
    }

    fun getTotalHoursInWeek(shifts: List<Shift>): Int {
        // TODO: implement later
        return shifts.filter { it.assignedEmployeeId == id }.size * 8
    }
}
