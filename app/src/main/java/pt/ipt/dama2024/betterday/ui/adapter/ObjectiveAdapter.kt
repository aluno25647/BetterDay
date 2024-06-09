package pt.ipt.dama2024.betterday.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.model.Objective


/**
 * Adapter class for the RecyclerView that displays a list of objectives
 */
class ObjectiveAdapter(private val objectives: List<Objective>) :
    RecyclerView.Adapter<ObjectiveAdapter.ObjectiveViewHolder>() {

    /**
     * Inner ViewHolder class that holds references to each item displayed on the Objective
     */
    inner class ObjectiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitleObjective)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescriptionObjective)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxObjective)
    }

    /**
     * Setups the objective_item into the view to be displayed
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectiveViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.objective_item, parent, false)
        return ObjectiveViewHolder(itemView)
    }

    /**
     *  Places the data into the correct places
     */
    override fun onBindViewHolder(holder: ObjectiveViewHolder, position: Int) {
        val currentObjective = objectives[position]
        holder.titleTextView.text = currentObjective.title // Set the title
        holder.descriptionTextView.text = currentObjective.description // Set the description
        holder.checkBox.isChecked = currentObjective.checked // Set the checkbox state
    }

    /**
     * Indicates the number of objectives
     */
    override fun getItemCount() = objectives.size
}
