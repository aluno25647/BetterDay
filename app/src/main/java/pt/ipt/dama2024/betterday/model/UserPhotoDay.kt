package pt.ipt.dama2024.betterday.model

/**
 * Model class representing user photo, latitude, and longitude.
 *
 * @property photo The user's photo as a ByteArray.
 * @property latitude The latitude associated with the user's location.
 * @property longitude The longitude associated with the user's location.
 */
data class UserPhotoDay(
    val photo: ByteArray? = null,
    val latitude: Double? = null,
    val longitude: Double?= null
) {
    /**
     * Since we are using an array type data class, android studio recommends us to generate
     *  the override of equals and hashcode since these are not implemented by default and might be needed
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserPhotoDay

        if (photo != null) {
            if (other.photo == null) return false
            if (!photo.contentEquals(other.photo)) return false
        } else if (other.photo != null) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = photo?.contentHashCode() ?: 0
        result = 31 * result + (latitude?.hashCode() ?: 0)
        result = 31 * result + (longitude?.hashCode() ?: 0)
        return result
    }
}