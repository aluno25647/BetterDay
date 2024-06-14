package pt.ipt.dama2024.betterday.ui.activity

import SessionManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.model.Objective

class EditObjectiveActivity : AppCompatActivity() {

    private lateinit var objective: Objective
    private lateinit var objectiveRepository: ObjectiveRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_objective)

        // Initialize ObjectiveRepository
        objectiveRepository = ObjectiveRepository(this)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Set the language before the activity is created
        sessionManager.setLanguage(sessionManager.getCurrentLanguage())

        // Retrieve objective details from intent extras (or from database if needed)
        val objectiveId = intent.getLongExtra("objectiveId", -1)
        objective = objectiveRepository.getObjectiveById(objectiveId) ?: return


        val editTextTitle: TextView = findViewById(R.id.editTextTitle)
        val editTextDescription: TextView = findViewById(R.id.editTextDescription)

        editTextTitle.text = objective.title
        editTextDescription.text = objective.description

        // Save changes button click listener
        val buttonSaveChanges: Button = findViewById(R.id.buttonSaveChanges)
        buttonSaveChanges.setOnClickListener {
            val newTitle = editTextTitle.text.toString()
            val newDescription = editTextDescription.text.toString()

            // Update objective in database
            objective.title = newTitle
            objective.description = newDescription
            objectiveRepository.updateObjective(objective)

            // Finish activity and return to previous screen
            finish()
        }

        //  Go back button click listener
        val buttonGoBackToDetail: Button = findViewById(R.id.buttonGoBackToDetail)
        buttonGoBackToDetail.setOnClickListener {
            finish()
        }
    }
}
