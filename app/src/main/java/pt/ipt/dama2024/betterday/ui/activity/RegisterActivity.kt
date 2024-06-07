package pt.ipt.dama2024.betterday.ui.activity

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

class RegisterActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userRepository = UserRepository(this)

        val registerButton = findViewById<Button>(R.id.register_btn)

        registerButton.setOnClickListener {

            val username = findViewById<EditText>(R.id.new_username).text.toString()
            val email = findViewById<EditText>(R.id.new_email).text.toString()
            val password = findViewById<EditText>(R.id.new_password).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.confirm_password).text.toString()

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()

            } else if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {

                Toast.makeText(
                    this,
                    "Username, email or password cannot be empty",
                    Toast.LENGTH_SHORT
                )
                    .show()

            } else if (!ValidationUtils.isValidUsername(username)) {

                Toast.makeText(this, "Username must have at least 5 characters", Toast.LENGTH_SHORT).show()

            } else if (!ValidationUtils.isValidPassword(password)) {

                Toast.makeText(
                    this,
                    "Password must have at least 6 characters, with at least one capital letter and one number",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (!ValidationUtils.isValidEmail(email)) {

                    Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()

            } else if (userRepository.isEmailAlreadyInUse(email)) {

                Toast.makeText(this, "Email is already in use", Toast.LENGTH_SHORT)
                    .show()

            } else if (userRepository.isUsernameAlreadyInUse(username)) {

                Toast.makeText(this, "Username is already in use", Toast.LENGTH_SHORT)
                    .show()

            } else {
                // SUCCESS
                if (userRepository.addUser(username, password, email)) {

                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }

            }
        }

        val loginText = findViewById<TextView>(R.id.login_page_btn)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
