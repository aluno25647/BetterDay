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

class LoginActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userRepository = UserRepository(this)
        sessionManager = SessionManager(this)

        // Check if the user is already logged in
        if (sessionManager.isLoggedIn()) {
            // If logged in, directly open the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish the LoginActivity so the user cannot go back to it
        }

        val loginButton = findViewById<Button>(R.id.login_btn)
        loginButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            if (!ValidationUtils.isValidUsername(username)) {

                Toast.makeText(this, "Username must have at least 5 characters", Toast.LENGTH_SHORT).show()

            } else if (!ValidationUtils.isValidPassword(password)) {

                Toast.makeText(
                    this,
                    "Password must have at least 6 characters, with at least one capital letter and one number",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (userRepository.checkUser(username, password)) {
                // SUCCESS

                // Save the username and password in the session.
                sessionManager.saveCredentials(username, password)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {

                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()

            }
        }

        val registerText = findViewById<TextView>(R.id.new_user_btn)
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
