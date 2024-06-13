import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.model.Objective

class ObjectiveAdapter(
    private val objectives: MutableList<Objective>,
    private val onItemClicked: (Objective) -> Unit,
    private val updateObjective: (Objective) -> Unit
) : RecyclerView.Adapter<ObjectiveAdapter.ObjectiveViewHolder>() {

    inner class ObjectiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitleObjective)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescriptionObjective)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxObjective)

        fun bind(objective: Objective) {
            titleTextView.text = objective.title
            descriptionTextView.text = objective.description
            checkBox.isChecked = objective.checked

            itemView.setOnClickListener {
                onItemClicked(objective)
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                objective.checked = isChecked
                // Atualiza o estado no banco de dados
                updateObjective(objective)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectiveViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.objective_item, parent, false)
        return ObjectiveViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ObjectiveViewHolder, position: Int) {
        val currentObjective = objectives[position]
        holder.bind(currentObjective)
    }

    override fun getItemCount() = objectives.size
}
