package pt.ipt.dama2024.betterday.ui.activity
import SessionManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.model.Objective

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
        val editTextLatitude: TextView = findViewById(R.id.editTextLatitude)
        val editTextLongitude: TextView = findViewById(R.id.editTextLongitude)

        editTextTitle.text = objective.title
        editTextDescription.text = objective.description
        editTextLatitude.text = objective.latitude?.toString() ?: ""
        editTextLongitude.text = objective.longitude?.toString() ?: ""

        val buttonTakePhoto: Button = findViewById(R.id.buttonTakePhoto)
        buttonTakePhoto.setOnClickListener{
            navigateToTakePhoto(objectiveId)
        }

        val buttonDeletePhoto: Button = findViewById(R.id.buttonDeletePhoto)
        buttonDeletePhoto.setOnClickListener{
            deletePhoto()
        }


        // Save changes button click listener
        val buttonSaveChanges: Button = findViewById(R.id.buttonSaveChanges)
        buttonSaveChanges.setOnClickListener {
            val newTitle = editTextTitle.text.toString()
            val newDescription = editTextDescription.text.toString()
            val newLatitude = editTextLatitude.text.toString().toDoubleOrNull()
            val newLongitude = editTextLongitude.text.toString().toDoubleOrNull()

            // Update objective in database
            objective.title = newTitle
            objective.description = newDescription
            objective.latitude = newLatitude
            objective.longitude = newLongitude
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
    private fun navigateToTakePhoto(objectiveId: Long) {
        val intent = Intent(this, TakePhotoActivity::class.java).apply {
            putExtra("objectiveId", objectiveId)
        }
        startActivity(intent)
    }

    private fun deletePhoto() {
        objective.photo1 = null
        objectiveRepository.updateObjective(objective)

        Toast.makeText(this, getString(R.string.photo_deleted), Toast.LENGTH_SHORT).show()
    }
}
