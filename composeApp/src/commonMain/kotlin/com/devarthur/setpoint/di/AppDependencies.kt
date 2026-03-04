package com.devarthur.setpoint.di

import com.devarthur.setpoint.application.usecase.AssignWorkoutToStudentUseCase
import com.devarthur.setpoint.application.usecase.CreateStudentUseCase
import com.devarthur.setpoint.application.usecase.CreateWorkoutTemplateUseCase
import com.devarthur.setpoint.application.usecase.GetMyAssignedWorkoutsUseCase
import com.devarthur.setpoint.application.usecase.GetMyWorkoutHistoryUseCase
import com.devarthur.setpoint.application.usecase.GetStudentWorkoutHistoryUseCase
import com.devarthur.setpoint.application.usecase.IdGenerator
import com.devarthur.setpoint.application.usecase.RecordWorkoutExecutionUseCase
import com.devarthur.setpoint.data.local.InMemoryLocalDataSource
import com.devarthur.setpoint.data.repository.ExerciseRepositoryImpl
import com.devarthur.setpoint.data.repository.StudentProfileRepositoryImpl
import com.devarthur.setpoint.data.repository.UserRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutAssignmentRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutExecutionRepositoryImpl
import com.devarthur.setpoint.data.repository.WorkoutTemplateRepositoryImpl
import com.devarthur.setpoint.domain.Exercise
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.StudentProfile
import com.devarthur.setpoint.domain.User

object AppDependencies {

    private var idCounter = 0
    private val idGenerator = IdGenerator { "id-${idCounter++}" }

    private val local = InMemoryLocalDataSource()

    val userRepository = UserRepositoryImpl(local)
    val studentProfileRepository = StudentProfileRepositoryImpl(local)
    val exerciseRepository = ExerciseRepositoryImpl(local)
    val workoutTemplateRepository = WorkoutTemplateRepositoryImpl(local)
    val workoutAssignmentRepository = WorkoutAssignmentRepositoryImpl(local)
    val workoutExecutionRepository = WorkoutExecutionRepositoryImpl(local)

    val createStudentUseCase = CreateStudentUseCase(userRepository, studentProfileRepository, idGenerator)
    val createWorkoutTemplateUseCase = CreateWorkoutTemplateUseCase(
        exerciseRepository,
        workoutTemplateRepository,
        idGenerator,
    )
    val assignWorkoutToStudentUseCase = AssignWorkoutToStudentUseCase(
        workoutTemplateRepository,
        userRepository,
        workoutAssignmentRepository,
        idGenerator,
    )
    val getStudentWorkoutHistoryUseCase = GetStudentWorkoutHistoryUseCase(
        userRepository,
        workoutAssignmentRepository,
        workoutExecutionRepository,
    )
    val getMyAssignedWorkoutsUseCase = GetMyAssignedWorkoutsUseCase(
        workoutAssignmentRepository,
        workoutTemplateRepository,
    )
    val recordWorkoutExecutionUseCase = RecordWorkoutExecutionUseCase(
        workoutAssignmentRepository,
        workoutTemplateRepository,
        workoutExecutionRepository,
        idGenerator,
    )
    val getMyWorkoutHistoryUseCase = GetMyWorkoutHistoryUseCase(
        workoutAssignmentRepository,
        workoutExecutionRepository,
    )

    suspend fun seedDefaultUsers() {
        if (userRepository.getById(Constants.TRAINER_ID) == null) {
            val trainer = User.create(
                Constants.TRAINER_ID,
                "professor@setpoint.com",
                "Professor",
                Role.TRAINER,
            ).getOrThrow()
            userRepository.save(trainer).getOrThrow()
        }
        if (userRepository.getById(Constants.STUDENT_ID) == null) {
            val student = User.create(
                Constants.STUDENT_ID,
                "aluno@setpoint.com",
                "Aluno",
                Role.STUDENT,
            ).getOrThrow()
            userRepository.save(student).getOrThrow()
            val profile = StudentProfile.create(
                idGenerator.generate(),
                Constants.STUDENT_ID,
                "Aluno",
                null,
            ).getOrThrow()
            studentProfileRepository.save(profile).getOrThrow()
        }
        if (exerciseRepository.list().isEmpty()) {
            listOf("Supino reto", "Agachamento", "Remada").forEachIndexed { i, name ->
                val ex = Exercise.create("ex-$i", name).getOrThrow()
                exerciseRepository.save(ex).getOrThrow()
            }
        }
    }

    object Constants {
        const val TRAINER_ID = "trainer-1"
        const val STUDENT_ID = "student-1"
    }
}
