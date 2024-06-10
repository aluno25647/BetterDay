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


class FragmentOne : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ObjectiveAdapter
    private lateinit var newObjectiveButton: FloatingActionButton
    private lateinit var objectiveRepository: ObjectiveRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_one, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewObjectives)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val objectives = objectiveRepository.getAllUserObjectives(sessionManager.getUsername())

        adapter = ObjectiveAdapter(objectives)
        recyclerView.adapter = adapter

        // Initialize FloatingActionButton
        newObjectiveButton = view.findViewById(R.id.buttonAddObjective)
        newObjectiveButton.setOnClickListener {
            // TODO add new objective action
        }

        return view
    }

}