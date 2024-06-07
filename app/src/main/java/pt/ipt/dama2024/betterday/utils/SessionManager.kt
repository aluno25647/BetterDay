import android.content.Context
import android.content.Context.MODE_PRIVATE

/**
 * SessionManager class provides functionality to manage user sessions.
 *
 * @property context The application context.
 */
class SessionManager(private val context: Context) {

    // SharedPreferences instance for storing session data
    private val sharedPreferences = context.getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

    /**
     * Clears the session data, effectively logging out the user.
     */
    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    /**
     * Checks if a user is logged in.
     *
     * @return true if a user is logged in, false otherwise.
     */
    fun isLoggedIn(): Boolean {
        // Check if user is logged in based on session data logic
        return sharedPreferences.contains("bdUser") && sharedPreferences.contains("bdPass")
    }

    /**
     * Retrieves the username from the session.
     *
     * @return The username if available, null otherwise.
     */
    fun getUsername(): String {
        return sharedPreferences.getString("bdUser", null).toString()
    }

    /**
     * Saves the username and password in the session.
     *
     * @param username The username to be saved.
     * @param password The password to be saved.
     */
    fun saveCredentials(username: String, password: String) {
        with(sharedPreferences.edit()) {
            putString("bdUser", username)
            putString("bdPass", password)
            apply()
        }
    }
}
