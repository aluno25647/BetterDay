import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import android.os.LocaleList
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
    @Suppress("DEPRECATION")
    fun setLanguage(languageCode: String) {

        val resources = context.resources
        val localeListToSet = LocaleList(Locale(languageCode))
        LocaleList.setDefault(localeListToSet)
        resources.configuration.setLocales(localeListToSet)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        // Edit SharedPreferences to store the selected language
        with(sharedPreferences.edit()) {
            putString("language", languageCode)
            apply()
        }
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
