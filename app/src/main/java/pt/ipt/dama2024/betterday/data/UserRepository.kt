package pt.ipt.dama2024.betterday.data

import android.content.Context

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
     * @param email The email address of the new user.
     * @return True if the user was successfully added, false otherwise.
     */
    fun addUser(username: String, password: String, email: String): Boolean {
        return db.addUser(username, password, email)
    }

    /**
     * Checks if the provided username and password are valid.
     *
     * @param username The username to be checked.
     * @param password The password to be checked.
     * @return True if the username and password are valid, false otherwise.
     */
    fun checkUser(username: String, password: String): Boolean {
        return db.checkUser(username, password)
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
     * Checks if the provided email is already in use.
     *
     * @param email The email address to be checked.
     * @return True if the email is already in use, false otherwise.
     */
    fun isEmailAlreadyInUse(email: String): Boolean {
        return db.isEmailAlreadyInUse(email)
    }
}
