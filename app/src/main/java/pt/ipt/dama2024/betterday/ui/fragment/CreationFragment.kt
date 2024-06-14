package pt.ipt.dama2024.betterday.ui.fragment
import SessionManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.model.Objective
import pt.ipt.dama2024.betterday.ui.activity.TakePhotoActivity
import java.util.Date


class CreationFragment : Fragment() {
    private lateinit var editTextObjectiveTitle: EditText
    private lateinit var editTextObjectiveDescription: EditText
    private lateinit var editTextObjectiveLatitude: EditText
    private lateinit var editTextObjectiveLongitude: EditText
    private lateinit var buttonSaveObjective: Button
    private lateinit var buttonTakePhoto: Button
    private lateinit var radioButtonYes: RadioButton
    private lateinit var radioButtonNo: RadioButton

    private lateinit var objectiveRepository: ObjectiveRepository

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.creation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextObjectiveTitle = view.findViewById(R.id.editTextObjectiveTitle)
        editTextObjectiveDescription = view.findViewById(R.id.editTextObjectiveDescription)
        editTextObjectiveLatitude = view.findViewById(R.id.editTextObjectiveLatitude)
        editTextObjectiveLongitude = view.findViewById(R.id.editTextObjectiveLongitude)
        buttonSaveObjective = view.findViewById(R.id.buttonSaveObjective)
        radioButtonYes = view.findViewById(R.id.radioButtonYes)
        radioButtonNo = view.findViewById(R.id.radioButtonNo)

        //initialize the repository
        objectiveRepository = ObjectiveRepository(requireContext())

        // Initialize SessionManager
        sessionManager = SessionManager(requireContext())



        buttonSaveObjective.setOnClickListener {
            saveNewObjective()
            // Check if the "Yes" radio button is selected
            //this means a photo will be taken
            if(radioButtonYes.isChecked) {
                navigateToTakePhoto()
            }
        }
    }

    private fun navigateToTakePhoto() {
        val intent = Intent(requireContext(), TakePhotoActivity::class.java).apply {
            putExtra("objectiveId", id)
        }
        // Navigate to TakePhotoActivity and pass the objective ID
        startActivity(intent)
    }

    private fun saveNewObjective() {
        val title = editTextObjectiveTitle.text.toString().trim()
        val description = editTextObjectiveDescription.text.toString().trim()
        val latitude = editTextObjectiveLatitude.text.toString().trim()
        val longitude = editTextObjectiveLongitude.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.empty_title_toast), Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.empty_description_toast), Toast.LENGTH_SHORT).show()
            return
        }

        // Save the objective to the database
        val newObjective = Objective(
            title = title,
            description = description,
            creationDate = Date(),
            checked = false,
            author = sessionManager.getUsername(),
            latitude = latitude.toDoubleOrNull(),
            longitude = longitude.toDoubleOrNull()
        )

        val id = objectiveRepository.insertObjective(newObjective, sessionManager.getUsername())

        // verification of correct insertion
        //if successful clears the text boxes for new creations
        if (id != -1L) {  //common check in SQLite to verify if it was inserted in a correct row
            Toast.makeText(requireContext(), getString(R.string.new_objective_saved_toast), Toast.LENGTH_SHORT).show()
            editTextObjectiveTitle.text.clear()
            editTextObjectiveDescription.text.clear()
            editTextObjectiveLongitude.text.clear()
            editTextObjectiveLatitude.text.clear()

        } else {
            Toast.makeText(requireContext(), getString(R.string.error_saving_new_objective_toast), Toast.LENGTH_SHORT).show()
        }
    }

}