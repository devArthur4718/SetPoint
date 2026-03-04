package com.devarthur.setpoint.data.local

import app.cash.sqldelight.db.SqlDriver
import com.devarthur.setpoint.db.SetPointDb
import com.devarthur.setpoint.domain.Exercise
import com.devarthur.setpoint.domain.Role
import com.devarthur.setpoint.domain.StudentProfile
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.domain.WorkoutAssignment
import com.devarthur.setpoint.domain.WorkoutExecution
import com.devarthur.setpoint.domain.WorkoutExercise
import com.devarthur.setpoint.domain.WorkoutTemplate
import com.devarthur.setpoint.domain.SetExecution

/**
 * Implementação persistente de [LocalDataSource] usando SQLDelight.
 * Dados persistem em disco entre sessões. O [SqlDriver] é injetado (ex.: pela app ou por teste).
 */
class SqlDelightLocalDataSource(
    driver: SqlDriver,
) : LocalDataSource {

    private val db = SetPointDb(driver)
    private val q = db.setPointDbQueries
    private val passwordHashes = mutableMapOf<String, String>()

    override fun saveUser(user: User) {
        q.insertUser(user.id, user.email, user.name, user.role.name)
    }

    override fun getUserById(id: String): User? {
        return q.selectUserById(id).executeAsOneOrNull()?.toDomain()
    }

    override fun getUserByEmail(email: String): User? {
        return q.selectUserByEmail(email).executeAsOneOrNull()?.toDomain()
    }

    override fun getAllUsers(): List<User> {
        return q.selectAllUsers().executeAsList().map { it.toDomain() }
    }

    override fun setPasswordHash(userId: String, hash: String) {
        passwordHashes[userId] = hash
    }

    override fun getPasswordHash(userId: String): String? = passwordHashes[userId]

    override fun saveStudentProfile(profile: StudentProfile) {
        q.insertStudentProfile(profile.id, profile.userId, profile.displayName, profile.createdAt)
    }

    override fun getStudentProfileById(id: String): StudentProfile? {
        return q.selectStudentProfileById(id).executeAsOneOrNull()?.toDomain()
    }

    override fun getStudentProfileByUserId(userId: String): StudentProfile? {
        return q.selectStudentProfileByUserId(userId).executeAsOneOrNull()?.toDomain()
    }

    override fun saveExercise(exercise: Exercise) {
        q.insertExercise(exercise.id, exercise.name, exercise.description)
    }

    override fun getExerciseById(id: String): Exercise? {
        return q.selectExerciseById(id).executeAsOneOrNull()?.toDomain()
    }

    override fun getAllExercises(): List<Exercise> {
        return q.selectAllExercises().executeAsList().map { it.toDomain() }
    }

    override fun saveWorkoutTemplate(template: WorkoutTemplate) {
        q.insertWorkoutTemplate(template.id, template.name, template.trainerId, template.createdAt)
        q.deleteWorkoutExercisesByTemplateId(template.id)
        template.exercises.forEach { ex ->
            q.insertWorkoutExercise(
                id = ex.id,
                workoutTemplateId = template.id,
                exerciseId = ex.exerciseId,
                order_ = ex.order.toLong(),
                sets = ex.sets.toLong(),
                reps = ex.reps.toLong(),
                loadKg = ex.loadKg,
                restSeconds = ex.restSeconds?.toLong(),
            )
        }
    }

    override fun getWorkoutTemplateById(id: String): WorkoutTemplate? {
        val row = q.selectWorkoutTemplateById(id).executeAsOneOrNull() ?: return null
        val exercises = q.selectWorkoutExercisesByTemplateId(id).executeAsList().map { it.toDomain() }
        if (exercises.isEmpty()) return null
        return row.toDomain(exercises)
    }

    override fun getWorkoutTemplatesByTrainerId(trainerId: String): List<WorkoutTemplate> {
        return q.selectWorkoutTemplatesByTrainerId(trainerId).executeAsList().mapNotNull { row ->
            val exercises = q.selectWorkoutExercisesByTemplateId(row.id).executeAsList().map { it.toDomain() }
            if (exercises.isEmpty()) null else row.toDomain(exercises)
        }
    }

    override fun saveWorkoutAssignment(assignment: WorkoutAssignment) {
        q.insertWorkoutAssignment(assignment.id, assignment.workoutTemplateId, assignment.studentUserId, assignment.assignedAt)
    }

    override fun getWorkoutAssignmentById(id: String): WorkoutAssignment? {
        return q.selectWorkoutAssignmentById(id).executeAsOneOrNull()?.toDomain()
    }

    override fun getWorkoutAssignmentsByStudentUserId(studentUserId: String): List<WorkoutAssignment> {
        return q.selectWorkoutAssignmentsByStudentUserId(studentUserId).executeAsList().map { it.toDomain() }
    }

    override fun saveWorkoutExecution(execution: WorkoutExecution) {
        q.insertWorkoutExecution(execution.id, execution.workoutAssignmentId, execution.executedAt)
        q.deleteSetExecutionsByExecutionId(execution.id)
        execution.setExecutions.forEach { se ->
            q.insertSetExecution(
                id = se.id,
                workoutExecutionId = se.workoutExecutionId,
                workoutExerciseId = se.workoutExerciseId,
                setNumber = se.setNumber.toLong(),
                actualReps = se.actualReps?.toLong(),
                actualLoadKg = se.actualLoadKg,
            )
        }
    }

    override fun getWorkoutExecutionById(id: String): WorkoutExecution? {
        val row = q.selectWorkoutExecutionById(id).executeAsOneOrNull() ?: return null
        val sets = q.selectSetExecutionsByExecutionId(id).executeAsList().map { it.toDomain() }
        return row.toDomain(sets)
    }

    override fun getWorkoutExecutionsByAssignmentId(assignmentId: String): List<WorkoutExecution> {
        return q.selectWorkoutExecutionsByAssignmentId(assignmentId).executeAsList().map { row ->
            val sets = q.selectSetExecutionsByExecutionId(row.id).executeAsList().map { it.toDomain() }
            row.toDomain(sets)
        }
    }

    private fun com.devarthur.setpoint.db.User.toDomain(): User =
        User(id = id, email = email, name = name, role = enumValueOf<Role>(role))

    private fun com.devarthur.setpoint.db.StudentProfile.toDomain(): StudentProfile =
        StudentProfile(id = id, userId = userId, displayName = displayName, createdAt = createdAt)

    private fun com.devarthur.setpoint.db.Exercise.toDomain(): Exercise =
        Exercise(id = id, name = name, description = description)

    private fun com.devarthur.setpoint.db.WorkoutExercise.toDomain(): WorkoutExercise =
        WorkoutExercise(
            id = id,
            exerciseId = exerciseId,
            order = order_.toInt(),
            sets = sets.toInt(),
            reps = reps.toInt(),
            loadKg = loadKg,
            restSeconds = restSeconds?.toInt(),
        )

    private fun com.devarthur.setpoint.db.WorkoutTemplate.toDomain(exercises: List<WorkoutExercise>): WorkoutTemplate =
        WorkoutTemplate(
            id = id,
            name = name,
            trainerId = trainerId,
            exercises = exercises,
            createdAt = createdAt,
        )

    private fun com.devarthur.setpoint.db.WorkoutAssignment.toDomain(): WorkoutAssignment =
        WorkoutAssignment(id = id, workoutTemplateId = workoutTemplateId, studentUserId = studentUserId, assignedAt = assignedAt)

    private fun com.devarthur.setpoint.db.WorkoutExecution.toDomain(setExecutions: List<SetExecution>): WorkoutExecution =
        WorkoutExecution(
            id = id,
            workoutAssignmentId = workoutAssignmentId,
            executedAt = executedAt,
            setExecutions = setExecutions,
        )

    private fun com.devarthur.setpoint.db.SetExecution.toDomain(): SetExecution =
        SetExecution(
            id = id,
            workoutExecutionId = workoutExecutionId,
            workoutExerciseId = workoutExerciseId,
            setNumber = setNumber.toInt(),
            actualReps = actualReps?.toInt(),
            actualLoadKg = actualLoadKg,
        )
}
