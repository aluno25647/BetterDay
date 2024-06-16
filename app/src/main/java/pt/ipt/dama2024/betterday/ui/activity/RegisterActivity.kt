package pt.ipt.dama2024.betterday.ui.activity

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
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
            val password = findViewById<EditText>(R.id.new_password).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirm_password).text.toString()

            // Validate password confirmation
            if (password != confirmPassword) {
                Toast.makeText(this, getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT)
                    .show()

            }
            // Validate empty fields
            else if (username.isEmpty() || password.isEmpty()) {

                Toast.makeText(this, getString(R.string.empty_fields_error), Toast.LENGTH_SHORT)
                    .show()

            }
            // Validate username
            else if (!ValidationUtils.isValidUsername(username)) {

                Toast.makeText(this, getString(R.string.username_error), Toast.LENGTH_SHORT).show()

            }
            // Validate password length
            else if (!ValidationUtils.isPasswordLengthValid(password)) {

                Toast.makeText(this, getString(R.string.password_length_error), Toast.LENGTH_SHORT)
                    .show()

            }
            // Validate password complexity
            else if (!ValidationUtils.isPasswordComplexityValid(password)) {

                Toast.makeText(
                    this,
                    getString(R.string.password_complexity_error),
                    Toast.LENGTH_SHORT
                ).show()

            }
            // Check if username is already in use
            else if (userRepository.isUsernameAlreadyInUse(username)) {

                Toast.makeText(this, getString(R.string.username_in_use), Toast.LENGTH_SHORT).show()

            } else {
                // Register user
                if (userRepository.addUser(username, password, Date())) {
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
            finish()  // Close the RegisterActivity
        }

        // Click listener for login requirements info icon
        val loginRequirementsIcon = findViewById<ImageView>(R.id.register_requirements_icon)
        loginRequirementsIcon.setOnClickListener {
            DialogHelper.showLoginRequirementsDialog(this)
        }

        // First Password

        // Get the EditText and TextView views
        val passwordEditText = findViewById<EditText>(R.id.new_password)
        val showPasswordTextView = findViewById<TextView>(R.id.showPassword_1)

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

        // Second Password

        // Get the EditText and TextView views
        val passwordEditText2 = findViewById<EditText>(R.id.confirm_password)
        val showPasswordTextView2 = findViewById<TextView>(R.id.showPassword_2)

        // Set a click listener on the TextView to toggle password visibility
        showPasswordTextView2.setOnClickListener {
            togglePasswordVisibility(passwordEditText2)
        }

        // Set a focus change listener on the EditText to hide the password when it loses focus
        passwordEditText2.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hidePassword(passwordEditText2)
            }
        }

        var isPasswordVisible2 = false

        // Set a click listener on the TextView to toggle password visibility
        showPasswordTextView2.setOnClickListener {
            if (isPasswordVisible2) {
                // If password is visible, hide it
                passwordEditText2.transformationMethod = PasswordTransformationMethod.getInstance()
                showPasswordTextView2.text = getString(R.string.show_password)
                isPasswordVisible2 = false
            } else {
                // If password is hidden, show it
                passwordEditText2.transformationMethod = null
                showPasswordTextView2.text = getString(R.string.hide_password)
                isPasswordVisible2 = true
            }
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
}
