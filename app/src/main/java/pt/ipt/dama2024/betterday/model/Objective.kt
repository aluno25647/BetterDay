package pt.ipt.dama2024.betterday.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "objectives")
data class Objective(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // Unique identifier for the objective
    val title: String, // Title of the objective
    val description: String, // Description of the objective
    val frequency: Frequency, // Frequency of the objective (daily, weekly, etc)
    val creationDate: Date, // Date when the objective was created
    val deadline: Date, // Deadline for the objective
    val checked: Boolean = false, // Whether the objective has been checked/completed
    val author: String, // User ID or username of the author/creator
)

// Possible frequencies for the objective
enum class Frequency {
    DAILY, WEEKLY, MONTHLY, UNIQUE
}