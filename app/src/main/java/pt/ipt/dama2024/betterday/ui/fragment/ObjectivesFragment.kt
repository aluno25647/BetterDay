package pt.ipt.dama2024.betterday.ui.fragment

import ObjectiveAdapter
import SessionManager
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.ui.activity.ObjectiveDetailActivity

class ObjectivesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ObjectiveAdapter
    private lateinit var objectiveRepository: ObjectiveRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.objectives_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewObjectives)
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onResume() {
        super.onResume()

        sessionManager = SessionManager(requireContext())
        objectiveRepository = ObjectiveRepository(requireContext())

        val objectives = objectiveRepository.getAllUserObjectives(sessionManager.getUsername())

        adapter = ObjectiveAdapter(objectives.toMutableList(), { selectedObjective ->
            val intent = Intent(requireContext(), ObjectiveDetailActivity::class.java).apply {
                putExtra("objectiveId", selectedObjective.id)
                putExtra("title", selectedObjective.title)
                putExtra("description", selectedObjective.description)
                putExtra("creationDate", selectedObjective.creationDate.time)
                putExtra("checked", selectedObjective.checked)
            }
            startActivity(intent)
        }, { updatedObjective ->
            // Atualiza o objetivo no banco de dados
            GlobalScope.launch(Dispatchers.IO) {
                objectiveRepository.updateObjective(updatedObjective)
            }
        })

        recyclerView.adapter = adapter
    }
}