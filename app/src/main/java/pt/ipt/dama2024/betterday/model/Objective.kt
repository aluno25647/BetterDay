package pt.ipt.dama2024.betterday.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "objectives")
data class Objective(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // Unique identifier for the objective
    var title: String, // Title of the objective
    var description: String, // Description of the objective
    var creationDate: Date, // Date when the objective was created
    var checked: Boolean = false, // Whether the objective has been checked/completed
    val author: String, // User ID or username of the author/creator
    var photo1: ByteArray? = null,  // ByteArray for the first photo
    var latitude: Double? = null,   // Latitude for GPS coordinates
    var longitude: Double? = null   // Longitude for GPS coordinates
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
        if (photo1 != null) {
            if (other.photo1 == null) return false
            if (!photo1.contentEquals(other.photo1)) return false
        } else if (other.photo1 != null) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + checked.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + (photo1?.contentHashCode() ?: 0)
        result = 31 * result + (latitude?.hashCode() ?: 0)
        result = 31 * result + (longitude?.hashCode() ?: 0)
        return result
    }
}
