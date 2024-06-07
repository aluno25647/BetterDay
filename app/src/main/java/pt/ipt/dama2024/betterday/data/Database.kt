package pt.ipt.dama2024.betterday.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pt.ipt.dama2024.betterday.model.Objective
import android.content.ContentValues
import pt.ipt.dama2024.betterday.model.Frequency
import java.security.MessageDigest
import java.util.Date
import java.security.NoSuchAlgorithmException
import android.util.Base64

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
        private const val COLUMN_FREQUENCY = "frequency"
        private const val COLUMN_CREATION_DATE = "creationDate"
        private const val COLUMN_DEADLINE = "deadline"
        private const val COLUMN_CHECKED = "checked"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_PHOTO1 = "photo1"
        private const val COLUMN_PHOTO2 = "photo2"
        private const val COLUMN_PHOTO3 = "photo3"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"

        // Users table columns
        private const val TABLE_USERS = "users"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
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
                + "$COLUMN_FREQUENCY TEXT, "
                + "$COLUMN_CREATION_DATE INTEGER, "
                + "$COLUMN_DEADLINE INTEGER, "
                + "$COLUMN_CHECKED INTEGER, "
                + "$COLUMN_AUTHOR TEXT)"
                + "$COLUMN_PHOTO1 BLOB, "
                + "$COLUMN_PHOTO2 BLOB, "
                + "$COLUMN_PHOTO3 BLOB, "
                + "$COLUMN_LATITUDE REAL, "
                + "$COLUMN_LONGITUDE REAL)")

        val createUsersTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_USERNAME TEXT UNIQUE, "
                + "$COLUMN_PASSWORD TEXT, "
                + "$COLUMN_EMAIL TEXT UNIQUE)")

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
    fun insertObjective(objective: Objective): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, objective.title)
            put(COLUMN_DESCRIPTION, objective.description)
            put(COLUMN_FREQUENCY, objective.frequency.name) // Retrieve Enum Option
            put(COLUMN_CREATION_DATE, objective.creationDate.time) // Convert Date to Long
            put(COLUMN_DEADLINE, objective.deadline.time) // Convert Date to Long
            put(COLUMN_CHECKED, if (objective.checked) 1 else 0)
            put(COLUMN_AUTHOR, objective.author)
            put(COLUMN_PHOTO1, objective.photo1)
            put(COLUMN_PHOTO2, objective.photo2)
            put(COLUMN_PHOTO3, objective.photo3)
            put(COLUMN_LATITUDE, objective.latitude)
            put(COLUMN_LONGITUDE, objective.longitude)
        }
        return db.insert(TABLE_OBJECTIVES, null, values)
    }

    /**
     * Here is prepared the function that returns all objectives currently in the database
     *  in a List of Objectives format
     *
     * Conversions are highlighted with comments when applied
     */
    fun getAllObjectives(): List<Objective> {
        val objectives = mutableListOf<Objective>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_OBJECTIVES, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val frequency = Frequency.valueOf(getString(getColumnIndexOrThrow(COLUMN_FREQUENCY))) // Retrieve Enum Option
                val creationDate = Date(getLong(getColumnIndexOrThrow(COLUMN_CREATION_DATE))) // Convert Long to Date
                val deadline = Date(getLong(getColumnIndexOrThrow(COLUMN_DEADLINE))) // Convert Long to Date
                val checked = getInt(getColumnIndexOrThrow(COLUMN_CHECKED)) == 1 // Convert int to boolean
                val author = getString(getColumnIndexOrThrow(COLUMN_AUTHOR))
                val photo1 = getBlob(getColumnIndexOrThrow(COLUMN_PHOTO1))
                val photo2 = getBlob(getColumnIndexOrThrow(COLUMN_PHOTO2))
                val photo3 = getBlob(getColumnIndexOrThrow(COLUMN_PHOTO3))
                val latitude = getDouble(getColumnIndexOrThrow(COLUMN_LATITUDE))
                val longitude = getDouble(getColumnIndexOrThrow(COLUMN_LONGITUDE))
                objectives.add(Objective(id, title, description, frequency, creationDate, deadline, checked, author, photo1, photo2, photo3, latitude, longitude))            }
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
            put(COLUMN_FREQUENCY, objective.frequency.name) // Convert enum to string
            put(COLUMN_CREATION_DATE, objective.creationDate.time) // Convert Date to Long
            put(COLUMN_DEADLINE, objective.deadline.time) // Convert Date to Long
            put(COLUMN_CHECKED, if (objective.checked) 1 else 0) // Convert boolean to int
            put(COLUMN_AUTHOR, objective.author)
            put(COLUMN_PHOTO1, objective.photo1)
            put(COLUMN_PHOTO2, objective.photo2)
            put(COLUMN_PHOTO3, objective.photo3)
            put(COLUMN_LATITUDE, objective.latitude)
            put(COLUMN_LONGITUDE, objective.longitude)
        }
        return db.update(TABLE_OBJECTIVES, values, "$COLUMN_ID = ?", arrayOf(objective.id.toString()))
    }

    /**
     * This function allows the deletion of an Objective that no longer is needed in the database
     */
    fun deleteObjective(id: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_OBJECTIVES, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }


    // Methods for managing users

    fun addUser(username: String, password: String, email: String): Boolean {

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
        }
        val success = db.insert(TABLE_USERS, null, values)
        db.close()
        return success != -1L
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val hashedPassword = hashPassword(password)
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, hashedPassword))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    private fun hashPassword(password: String): String {
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(password.toByteArray())
            return Base64.encodeToString(hashBytes, Base64.DEFAULT)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            // Handle error
            return "Something went wrong"
        }
    }

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