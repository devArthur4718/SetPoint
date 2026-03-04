package com.devarthur.setpoint.data.local

import com.devarthur.setpoint.domain.Exercise
import com.devarthur.setpoint.domain.StudentProfile
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.domain.WorkoutAssignment
import com.devarthur.setpoint.domain.WorkoutExecution
import com.devarthur.setpoint.domain.WorkoutTemplate

/**
 * Implementação in-memory de [LocalDataSource].
 * Dados persistem apenas durante a sessão; substituível por persistência em disco em spec futura.
 */
class InMemoryLocalDataSource : LocalDataSource {
    private val users = mutableMapOf<String, User>()
    private val usersByEmail = mutableMapOf<String, User>()
    private val passwordHashes = mutableMapOf<String, String>()
    private val studentProfiles = mutableMapOf<String, StudentProfile>()
    private val studentProfilesByUserId = mutableMapOf<String, StudentProfile>()
    private val exercises = mutableMapOf<String, Exercise>()
    private val workoutTemplates = mutableMapOf<String, WorkoutTemplate>()
    private val workoutTemplatesByTrainer = mutableMapOf<String, MutableList<WorkoutTemplate>>()
    private val workoutAssignments = mutableMapOf<String, WorkoutAssignment>()
    private val workoutAssignmentsByStudent = mutableMapOf<String, MutableList<WorkoutAssignment>>()
    private val workoutExecutions = mutableMapOf<String, WorkoutExecution>()
    private val workoutExecutionsByAssignment = mutableMapOf<String, MutableList<WorkoutExecution>>()

    override fun saveUser(user: User) {
        users[user.id] = user
        usersByEmail[user.email.lowercase()] = user
    }

    override fun getUserById(id: String): User? = users[id]
    override fun getUserByEmail(email: String): User? = usersByEmail[email.lowercase()]
    override fun getAllUsers(): List<User> = users.values.toList()

    override fun setPasswordHash(userId: String, hash: String) {
        passwordHashes[userId] = hash
    }

    override fun getPasswordHash(userId: String): String? = passwordHashes[userId]

    override fun saveStudentProfile(profile: StudentProfile) {
        studentProfiles[profile.id] = profile
        studentProfilesByUserId[profile.userId] = profile
    }

    override fun getStudentProfileById(id: String): StudentProfile? = studentProfiles[id]
    override fun getStudentProfileByUserId(userId: String): StudentProfile? = studentProfilesByUserId[userId]

    override fun saveExercise(exercise: Exercise) {
        exercises[exercise.id] = exercise
    }

    override fun getExerciseById(id: String): Exercise? = exercises[id]
    override fun getAllExercises(): List<Exercise> = exercises.values.toList()

    override fun saveWorkoutTemplate(template: WorkoutTemplate) {
        workoutTemplates[template.id] = template
        workoutTemplatesByTrainer.getOrPut(template.trainerId) { mutableListOf() }.apply {
            removeAll { it.id == template.id }
            add(template)
        }
    }

    override fun getWorkoutTemplateById(id: String): WorkoutTemplate? = workoutTemplates[id]
    override fun getWorkoutTemplatesByTrainerId(trainerId: String): List<WorkoutTemplate> =
        workoutTemplatesByTrainer[trainerId].orEmpty()

    override fun saveWorkoutAssignment(assignment: WorkoutAssignment) {
        workoutAssignments[assignment.id] = assignment
        workoutAssignmentsByStudent.getOrPut(assignment.studentUserId) { mutableListOf() }.apply {
            removeAll { it.id == assignment.id }
            add(assignment)
        }
    }

    override fun getWorkoutAssignmentById(id: String): WorkoutAssignment? = workoutAssignments[id]
    override fun getWorkoutAssignmentsByStudentUserId(studentUserId: String): List<WorkoutAssignment> =
        workoutAssignmentsByStudent[studentUserId].orEmpty()

    override fun saveWorkoutExecution(execution: WorkoutExecution) {
        workoutExecutions[execution.id] = execution
        workoutExecutionsByAssignment.getOrPut(execution.workoutAssignmentId) { mutableListOf() }.apply {
            removeAll { it.id == execution.id }
            add(execution)
        }
    }

    override fun getWorkoutExecutionById(id: String): WorkoutExecution? = workoutExecutions[id]
    override fun getWorkoutExecutionsByAssignmentId(assignmentId: String): List<WorkoutExecution> =
        workoutExecutionsByAssignment[assignmentId].orEmpty()
}
