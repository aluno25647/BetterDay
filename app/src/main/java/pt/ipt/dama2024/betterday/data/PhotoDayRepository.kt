package pt.ipt.dama2024.betterday.data

import android.content.Context
import pt.ipt.dama2024.betterday.model.UserPhotoDay

/**
 * A repository class for managing UserPhotoDay data in the application's database.
 * It acts as a mediator between the application and the database, offering a simpler and cleaner
 * communication with the database for user-related operations.
 */
class PhotoDayRepository(context: Context) {
    private val db = Database(context)

    /**
     * Updates the current UserPhotoDay for the specified username with new photo and location data.
     *
     * @param username  The username of the user whose photo day record is to be updated.
     * @param photo     The photo data as a byte array.
     * @param latitude  The latitude coordinate associated with the photo location.
     * @param longitude The longitude coordinate associated with the photo location.
     * @return The number of rows affected (typically 1 if successful, 0 otherwise).
     */
    fun insertOrUpdateUserPhotoDay(username: String, photo: String, latitude: Double?, longitude: Double?): Long {
        return db.insertOrUpdateUserPhotoDay(username, photo, latitude, longitude)
    }

    /**
     * Retrieves the current UserPhotoDay for the specified username.
     *
     * @param username The username of the user whose current photoDay is to be retrieved.
     * @return The UserPhotoDay object associated with the username, or null if not found.
     */
    fun getUserCurrentPhotoDayById(username: String): UserPhotoDay {
        return db.getUserPhotoDayByUsername(username)
    }

}