import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.model.Objective

/**
 * Adapter for managing a list of objectives in a RecyclerView.
 * @param objectives MutableList of Objective objects to be displayed.
 * @param onItemClicked Lambda function to handle item click events.
 * @param updateObjective Lambda function to update an objective.
 * @param onItemChecked Lambda function to handle item check events and update the incentive message.
 */
class ObjectiveAdapter(
    private var objectives: MutableList<Objective>,
    private val onItemClicked: (Objective) -> Unit,
    private val updateObjective: (Objective) -> Unit,
    private val onItemChecked: (List<Objective>) -> Unit
) : RecyclerView.Adapter<ObjectiveAdapter.ObjectiveViewHolder>() {

    /**
     * ViewHolder class for holding the view elements for each objective item.
     * @param itemView The view of the objective item.
     */
    inner class ObjectiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitleObjective)
        private val descriptionTextView: TextView =
            itemView.findViewById(R.id.textViewDescriptionObjective)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxObjective)

        /**
         * Binds an Objective object to the view elements.
         * @param objective The Objective object to bind to the view.
         */
        fun bind(objective: Objective) {
            titleTextView.text = objective.title
            descriptionTextView.text = objective.description
            checkBox.isChecked = objective.checked

            itemView.setOnClickListener {
                onItemClicked(objective)
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                objective.checked = isChecked
                updateObjective(objective)
                onItemChecked(objectives)
            }
        }
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ObjectiveViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectiveViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.objective_item, parent, false)
        return ObjectiveViewHolder(itemView)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ObjectiveViewHolder, position: Int) {
        val currentObjective = objectives[position]
        holder.bind(currentObjective)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = objectives.size

    /**
     * Updates the list of objectives and notifies the adapter to refresh the data.
     * @param newObjectives The new list of Objective objects to be displayed.
     */
    fun updateObjectives(newObjectives: List<Objective>) {
        objectives.clear()
        objectives.addAll(newObjectives)
        notifyDataSetChanged()
    }
}
