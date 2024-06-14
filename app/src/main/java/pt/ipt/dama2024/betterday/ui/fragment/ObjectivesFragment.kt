package pt.ipt.dama2024.betterday.ui.fragment

import ObjectiveAdapter
import SessionManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.model.Objective
import pt.ipt.dama2024.betterday.ui.activity.CreationActivity
import pt.ipt.dama2024.betterday.ui.activity.ObjectiveDetailActivity

/**
 * Fragment to display and manage a list of objectives.
 */
class ObjectivesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ObjectiveAdapter
    private lateinit var objectiveRepository: ObjectiveRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var incentiveMessage: TextView

    /**
     * Called to have the fragment instantiate its user interface view.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.objectives_fragment, container, false)
    }

    /**
     * Called immediately after onCreateView() has returned, but before any saved state has been restored in to the view.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewObjectives)
        recyclerView.layoutManager = LinearLayoutManager(context)
        incentiveMessage = view.findViewById(R.id.incentiveMessage)

        // Initialize session manager and repository
        sessionManager = SessionManager(requireContext())
        objectiveRepository = ObjectiveRepository(requireContext())

        // Setup adapter for RecyclerView
        adapter = ObjectiveAdapter(mutableListOf(), { selectedObjective ->
            // Handle item click: Open ObjectiveDetailActivity with the selected objective's ID
            val intent = Intent(requireContext(), ObjectiveDetailActivity::class.java).apply {
                putExtra("objectiveId", selectedObjective.id)
            }
            startActivity(intent)
        }, { updatedObjective ->
            // Handle objective update in background
            GlobalScope.launch(Dispatchers.IO) {
                objectiveRepository.updateObjective(updatedObjective)
                loadObjectives()
            }
        }, { objectives ->
            // Update incentive message based on the list of objectives
            updateIncentiveMessage(objectives)
        })

        recyclerView.adapter = adapter

        // Setup button to navigate to CreationActivity
        val createNewObjectiveButton: Button = view.findViewById(R.id.create_new_objective_btn)
        createNewObjectiveButton.setOnClickListener {
            val intent = Intent(requireContext(), CreationActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Called when the Fragment is visible to the user and actively running.
     */
    override fun onResume() {
        super.onResume()
        loadObjectives()
    }

    /**
     * Loads the objectives from the repository and updates the RecyclerView and incentive message.
     */
    private fun loadObjectives() {
        GlobalScope.launch(Dispatchers.Main) {
            val objectives = objectiveRepository.getAllUserObjectives(sessionManager.getUsername())
            adapter.updateObjectives(objectives)
            updateIncentiveMessage(objectives)
        }
    }

    /**
     * Updates the incentive message based on the completion percentage of objectives.
     */
    private fun updateIncentiveMessage(objectives: List<Objective>) {
        val completedObjectives = objectives.count { it.checked }
        val totalObjectives = objectives.size
        val completionPercentage = if (totalObjectives > 0) {
            (completedObjectives.toDouble() / totalObjectives * 100).toInt()
        } else {
            0
        }

        // Set incentive message based on the completion percentage
        incentiveMessage.text = when {
            completionPercentage == 100 -> getString(R.string.incentive_message_incredible)
            completionPercentage >= 75 -> getString(R.string.incentive_message_great_job)
            completionPercentage >= 50 -> getString(R.string.incentive_message_doing_well)
            completionPercentage > 0 -> getString(R.string.incentive_message_good_start)
            else -> getString(R.string.incentive_message_get_started)
        }
    }
}
