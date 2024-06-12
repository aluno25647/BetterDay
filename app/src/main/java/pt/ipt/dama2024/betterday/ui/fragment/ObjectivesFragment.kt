package pt.ipt.dama2024.betterday.ui.fragment
import SessionManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.ui.adapter.ObjectiveAdapter

class ObjectivesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ObjectiveAdapter
    private lateinit var newObjectiveButton: FloatingActionButton

    private lateinit var objectiveRepository: ObjectiveRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.objectives_fragment, container, false)
    }

    /**
     * Called immediately after the view created, to initialize UI components.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewObjectives)
        recyclerView.layoutManager = LinearLayoutManager(context)

    }
    /**
     * Called when the Fragment is visible to the user
     */
    override fun onResume() {
        super.onResume()
        // Initialize SessionManager
        sessionManager = SessionManager(requireContext())

        // Initialize ObjectiveRepository
        objectiveRepository = ObjectiveRepository(requireContext())

        val objectives = objectiveRepository.getAllUserObjectives(sessionManager.getUsername())

        adapter = ObjectiveAdapter(objectives)
        recyclerView.adapter = adapter
    }

}