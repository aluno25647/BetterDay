package pt.ipt.dama2024.betterday.ui.activity

import SessionManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.model.Objective
import java.util.Date

/**
 * Activity responsible for the creation of new Objectives
 */
class CreationActivity : AppCompatActivity() {
    private lateinit var editTextObjectiveTitle: EditText
    private lateinit var editTextObjectiveDescription: EditText
    private lateinit var buttonSaveObjective: Button

    private lateinit var objectiveRepository: ObjectiveRepository

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize the repository
        objectiveRepository = ObjectiveRepository(this)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Set the language before the activity is created
        sessionManager.setLanguage(sessionManager.getCurrentLanguage())

        // set language before the content view!!!

        super.onCreate(savedInstanceState)
        setContentView(R.layout.creation_activity)

        editTextObjectiveTitle = findViewById(R.id.editTextObjectiveTitle)
        editTextObjectiveDescription = findViewById(R.id.editTextObjectiveDescription)
        buttonSaveObjective = findViewById(R.id.buttonSaveObjective)

        buttonSaveObjective.setOnClickListener {
            saveNewObjective()
        }

        // Go back button click listener
        val buttonBack: ImageView = findViewById(R.id.buttonGoBackFromCreation)
        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun saveNewObjective() {
        val title = editTextObjectiveTitle.text.toString().trim()
        val description = editTextObjectiveDescription.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_title_toast), Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_description_toast), Toast.LENGTH_SHORT).show()
            return
        }

        // Save the objective to the database
        val newObjective = Objective(
            title = title,
            description = description,
            creationDate = Date(),
            checked = false,
            author = sessionManager.getUsername()
        )

        val id = objectiveRepository.insertObjective(newObjective, sessionManager.getUsername())

        // Verification of correct insertion
        // If successful, clear the text boxes for new creations
        if (id != -1L) {  // Common check in SQLite to verify if it was inserted in a correct row
            Toast.makeText(this, getString(R.string.new_objective_saved_toast), Toast.LENGTH_SHORT).show()
            editTextObjectiveTitle.text.clear()
            editTextObjectiveDescription.text.clear()
            finish()
        } else {
            Toast.makeText(this, getString(R.string.error_saving_new_objective_toast), Toast.LENGTH_SHORT).show()
        }
    }
}
