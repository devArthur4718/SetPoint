package com.devarthur.setpoint.navigation

sealed class AppScreen {
    data object Entry : AppScreen()
    data object ProfessorLogin : AppScreen()
    data object StudentLogin : AppScreen()
    data object ProfessorHome : AppScreen()
    data object ProfessorListStudents : AppScreen()
    data object CreateStudent : AppScreen()
    data object ProfessorListWorkouts : AppScreen()
    data object CreateWorkout : AppScreen()
    data object AssignWorkout : AppScreen()
    data class StudentHistory(val studentUserId: String) : AppScreen()
    data object StudentHome : AppScreen()
    data class ExecuteWorkout(val assignmentId: String) : AppScreen()
    data object MyHistory : AppScreen()
}
