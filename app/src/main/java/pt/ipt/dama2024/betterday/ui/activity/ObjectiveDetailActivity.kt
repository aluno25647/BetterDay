package pt.ipt.dama2024.betterday.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ObjectiveDetailActivity : AppCompatActivity() {

    private lateinit var objectiveRepository: ObjectiveRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objective_detail)

        // Initialize ObjectiveRepository
        objectiveRepository = ObjectiveRepository(this)

        // Enable the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get data from intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val creationDate = intent.getLongExtra("creationDate", -1L)
        val checked = intent.getBooleanExtra("checked", false)
        val objectiveId = intent.getLongExtra("objectiveId", -1L)

        // Find views by ID
        val titleTextView = findViewById<TextView>(R.id.textViewTitle)
        val descriptionTextView = findViewById<TextView>(R.id.textViewDescription)
        val creationDateTextView = findViewById<TextView>(R.id.textViewCreationDate)
        val statusTextView = findViewById<TextView>(R.id.textViewStatus)

        // Set the data to views
        titleTextView.text = title
        descriptionTextView.text = description

        creationDateTextView.text = formatDate(creationDate)

        // Set status text and color based on "checked" attribute
        if (checked) {
            statusTextView.text = "Completed"
            statusTextView.setTextColor(ContextCompat.getColor(this, R.color.green))  // Green color for "Completed"
        } else {
            statusTextView.text = "Not Completed"
            statusTextView.setTextColor(ContextCompat.getColor(this, R.color.red))  // Red color for "Not Completed"
        }

        // Handle custom back button click
        val backButton: Button = findViewById(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }

        // Handle delete objective button click
        val deleteButton: Button = findViewById(R.id.buttonDeleteObjective)
        deleteButton.setOnClickListener {
            if (objectiveId != -1L) {
                // Call the deleteObjective function in repository
                val rowsAffected = objectiveRepository.deleteObjective(objectiveId)
                if (rowsAffected > 0) {
                    // Objective deleted successfully
                    finish()
                } else {
                    Toast.makeText(this, "Failed to delete objective", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Failed to delete objective", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatDate(date: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(Date(date))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button click here
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
