package com.devarthur.setpoint.data.local

import com.devarthur.setpoint.domain.Exercise
import com.devarthur.setpoint.domain.StudentProfile
import com.devarthur.setpoint.domain.User
import com.devarthur.setpoint.domain.WorkoutAssignment
import com.devarthur.setpoint.domain.WorkoutExecution
import com.devarthur.setpoint.domain.WorkoutTemplate

/**
 * Fonte de dados local (cache) para uso offline-first.
 * Implementação in-memory nesta spec; pode ser substituída por persistência (ex.: SQLDelight) em spec futura.
 */
interface LocalDataSource {
    // User
    fun saveUser(user: User)
    fun getUserById(id: String): User?
    fun getUserByEmail(email: String): User?
    fun getAllUsers(): List<User>

    // StudentProfile
    fun saveStudentProfile(profile: StudentProfile)
    fun getStudentProfileById(id: String): StudentProfile?
    fun getStudentProfileByUserId(userId: String): StudentProfile?

    // Exercise
    fun saveExercise(exercise: Exercise)
    fun getExerciseById(id: String): Exercise?
    fun getAllExercises(): List<Exercise>

    // WorkoutTemplate
    fun saveWorkoutTemplate(template: WorkoutTemplate)
    fun getWorkoutTemplateById(id: String): WorkoutTemplate?
    fun getWorkoutTemplatesByTrainerId(trainerId: String): List<WorkoutTemplate>

    // WorkoutAssignment
    fun saveWorkoutAssignment(assignment: WorkoutAssignment)
    fun getWorkoutAssignmentById(id: String): WorkoutAssignment?
    fun getWorkoutAssignmentsByStudentUserId(studentUserId: String): List<WorkoutAssignment>

    // WorkoutExecution
    fun saveWorkoutExecution(execution: WorkoutExecution)
    fun getWorkoutExecutionById(id: String): WorkoutExecution?
    fun getWorkoutExecutionsByAssignmentId(assignmentId: String): List<WorkoutExecution>
}
