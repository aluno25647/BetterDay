import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.util.Locale

/**
 * SessionManager class provides functionality to manage user sessions.
 *
 * @property context The application context.
 */
class SessionManager(private val context: Context) {

    // SharedPreferences instance for storing session data
    private val sharedPreferences = context.getSharedPreferences("BetterDayPrefs", MODE_PRIVATE)

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
        return sharedPreferences.contains("username") && sharedPreferences.contains("password")
    }

    /**
     * Retrieves the username from the session.
     *
     * @return The username if available, null otherwise.
     */
    fun getUsername(): String {
        return sharedPreferences.getString("username", null).toString()
    }

    /**
     * Saves the username and password in the session.
     *
     * @param username The username to be saved.
     * @param password The password to be saved.
     */
    fun saveCredentials(username: String, password: String) {
        with(sharedPreferences.edit()) {
            putString("username", username)
            putString("password", password)
            apply()
        }
    }

    /**
     * Sets the language for the application.
     *
     * @param languageCode The language code to set (e.g., "en" for English, "pt" for Portuguese).
     */
    fun setLanguage(languageCode: String) {
        // Create a Locale object with the given language code
        //val locale = Locale(languageCode)

        // Set the default locale for the application
        //Locale.setDefault(locale)

        // Get resources and configuration objects
        //val resources = context.resources
        //val configuration = resources.configuration

        // Set the locale in the configuration
        //configuration.setLocale(locale)

        // Edit SharedPreferences to store the selected language
        //val editor = sharedPreferences.edit()
        //editor.putString("language", languageCode)

        // Apply changes
        //editor.apply()
    }

    /**
     * Retrieves the currently set language for the application.
     *
     * @return The currently set language code, or "en" (English) if not found.
     */
    fun getCurrentLanguage(): String {
        return sharedPreferences.getString("language", "en").toString()
    }
}
