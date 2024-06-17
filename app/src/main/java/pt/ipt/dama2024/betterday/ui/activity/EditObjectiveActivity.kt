package pt.ipt.dama2024.betterday.ui.activity

import SessionManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.model.Objective

/**
 * Activity responsible for the edition of new Objectives elements
 */
class EditObjectiveActivity : AppCompatActivity() {

    private lateinit var objective: Objective
    private lateinit var objectiveRepository: ObjectiveRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {

        // Initialize ObjectiveRepository
        objectiveRepository = ObjectiveRepository(this)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Set the language before the activity is created
        sessionManager.setLanguage(sessionManager.getCurrentLanguage())

        // set language before the content view!!!

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_objective)

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

        // Go back button click listener
        val buttonGoBackToDetail: ImageView = findViewById(R.id.buttonGoBackToDetail)
        buttonGoBackToDetail.setOnClickListener {
            finish()
        }

        // hide keyboard
        val rootLayout: View = findViewById(R.id.edit_layout)
        rootLayout.setOnTouchListener { _, _ ->
            hideKeyboard(rootLayout)
            true
        }
    }

    /**
     * Hides the soft keyboard from the screen.
     *
     * This function takes a view as a parameter and hides the soft keyboard if it is currently visible.
     * It uses the InputMethodManager to handle the keyboard operations.
     *
     * @param view The view from which the keyboard should be hidden. This view must be a part of the currently focused window.
     */
    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
