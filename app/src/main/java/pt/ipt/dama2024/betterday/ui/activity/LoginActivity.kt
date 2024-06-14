package pt.ipt.dama2024.betterday.ui.activity

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.data.UserRepository
import pt.ipt.dama2024.betterday.utils.DialogHelper
import pt.ipt.dama2024.betterday.utils.ValidationUtils

/**
 * Activity responsible for handling user login functionality.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager

    /**
     * Called when the activity is starting.
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize UserRepository and SessionManager
        userRepository = UserRepository(this)
        sessionManager = SessionManager(this)

        // Set the language before the activity is created
        sessionManager.setLanguage(sessionManager.getCurrentLanguage())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if the user is already logged in
        if (sessionManager.isLoggedIn()) {
            // If logged in, directly open the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Finish the LoginActivity so the user cannot go back to it
            finish()

            // Get the stored username and token from the session
            val username = sessionManager.getUsername()
            val token = sessionManager.getToken()

            // Verify the token with the database
            if (userRepository.verifyToken(username, token)) {

                // Resets Objectives if is new day
                checkObjectives(username)

                // If token is valid, directly open the main activity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                // Finish the LoginActivity so the user cannot go back to it
                finish()
            } else {
                // If token is invalid, log out the user
                sessionManager.logout()
            }

        }

        // Login Button Click Listener
        val loginButton = findViewById<Button>(R.id.login_btn)

        loginButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            // Validate username
            if (!ValidationUtils.isValidUsername(username)) {
                Toast.makeText(this, getString(R.string.username_error), Toast.LENGTH_SHORT).show()
            }
            // Validate password length
            else if (!ValidationUtils.isPasswordLengthValid(password)) {
                Toast.makeText(this, getString(R.string.password_length_error), Toast.LENGTH_SHORT).show()
            }
            // Validate password complexity
            else if (!ValidationUtils.isPasswordComplexityValid(password)) {
                Toast.makeText(this, getString(R.string.password_complexity_error), Toast.LENGTH_SHORT).show()
            }
            // Authenticate user
            else {
                // Authenticate and get token
                val token = userRepository.authenticateUser(username, password)
                if (token != null) {
                    // SUCCESS
                    Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT)
                        .show()

                    // Save the username and token in the session.
                    sessionManager.saveCredentials(username, token)

                    // Resets Objectives if is new day
                    checkObjectives(username)

                    // Open MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Authentication failed
                    Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        // Click Listener for Register Text
        val registerText = findViewById<TextView>(R.id.new_user_btn)
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Click Listener for About the App Dialog
        val aboutText = findViewById<TextView>(R.id.about_app_btn)
        aboutText.setOnClickListener {
            DialogHelper.showAboutDialog(this)
        }

        // Get the EditText and TextView views
        val passwordEditText = findViewById<EditText>(R.id.password)
        val showPasswordTextView = findViewById<TextView>(R.id.showPassword)

        // Set a click listener on the TextView to toggle password visibility
        showPasswordTextView.setOnClickListener {
            togglePasswordVisibility(passwordEditText)
        }

        // Set a focus change listener on the EditText to hide the password when it loses focus
        passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hidePassword(passwordEditText)
            }
        }

        var isPasswordVisible = false

        // Set a click listener on the TextView to toggle password visibility
        showPasswordTextView.setOnClickListener {
            if (isPasswordVisible) {
                // If password is visible, hide it
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                showPasswordTextView.text = getString(R.string.show_password)
                isPasswordVisible = false
            } else {
                // If password is hidden, show it
                passwordEditText.transformationMethod = null
                showPasswordTextView.text = getString(R.string.hide_password)
                isPasswordVisible = true
            }
        }

        // Click listener for login requirements info icon
        val loginRequirementsIcon = findViewById<ImageView>(R.id.login_requirements_icon)
        loginRequirementsIcon.setOnClickListener {
            DialogHelper.showLoginRequirementsDialog(this)
        }


    }

    /**
     * Toggles the visibility of the password in the specified EditText.
     * If the password is currently hidden, it will be shown, and vice versa.
     *
     * @param passwordEditText The EditText containing the password.
     */
    private fun togglePasswordVisibility(passwordEditText: EditText) {
        passwordEditText.apply {
            transformationMethod =
                if (transformationMethod == PasswordTransformationMethod.getInstance()) {
                    // Show password
                    null
                } else {
                    // Hide password
                    PasswordTransformationMethod.getInstance()
                }
        }
    }

    /**
     * Hides the password in the specified EditText.
     *
     * @param passwordEditText The EditText containing the password.
     */
    private fun hidePassword(passwordEditText: EditText) {
        passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    /**
     * Checks the objectives for the specified user and updates them if necessary.
     *
     * This function verifies if the current date is valid for the user. If the date is not valid,
     * it will uncheck all objectives for the user.
     *
     * @param username The username of the user whose objectives are to be checked.
     */
    private fun checkObjectives(username: String) {
        val repository = ObjectiveRepository(this)

        // Check if the current date is valid for the user
        val currentDateValid = userRepository.isCurrentDateValidForUser(username)

        if (!currentDateValid) {
            // Uncheck all objectives for the user
            val updatedCount = repository.uncheckAllObjectives(username)

            if (updatedCount > 0) {
                Toast.makeText(this, "Successfully updated objectives for user.", Toast.LENGTH_SHORT).show()

                val currentDateUpdated = userRepository.updateCurrentDate(username)

                if (currentDateUpdated) {
                    Toast.makeText(this, "Current date updated successfully for user.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to update current date for user.", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "No objectives were updated for user.", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "Everything is ok.", Toast.LENGTH_SHORT).show()
        }
    }

}
