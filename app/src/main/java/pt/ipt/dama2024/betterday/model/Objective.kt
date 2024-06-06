package pt.ipt.dama2024.betterday.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "objectives")
data class Objective(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val frequency: String, // "daily", "weekly", "monthly", "unique"
    val date: Date? = null //
)