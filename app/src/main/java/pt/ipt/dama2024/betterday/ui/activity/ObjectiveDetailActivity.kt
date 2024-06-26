package pt.ipt.dama2024.betterday.ui.activity

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.model.Objective
import pt.ipt.dama2024.betterday.utils.DialogHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ObjectiveDetailActivity : AppCompatActivity() {

    private lateinit var objectiveRepository: ObjectiveRepository
    private lateinit var sessionManager: SessionManager
    private var objectiveId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize ObjectiveRepository
        objectiveRepository = ObjectiveRepository(this)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Set the language before the activity is created
        sessionManager.setLanguage(sessionManager.getCurrentLanguage())

        // set language before the content view!!!

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objective_detail)

        // Enable the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get objectiveId from intent
        objectiveId = intent.getLongExtra("objectiveId", -1L)

        if (objectiveId == -1L) {
            Toast.makeText(
                this,
                getString(R.string.toast_objective_id_not_provided),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        // Go back button click listener
        val buttonBack: ImageView = findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        if (objectiveId != -1L) {
            // Fetch objective from repository using objectiveId
            val objective = objectiveRepository.getObjectiveById(objectiveId)

            if (objective != null) {
                // Update UI with fetched objective data
                updateUIWithObjective(objective)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.toast_objective_not_found),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    /**
     * Updates the user interface with data from a given objective.
     *
     * @param objective The objective object containing data to update the UI.
     */
    private fun updateUIWithObjective(objective: Objective) {
        // Find views by ID
        val titleTextView = findViewById<TextView>(R.id.textViewTitle)
        val descriptionTextView = findViewById<TextView>(R.id.textViewDescription)
        val creationDateTextView = findViewById<TextView>(R.id.textViewCreationDate)
        val statusTextView = findViewById<TextView>(R.id.textViewStatus)

        // Set the data to views
        titleTextView.text = objective.title
        descriptionTextView.text = objective.description
        creationDateTextView.text = formatDate(objective.creationDate)

        // Set status text and color based on "checked" attribute
        if (objective.checked) {
            statusTextView.text = getString(R.string.objective_detail_completed)
            statusTextView.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            statusTextView.text = getString(R.string.objective_detail_not_completed)
            statusTextView.setTextColor(ContextCompat.getColor(this, R.color.red))
        }

        // Handle delete objective button click
        val deleteButton: Button = findViewById(R.id.buttonDeleteObjective)
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(objectiveId)
        }

        // Handle edit objective button click
        val editButton: Button = findViewById(R.id.buttonEditObjective)
        editButton.setOnClickListener {
            navigateToEditObjective(objectiveId)
        }
    }

    /**
     * Shows a confirmation dialog to confirm the deletion of an objective.
     *
     * @param objectiveId The ID of the objective to be deleted.
     */
    private fun showDeleteConfirmationDialog(objectiveId: Long) {
        DialogHelper.showDeleteConfirmationDialog(this) {
            deleteObjective(objectiveId)
        }
    }

    /**
     * Deletes an objective from the database.
     *
     * @param objectiveId The ID of the objective to be deleted.
     */
    private fun deleteObjective(objectiveId: Long) {
        val rowsAffected = objectiveRepository.deleteObjective(objectiveId)
        if (rowsAffected > 0) {
            Toast.makeText(
                this,
                getString(R.string.toast_objective_deleted_success),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        } else {
            Toast.makeText(
                this,
                getString(R.string.toast_failed_to_delete_objective),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    /**
     * Navigates to the EditObjectiveActivity to edit the specified objective.
     *
     * @param objectiveId The ID of the objective to be edited.
     */
    private fun navigateToEditObjective(objectiveId: Long) {
        val intent = Intent(this, EditObjectiveActivity::class.java).apply {
            putExtra("objectiveId", objectiveId)
        }
        startActivity(intent)
    }

    /**
     * Formats a Date object into a string with the format "dd MMM yyyy".
     *
     * @param date The date to be formatted.
     * @return A string representing the formatted date.
     */
    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }

}
