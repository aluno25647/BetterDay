package pt.ipt.dama2024.betterday.ui.fragment
import SessionManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.Database
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.model.Objective
import java.util.Date


class FragmentThree : Fragment() {
    private lateinit var editTextObjectiveTitle: EditText
    private lateinit var editTextObjectiveDescription: EditText
    private lateinit var buttonSaveObjective: Button

    private lateinit var objectiveRepository: ObjectiveRepository

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_three, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextObjectiveTitle = view.findViewById(R.id.editTextObjectiveTitle)
        editTextObjectiveDescription = view.findViewById(R.id.editTextObjectiveDescription)
        buttonSaveObjective = view.findViewById(R.id.buttonSaveObjective)

        //initialize the repository
        objectiveRepository = ObjectiveRepository(requireContext())

        // Initialize SessionManager
        sessionManager = SessionManager(requireContext())

        buttonSaveObjective.setOnClickListener {
            saveNewObjective()
        }
    }

    private fun saveNewObjective() {
        val title = editTextObjectiveTitle.text.toString().trim()
        val description = editTextObjectiveDescription.text.toString().trim()

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
            author = sessionManager.getUsername()
        )

        val id = objectiveRepository.insertObjective(newObjective, sessionManager.getUsername())

        // verification of correct insertion
        //if successful clears the text boxes for new creations
        if (id != -1L) {  //common check in SQLite to verify if it was inserted in a correct row
            Toast.makeText(requireContext(), getString(R.string.new_objective_saved_toast), Toast.LENGTH_SHORT).show()
            editTextObjectiveTitle.text.clear()
            editTextObjectiveDescription.text.clear()
        } else {
            Toast.makeText(requireContext(), getString(R.string.error_saving_new_objective_toast), Toast.LENGTH_SHORT).show()
        }
    }

}