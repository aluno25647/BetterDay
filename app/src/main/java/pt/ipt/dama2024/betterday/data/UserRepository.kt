package pt.ipt.dama2024.betterday.data

import android.content.Context
import java.util.Calendar
import java.util.Date

/**
 * The UserRepository class provides a cleaner and more organized way to interact with the database.
 * It acts as a mediator between the application and the database, offering a simpler and cleaner
 * communication with the database for user-related operations.
 *
 * @param context The application context, required to initialize the database.
 */
class UserRepository(context: Context) {
    private val db = Database(context)

    /**
     * Adds a new user to the database.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param currentDate The current Date.
     * @return True if the user was successfully added, false otherwise.
     */
    fun addUser(username: String, password: String, currentDate: Date): Boolean {
        return db.addUser(username, password, currentDate)
    }

    /**
     * Authenticates a user with the provided username and password.
     *
     * @param username The username to be checked.
     * @param password The password to be checked.
     * @return The generated token if the username and password are valid, null otherwise.
     */
    fun authenticateUser(username: String, password: String): String? {
        return db.authenticateUser(username, password)
    }

    /**
     * Verifies if the provided token matches the stored token for the given username.
     *
     * @param username The username whose token needs to be verified.
     * @param token The token to be verified.
     * @return True if the provided token matches the stored token, false otherwise.
     */
    fun verifyToken(username: String, token: String): Boolean {
        return db.verifyToken(username, token)
    }

    /**
     * Checks if the provided username is already in use.
     *
     * @param username The username to be checked.
     * @return True if the username is already in use, false otherwise.
     */
    fun isUsernameAlreadyInUse(username: String): Boolean {
        return db.isUsernameAlreadyInUse(username)
    }

    /**
     * Checks if the current system date (ignoring time) matches the date retrieved from the database
     * for the specified user.
     *
     * @param username The username of the user whose current date is to be retrieved and validated.
     * @return True if the current system date (ignoring time) matches the date from the database,
     * false otherwise or if the date is not found.
     */
    fun isCurrentDateValidForUser(username: String): Boolean {
        // Retrieve the date stored in the database for the user
        val currentDateFromDb = db.getCurrentDateByUsername(username) ?: return false

        // Get the current system date
        val currentDate = Date()

        // Create Calendar objects to compare year, month, and day
        val calendarDb = Calendar.getInstance().apply {
            time = currentDateFromDb
            // Reset time fields to compare only year, month, and day
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val calendarCurrent = Calendar.getInstance().apply {
            time = currentDate
            // Reset time fields to compare only year, month, and day
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Compare year, month, and day
        return calendarDb.get(Calendar.YEAR) == calendarCurrent.get(Calendar.YEAR) &&
                calendarDb.get(Calendar.MONTH) == calendarCurrent.get(Calendar.MONTH) &&
                calendarDb.get(Calendar.DAY_OF_MONTH) == calendarCurrent.get(Calendar.DAY_OF_MONTH)
    }


    /**
     * Updates the current date for the specified user to today's date.
     *
     * @param username The username of the user whose current date is to be updated.
     * @return True if the current date was successfully updated, false otherwise.
     */
    fun updateCurrentDate(username: String): Boolean {
        val currentDate = Date() // Today's date
        return db.updateCurrentDateByUsername(username, currentDate)
    }


}
