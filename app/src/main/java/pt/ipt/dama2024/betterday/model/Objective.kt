package pt.ipt.dama2024.betterday.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Model class that represents an objective object
 *
 * @property id Unique identifier for the objective. Automatically generated.
 * @property title Title of the objective.
 * @property description Description of the objective.
 * @property creationDate Date when the objective was created.
 * @property checked Whether the objective has been checked/completed. Defaults to false.
 * @property author User ID or username of the author/creator.
 */
@Entity(tableName = "objectives")
data class Objective(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // Unique identifier for the objective
    var title: String, // Title of the objective
    var description: String, // Description of the objective
    var creationDate: Date, // Date when the objective was created
    var checked: Boolean = false, // Whether the objective has been checked/completed
    val author: String, // User ID or username of the author/creator
) {
    /**
     * Since we are using an array type data class, android studio recommends us to generate
     *  the override of equals and hashcode since these are not implemented by default and might be needed
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Objective

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (creationDate != other.creationDate) return false
        if (checked != other.checked) return false
        if (author != other.author) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + checked.hashCode()
        result = 31 * result + author.hashCode()
        return result
    }
}
