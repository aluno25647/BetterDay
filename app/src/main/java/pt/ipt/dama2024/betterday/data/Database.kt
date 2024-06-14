package pt.ipt.dama2024.betterday.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import pt.ipt.dama2024.betterday.model.Objective
import java.security.MessageDigest
import java.util.Date
import java.util.UUID

/**
 * The Database class manages the SQLite database operations for BetterDay application.
 *
 * @param context The application context.
 */
class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * Declaration of variables representing the different column names for the data base
     *  as well as version and name of the same
     */
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "objectives.db"

        // Objectives table columns
        private const val TABLE_OBJECTIVES = "objectives"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CREATION_DATE = "creationDate"
        private const val COLUMN_CHECKED = "checked"
        private const val COLUMN_AUTHOR = "author"

        // Users table columns
        private const val TABLE_USERS = "users"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_TOKEN = "token"
        private const val COLUMN_CURRENT_DATE = "currentDate"
    }

    /**
     * Here are declared the different columns and respective data types of each on creation of the datatable
     *
     * SQLite accepts 5 types of data: NULL, INTEGER, REAL, TEXT, BLOB
     * Later conversions will be needed to these data types
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createObjectivesTable = ("CREATE TABLE $TABLE_OBJECTIVES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_TITLE TEXT, "
                + "$COLUMN_DESCRIPTION TEXT, "
                + "$COLUMN_CREATION_DATE INTEGER, "
                + "$COLUMN_CHECKED INTEGER, "
                + "$COLUMN_AUTHOR TEXT, ")

        val createUsersTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_USERNAME TEXT UNIQUE, "
                + "$COLUMN_PASSWORD TEXT, "
                + "$COLUMN_EMAIL TEXT UNIQUE, "
                + "$COLUMN_TOKEN TEXT, "
                + "$COLUMN_CURRENT_DATE INTEGER)")

        db.execSQL(createObjectivesTable)
        db.execSQL(createUsersTable)
    }

    /**
     * This function exists with the purpose of help with changes on the event of
     *  adding/dropping/changing the tables and columns
     *
     *  This prevents incompatibility of types or structure
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_OBJECTIVES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    /**
     * InsertObjective allows the insertion of a new Objective in the Database
     *  this action is correlated to the creation of a new objective
     *
     * Conversions are highlighted with comments when applied
     */
    fun insertObjective(objective: Objective, username: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, objective.title)
            put(COLUMN_DESCRIPTION, objective.description)
            put(COLUMN_CREATION_DATE, objective.creationDate.time) // Convert Date to Long
            put(COLUMN_CHECKED, if (objective.checked) 1 else 0)
            put(COLUMN_AUTHOR, username)
        }
        return db.insert(TABLE_OBJECTIVES, null, values)
    }

    /**
     * Retrieves an objective by its ID from the database.
     *
     * @param id The ID of the objective to retrieve.
     * @return The Objective object if found, null otherwise.
     */
    fun getObjectiveById(id: Long): Objective? {
        val db = this.readableDatabase
        var objective: Objective? = null

        val cursor = db.query(
            TABLE_OBJECTIVES,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use { cursor ->
            if (cursor.moveToFirst()) {
                val titleIndex = cursor.getColumnIndexOrThrow(COLUMN_TITLE)
                val descriptionIndex = cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)
                val creationDateIndex = cursor.getColumnIndexOrThrow(COLUMN_CREATION_DATE)
                val checkedIndex = cursor.getColumnIndexOrThrow(COLUMN_CHECKED)
                val authorIndex = cursor.getColumnIndexOrThrow(COLUMN_AUTHOR)

                val title = cursor.getString(titleIndex)
                val description = cursor.getString(descriptionIndex)
                val creationDate = Date(cursor.getLong(creationDateIndex))
                val checked = cursor.getInt(checkedIndex) == 1
                val author = cursor.getString(authorIndex)

                objective = Objective(
                    id,
                    title,
                    description,
                    creationDate,
                    checked,
                    author
                )
            }
        }

        cursor.close()
        db.close()
        return objective
    }

    /**
     * Here is prepared the function that returns all objectives currently in the database
     *  in a List of Objectives format
     *
     * Conversions are highlighted with comments when applied
     */
    fun getAllUserObjectives(username: String): List<Objective> {
        val objectives = mutableListOf<Objective>()
        val db = this.readableDatabase

        val cursor = db.query(TABLE_OBJECTIVES, null, "$COLUMN_AUTHOR = ?", arrayOf(username), null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val creationDate = Date(getLong(getColumnIndexOrThrow(COLUMN_CREATION_DATE))) // Convert Long to Date
                val checked = getInt(getColumnIndexOrThrow(COLUMN_CHECKED)) == 1 // Convert int to boolean
                val author = getString(getColumnIndexOrThrow(COLUMN_AUTHOR))
                objectives.add(Objective(id, title, description, creationDate, checked, author))
            }
        }
        cursor.close()
        return objectives
    }

    /**
     * The update function allows the update of an already created Objective
     *  updating that information in the database
     *
     *  Conversions are highlighted with comments when applied
     */
    fun updateObjective(objective: Objective): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, objective.title)
            put(COLUMN_DESCRIPTION, objective.description)
            put(COLUMN_CREATION_DATE, objective.creationDate.time) // Convert Date to Long
            put(COLUMN_CHECKED, if (objective.checked) 1 else 0) // Convert boolean to int
            put(COLUMN_AUTHOR, objective.author)
        }
        return db.update(TABLE_OBJECTIVES, values, "$COLUMN_ID = ?", arrayOf(objective.id.toString()))
    }

    /**
     * Updates the 'checked' field of all objectives for the specified user to false.
     *
     * @param username The username of the user whose objectives are to be updated.
     * @return The number of objectives updated.
     */
    fun uncheckAllObjectives(username: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CHECKED, 0) // Set checked to false (0)
        }
        val selection = "$COLUMN_AUTHOR = ?"
        val selectionArgs = arrayOf(username)
        return db.update(TABLE_OBJECTIVES, values, selection, selectionArgs)
    }


    /**
     * This function allows the deletion of an Objective that no longer is needed in the database
     */
    fun deleteObjective(id: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_OBJECTIVES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    /**
     * Adds a new user to the database.
     *
     * @param username The username of the user to be added.
     * @param password The password of the user to be added.
     * @param email The email of the user to be added.
     * @param currentDate The current date to be added.
     * @return True if the user was added successfully, false otherwise.
     */
    fun addUser(username: String, password: String, email: String, currentDate: Date): Boolean {
        if (isUsernameAlreadyInUse(username)) {
            return false
        }
        if (isEmailAlreadyInUse(email)) {
            return false
        }
        val db = this.writableDatabase
        val hashedPassword = hashPassword(password)
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, hashedPassword)
            put(COLUMN_EMAIL, email)
            put(COLUMN_CURRENT_DATE, currentDate.time) // Convert Date to Long
        }
        val success = db.insert(TABLE_USERS, null, values)
        db.close()
        return success != -1L
    }

    /**
     * Authenticates a user with the provided username and password.
     * If authentication is successful, a token is generated, stored in the database,
     * and returned.
     *
     * @param username The username of the user to authenticate.
     * @param password The password of the user to authenticate.
     * @return The generated token if authentication is successful, null otherwise.
     */
    fun authenticateUser(username: String, password: String): String? {
        val db = this.writableDatabase
        val hashedPassword = hashPassword(password)
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, hashedPassword))

        var token: String? = null

        if (cursor.count > 0) {
            token = generateToken()
            val contentValues = ContentValues()
            contentValues.put(COLUMN_TOKEN, token)
            db.update(TABLE_USERS, contentValues, "$COLUMN_USERNAME = ?", arrayOf(username))
        }

        cursor.close()
        db.close()
        return token
    }

    /**
     * Verifies if the provided token matches the stored token for the given username.
     *
     * This method retrieves the token associated with the specified username from the database
     * and compares it with the provided token. It returns true if the tokens match, otherwise false.
     *
     * @param username The username whose token needs to be verified.
     * @param token The token to be verified.
     * @return True if the provided token matches the stored token, false otherwise.
     */
    fun verifyToken(username: String, token: String): Boolean {
        // Get a readable instance of the database
        val db = this.readableDatabase

        // Define the query to retrieve the token for the given username
        val query = "SELECT $COLUMN_TOKEN FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"

        // Execute the query with the username as a parameter
        val cursor = db.rawQuery(query, arrayOf(username))

        var storedToken: String? = null
        // Check if the cursor contains a result and retrieve the token
        if (cursor.moveToFirst()) {
            storedToken = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOKEN))
        }

        // Close the cursor to release resources
        cursor.close()

        // Close the database connection
        db.close()

        // Compare the stored token with the provided token and return the result
        return storedToken == token
    }

    /**
     * Retrieves the current date associated with the specified username from the Users table.
     *
     * @param username The username of the user whose current date is to be retrieved.
     * @return The current date as a [Date] object if found, null otherwise.
     */
    fun getCurrentDateByUsername(username: String): Date? {
        val db = this.readableDatabase
        var currentDate: Date? = null

        val query = "SELECT $COLUMN_CURRENT_DATE FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        if (cursor.moveToFirst()) {
            val currentDateMillis = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CURRENT_DATE))
            currentDate = Date(currentDateMillis)
        }

        cursor.close()
        db.close()
        return currentDate
    }

    /**
     * Updates the current date for the specified user in the database.
     *
     * @param username The username of the user whose current date is to be updated.
     * @param currentDate The new current date to be updated in the database.
     * @return True if the current date was successfully updated, false otherwise.
     */
    fun updateCurrentDateByUsername(username: String, currentDate: Date): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_CURRENT_DATE, currentDate.time) // Assuming COLUMN_CURRENT_DATE is the column name for storing the date
        }
        val whereClause = "$COLUMN_USERNAME = ?"
        val whereArgs = arrayOf(username)
        val rowsAffected = db.update(TABLE_USERS, contentValues, whereClause, whereArgs)
        return rowsAffected > 0
    }

    /**
     * Generates a new token for the user.
     *
     * @return A new token.
     */
    private fun generateToken(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * Hashes the provided password using SHA-256 algorithm.
     *
     * @param password The password to be hashed.
     * @return The hashed password as a Base64 encoded string.
     */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return Base64.encodeToString(hashBytes, Base64.DEFAULT)
    }

    /**
     * Checks if the specified username is already in use.
     *
     * @param username The username to be checked.
     * @return True if the username is already in use, false otherwise.
     */
    fun isUsernameAlreadyInUse(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        db.close()
        return count > 0
    }

    /**
     * Checks if the specified email is already in use.
     *
     * @param email The email to be checked.
     * @return True if the email is already in use, false otherwise.
     */
    fun isEmailAlreadyInUse(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        db.close()
        return count > 0
    }
}
