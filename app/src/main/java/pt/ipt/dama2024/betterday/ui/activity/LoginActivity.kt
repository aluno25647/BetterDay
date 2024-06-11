package pt.ipt.dama2024.betterday.ui.activity

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.UserRepository
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
            // Validate password
            else if (!ValidationUtils.isValidPassword(password)) {
                Toast.makeText(this, getString(R.string.password_error), Toast.LENGTH_SHORT).show()
            }
            // Authenticate user
            else {
                // Authenticate and get token
                val token = userRepository.authenticateUser(username, password)
                if (token != null) {
                    // SUCCESS
                    Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

                    // Save the username and token in the session.
                    sessionManager.saveCredentials(username, token)

                    // Open MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Authentication failed
                    Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Click Listener for Register Text
        val registerText = findViewById<TextView>(R.id.new_user_btn)
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
