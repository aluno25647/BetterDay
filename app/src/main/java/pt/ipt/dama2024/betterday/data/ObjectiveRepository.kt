package pt.ipt.dama2024.betterday.data

import android.content.Context
import pt.ipt.dama2024.betterday.model.Objective

/**
 * The ObjectiveRepository class provides a cleaner and more organized way to interact with the database
 * for objective-related operations. It acts as a mediator between the application and the database,
 * offering a simpler and cleaner communication with the database.
 *
 * @param context The application context, required to initialize the database.
 */
class ObjectiveRepository(context: Context) {
    private val db = Database(context)

    /**
     * Inserts a new objective into the database.
     *
     * @param objective The objective to be inserted.
     * @param username The username of the user associated with the objective.
     * @return The row ID of the newly inserted objective, or -1 if an error occurred.
     */
    fun insertObjective(objective: Objective, username: String): Long {
        return db.insertObjective(objective, username)
    }

    /**
     * Retrieves an objective by its ID from the database.
     *
     * @param id The ID of the objective to retrieve.
     * @return The Objective object if found, null otherwise.
     */
    fun getObjectiveById(id: Long): Objective? {
        return db.getObjectiveById(id)
    }

    /**
     * Retrieves all objectives associated with a specific user from the database.
     *
     * @param username The username of the user whose objectives are to be retrieved.
     * @return A list of objectives associated with the specified user.
     */
    fun getAllUserObjectives(username: String): List<Objective> {
        return db.getAllUserObjectives(username)
    }

    /**
     * Updates an existing objective in the database.
     *
     * @param objective The objective with updated information.
     * @return The number of rows affected.
     */
    fun updateObjective(objective: Objective): Int {
        return db.updateObjective(objective)
    }

    /**
     * Deletes an objective from the database.
     *
     * @param id The ID of the objective to be deleted.
     * @return The number of rows affected.
     */
    fun deleteObjective(id: Long): Int {
        return db.deleteObjective(id)
    }

    /**
     * Updates the 'checked' field of all objectives of a user to false.
     *
     * @return The number of objectives updated.
     */
    fun uncheckAllObjectives(username: String): Int {
        return db.uncheckAllObjectives(username)
    }
}
