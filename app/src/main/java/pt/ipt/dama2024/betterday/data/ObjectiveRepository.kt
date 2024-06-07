package pt.ipt.dama2024.betterday.data

import android.content.Context
import pt.ipt.dama2024.betterday.model.Objective

/**
 * The present class offers a cleaner usage of the database functions
 *  providing a simpler and cleaner communication with the database
 *
 *  These represent the CRUD
 */
class ObjectiveRepository(context: Context) {
    private val db = Database(context)

    /**
     * Create an Objective
     */
    fun insertObjective(objective: Objective): Long {
        return db.insertObjective(objective)
    }

    /**
     * Read all Objectives
     */
    fun getAllObjectives(): List<Objective> {
        return db.getAllObjectives()
    }

    /**
     * Update an Objective
     */
    fun updateObjective(objective: Objective): Int {
        return db.updateObjective(objective)
    }

    /**
     * Delete an Objective
     */
    fun deleteObjective(id: Long): Int {
        return db.deleteObjective(id)
    }
}
