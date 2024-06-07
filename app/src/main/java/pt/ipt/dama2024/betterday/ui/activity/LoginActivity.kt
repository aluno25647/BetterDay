package pt.ipt.dama2024.betterday.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.betterday.data.Database
import pt.ipt.dama2024.betterday.R

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = Database(this)

        val loginButton = findViewById<Button>(R.id.login_btn)
        loginButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            if (!isValidUsername(username)) {

                Toast.makeText(this, "Username must have at least 5 characters", Toast.LENGTH_SHORT).show()

            } else if (!isValidPassword(password)) {

                Toast.makeText(
                    this,
                    "Password must have at least 6 characters, with at least one capital letter and one number",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (dbHelper.checkUser(username, password)) {

                val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("betterday_username", username)
                    putString("betterday_password", password)
                    apply()
                }
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

    private fun isValidUsername(username: String): Boolean {
        return username.length >= 5
    }

    private fun isValidPassword(password: String): Boolean {
        val containsUpperCase = password.any { it.isUpperCase() }
        val containsDigit = password.any { it.isDigit() }
        return password.length >= 6 && containsUpperCase && containsDigit
    }
}
