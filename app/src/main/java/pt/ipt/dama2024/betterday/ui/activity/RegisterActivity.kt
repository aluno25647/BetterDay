package pt.ipt.dama2024.betterday.ui.activity

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.UserRepository
import pt.ipt.dama2024.betterday.utils.DialogHelper
import pt.ipt.dama2024.betterday.utils.ValidationUtils
import java.util.Date

/**
 * Activity responsible for user registration functionality.
 */
class RegisterActivity : AppCompatActivity() {

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

        // set language before the content view!!!

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Register Button Click Listener
        val registerButton = findViewById<Button>(R.id.register_btn)
        registerButton.setOnClickListener {
            // Retrieve user input
            val username = findViewById<EditText>(R.id.new_username).text.toString()
            val email = findViewById<EditText>(R.id.new_email).text.toString()
            val password = findViewById<EditText>(R.id.new_password).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirm_password).text.toString()

            // Validate password confirmation
            if (password != confirmPassword) {
                Toast.makeText(this, getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT)
                    .show()

            }
            // Validate empty fields
            else if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {

                Toast.makeText(this, getString(R.string.empty_fields_error), Toast.LENGTH_SHORT)
                    .show()

            }
            // Validate username
            else if (!ValidationUtils.isValidUsername(username)) {

                Toast.makeText(this, getString(R.string.username_error), Toast.LENGTH_SHORT).show()

            }
            // Validate password
            else if (!ValidationUtils.isValidPassword(password)) {

                Toast.makeText(this, getString(R.string.password_error), Toast.LENGTH_SHORT).show()

            }
            // Validate email
            else if (!ValidationUtils.isValidEmail(email)) {

                Toast.makeText(this, getString(R.string.email_error), Toast.LENGTH_SHORT).show()

            }
            // Check if email is already in use
            else if (userRepository.isEmailAlreadyInUse(email)) {

                Toast.makeText(this, getString(R.string.email_in_use), Toast.LENGTH_SHORT).show()

            }
            // Check if username is already in use
            else if (userRepository.isUsernameAlreadyInUse(username)) {

                Toast.makeText(this, getString(R.string.username_in_use), Toast.LENGTH_SHORT).show()

            } else {
                // Register user
                if (userRepository.addUser(username, password, email, Date())) {
                    // SUCCESS
                    Toast.makeText(
                        this,
                        getString(R.string.registration_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // Registration failed
                    Toast.makeText(
                        this,
                        getString(R.string.registration_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Click Listener for Login Text
        val loginText = findViewById<TextView>(R.id.login_page_btn)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Click listener for login requirements info icon
        val loginRequirementsIcon = findViewById<ImageView>(R.id.register_requirements_icon)
        loginRequirementsIcon.setOnClickListener {
            DialogHelper.showLoginRequirementsDialog(this)
        }

    }
}
