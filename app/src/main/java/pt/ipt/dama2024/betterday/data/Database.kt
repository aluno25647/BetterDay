package pt.ipt.dama2024.betterday.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pt.ipt.dama2024.betterday.model.Objective
import android.content.ContentValues
import pt.ipt.dama2024.betterday.model.Frequency
import java.util.Date

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * Declaration of variables representing the different column names for the data base
     *  as well as version and name of the same
     */
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "objectives.db"

        private const val TABLE_NAME = "objectives"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_FREQUENCY = "frequency"
        private const val COLUMN_CREATION_DATE = "creationDate"
        private const val COLUMN_DEADLINE = "deadline"
        private const val COLUMN_CHECKED = "checked"
        private const val COLUMN_AUTHOR = "author"
    }

    /**
     * Here are declared the different columns and respective data types of each on creation of the datatable
     *
     * SQLite accepts 5 types of data: NULL, INTEGER, REAL, TEXT, BLOB
     * Later conversions will be needed to these data types
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_TITLE TEXT, "
                + "$COLUMN_DESCRIPTION TEXT, "
                + "$COLUMN_FREQUENCY TEXT, "
                + "$COLUMN_CREATION_DATE INTEGER, "
                + "$COLUMN_DEADLINE INTEGER, "
                + "$COLUMN_CHECKED INTEGER, "
                + "$COLUMN_AUTHOR TEXT)")
        db.execSQL(createTable)
    }

    /**
     * This function exists with the purpose of help with changes on the event of
     *  adding/dropping/changing the tables and columns
     *
     *  This prevents incompatibility of types or structure
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
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
        }
        return db.insert(TABLE_NAME, null, values)
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
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
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
                objectives.add(Objective(id, title, description, frequency, creationDate, deadline, checked, author))
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
            put(COLUMN_FREQUENCY, objective.frequency.name) // Convert enum to string
            put(COLUMN_CREATION_DATE, objective.creationDate.time) // Convert Date to Long
            put(COLUMN_DEADLINE, objective.deadline.time) // Convert Date to Long
            put(COLUMN_CHECKED, if (objective.checked) 1 else 0) // Convert boolean to int
            put(COLUMN_AUTHOR, objective.author)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(objective.id.toString()))
    }

    /**
     * This function allows the deletion of an Objective that no longer is needed in the database
     */
    fun deleteObjective(id: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

}