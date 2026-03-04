package com.devarthur.setpoint

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.devarthur.setpoint.di.AppDependencies
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.navigation.AppScreen
import com.devarthur.setpoint.session.UserSession
import com.devarthur.setpoint.ui.entry.EntryScreen
import com.devarthur.setpoint.ui.professor.AssignWorkoutScreen
import com.devarthur.setpoint.ui.professor.CreateStudentScreen
import com.devarthur.setpoint.ui.professor.CreateWorkoutScreen
import com.devarthur.setpoint.ui.professor.ProfessorHomeScreen
import com.devarthur.setpoint.ui.professor.ProfessorListStudentsScreen
import com.devarthur.setpoint.ui.professor.ProfessorListWorkoutsScreen
import com.devarthur.setpoint.ui.professor.StudentHistoryScreen
import com.devarthur.setpoint.ui.student.ExecuteWorkoutScreen
import com.devarthur.setpoint.ui.student.MyHistoryScreen
import com.devarthur.setpoint.ui.student.StudentHomeScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.devarthur.setpoint.ui.theme.SetPointTheme

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Entry) }
    var session by remember { mutableStateOf<UserSession?>(null) }

    LaunchedEffect(Unit) {
        AppDependencies.seedDefaultUsers()
    }

    SetPointTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (val screen = currentScreen) {
                AppScreen.Entry -> EntryScreen(
                    onSelectProfessor = {
                        session = UserSession(AppDependencies.Constants.TRAINER_ID, Role.TRAINER)
                        currentScreen = AppScreen.ProfessorHome
                    },
                    onSelectStudent = {
                        session = UserSession(AppDependencies.Constants.STUDENT_ID, Role.STUDENT)
                        currentScreen = AppScreen.StudentHome
                    },
                )
                AppScreen.ProfessorHome -> {
                    val s = session!!
                    ProfessorHomeScreen(
                        onListStudents = { currentScreen = AppScreen.ProfessorListStudents },
                        onListWorkouts = { currentScreen = AppScreen.ProfessorListWorkouts },
                        onAssignWorkout = { currentScreen = AppScreen.AssignWorkout },
                        onLogout = { session = null; currentScreen = AppScreen.Entry },
                    )
                }
                AppScreen.ProfessorListStudents -> ProfessorListStudentsScreen(
                    onNavigateToCreateStudent = { currentScreen = AppScreen.CreateStudent },
                    onNavigateToStudentHistory = { studentUserId -> currentScreen = AppScreen.StudentHistory(studentUserId) },
                    onBack = { currentScreen = AppScreen.ProfessorHome },
                )
                AppScreen.CreateStudent -> CreateStudentScreen(
                    trainerId = session!!.userId,
                    onSuccess = { currentScreen = AppScreen.ProfessorListStudents },
                    onBack = { currentScreen = AppScreen.ProfessorListStudents },
                )
                AppScreen.ProfessorListWorkouts -> ProfessorListWorkoutsScreen(
                    trainerId = session!!.userId,
                    onNavigateToCreateWorkout = { currentScreen = AppScreen.CreateWorkout },
                    onBack = { currentScreen = AppScreen.ProfessorHome },
                )
                AppScreen.CreateWorkout -> CreateWorkoutScreen(
                    trainerId = session!!.userId,
                    onSuccess = { currentScreen = AppScreen.ProfessorListWorkouts },
                    onBack = { currentScreen = AppScreen.ProfessorListWorkouts },
                )
                AppScreen.AssignWorkout -> AssignWorkoutScreen(
                    trainerId = session!!.userId,
                    onSuccess = { currentScreen = AppScreen.ProfessorHome },
                    onBack = { currentScreen = AppScreen.ProfessorHome },
                )
                is AppScreen.StudentHistory -> StudentHistoryScreen(
                    studentUserId = screen.studentUserId,
                    trainerId = session!!.userId,
                    onBack = { currentScreen = AppScreen.ProfessorListStudents },
                )
                AppScreen.StudentHome -> {
                    StudentHomeScreen(
                        studentUserId = session!!.userId,
                        onExecuteWorkout = { assignmentId -> currentScreen = AppScreen.ExecuteWorkout(assignmentId) },
                        onMyHistory = { currentScreen = AppScreen.MyHistory },
                        onLogout = { session = null; currentScreen = AppScreen.Entry },
                    )
                }
                is AppScreen.ExecuteWorkout -> ExecuteWorkoutScreen(
                    assignmentId = screen.assignmentId,
                    studentUserId = session!!.userId,
                    onSuccess = { currentScreen = AppScreen.StudentHome },
                    onBack = { currentScreen = AppScreen.StudentHome },
                )
                AppScreen.MyHistory -> MyHistoryScreen(
                    studentUserId = session!!.userId,
                    onBack = { currentScreen = AppScreen.StudentHome },
                )
            }
        }
    }
}
