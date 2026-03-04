package com.devarthur.setpoint

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import com.devarthur.setpoint.ui.theme.SetPointTheme

private data class NavState(val screen: AppScreen, val forward: Boolean)

@Composable
fun App() {
    var navState by remember { mutableStateOf(NavState(AppScreen.Entry, forward = true)) }
    var session by remember { mutableStateOf<UserSession?>(null) }
    val reduceMotion = com.devarthur.setpoint.ui.motion.reduceMotionEnabled()

    fun navigate(screen: AppScreen, forward: Boolean) {
        navState = NavState(screen, forward)
    }

    LaunchedEffect(Unit) {
        AppDependencies.seedDefaultUsers()
    }

    val darkTheme = isSystemInDarkTheme()
    SetPointTheme(darkTheme = darkTheme) {
        Surface(modifier = Modifier.fillMaxSize()) {
            CompositionLocalProvider(com.devarthur.setpoint.ui.motion.LocalReduceMotion provides reduceMotion) {
                AnimatedContent(
                    targetState = navState,
                    modifier = Modifier.fillMaxSize(),
                    transitionSpec = {
                        val transform: ContentTransform = if (reduceMotion) {
                            fadeIn(animationSpec = tween(0)) togetherWith fadeOut(animationSpec = tween(0))
                        } else {
                            val direction = if (targetState.forward) 1 else -1
                            val enter = slideInHorizontally(animationSpec = tween(300)) { fullWidth -> direction * fullWidth } +
                                fadeIn(animationSpec = tween(300))
                            val exit = slideOutHorizontally(animationSpec = tween(300)) { fullWidth -> -direction * fullWidth } +
                                fadeOut(animationSpec = tween(300))
                            enter togetherWith exit
                        }
                        transform
                    },
                    label = "screenTransition",
                ) { state ->
                    when (val screen = state.screen) {
                AppScreen.Entry -> EntryScreen(
                    onSelectProfessor = {
                        session = UserSession(AppDependencies.Constants.TRAINER_ID, Role.TRAINER)
                        navigate(AppScreen.ProfessorHome, forward = true)
                    },
                    onSelectStudent = {
                        session = UserSession(AppDependencies.Constants.STUDENT_ID, Role.STUDENT)
                        navigate(AppScreen.StudentHome, forward = true)
                    },
                )
                AppScreen.ProfessorHome -> {
                    ProfessorHomeScreen(
                        onListStudents = { navigate(AppScreen.ProfessorListStudents, forward = true) },
                        onListWorkouts = { navigate(AppScreen.ProfessorListWorkouts, forward = true) },
                        onAssignWorkout = { navigate(AppScreen.AssignWorkout, forward = true) },
                        onLogout = { session = null; navigate(AppScreen.Entry, forward = false) },
                    )
                }
                AppScreen.ProfessorListStudents -> ProfessorListStudentsScreen(
                    onNavigateToCreateStudent = { navigate(AppScreen.CreateStudent, forward = true) },
                    onNavigateToStudentHistory = { studentUserId -> navigate(AppScreen.StudentHistory(studentUserId), forward = true) },
                    onBack = { navigate(AppScreen.ProfessorHome, forward = false) },
                )
                AppScreen.CreateStudent -> CreateStudentScreen(
                    trainerId = session!!.userId,
                    onSuccess = { navigate(AppScreen.ProfessorListStudents, forward = false) },
                    onBack = { navigate(AppScreen.ProfessorListStudents, forward = false) },
                )
                AppScreen.ProfessorListWorkouts -> ProfessorListWorkoutsScreen(
                    trainerId = session!!.userId,
                    onNavigateToCreateWorkout = { navigate(AppScreen.CreateWorkout, forward = true) },
                    onBack = { navigate(AppScreen.ProfessorHome, forward = false) },
                )
                AppScreen.CreateWorkout -> CreateWorkoutScreen(
                    trainerId = session!!.userId,
                    onSuccess = { navigate(AppScreen.ProfessorListWorkouts, forward = false) },
                    onBack = { navigate(AppScreen.ProfessorListWorkouts, forward = false) },
                )
                AppScreen.AssignWorkout -> AssignWorkoutScreen(
                    trainerId = session!!.userId,
                    onSuccess = { navigate(AppScreen.ProfessorHome, forward = false) },
                    onBack = { navigate(AppScreen.ProfessorHome, forward = false) },
                )
                is AppScreen.StudentHistory -> StudentHistoryScreen(
                    studentUserId = screen.studentUserId,
                    trainerId = session!!.userId,
                    onBack = { navigate(AppScreen.ProfessorListStudents, forward = false) },
                )
                AppScreen.StudentHome -> {
                    StudentHomeScreen(
                        studentUserId = session!!.userId,
                        onExecuteWorkout = { assignmentId -> navigate(AppScreen.ExecuteWorkout(assignmentId), forward = true) },
                        onMyHistory = { navigate(AppScreen.MyHistory, forward = true) },
                        onLogout = { session = null; navigate(AppScreen.Entry, forward = false) },
                    )
                }
                is AppScreen.ExecuteWorkout -> ExecuteWorkoutScreen(
                    assignmentId = screen.assignmentId,
                    studentUserId = session!!.userId,
                    onSuccess = { navigate(AppScreen.StudentHome, forward = false) },
                    onBack = { navigate(AppScreen.StudentHome, forward = false) },
                )
                AppScreen.MyHistory -> MyHistoryScreen(
                    studentUserId = session!!.userId,
                    onBack = { navigate(AppScreen.StudentHome, forward = false) },
                )
                    }
                }
            }
        }
    }
}
