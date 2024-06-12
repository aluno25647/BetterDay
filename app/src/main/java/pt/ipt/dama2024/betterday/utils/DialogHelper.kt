package pt.ipt.dama2024.betterday.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import pt.ipt.dama2024.betterday.R

/**
 * Utility object for displaying an "About App" dialog.
 */
object DialogHelper {

    /**
     * Displays an "About App" dialog.
     *
     * @param context The context used to inflate the layout and access resources.
     */
    fun showAboutDialog(context: Context) {
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_about_app, null)

        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(context)

        // Set the custom layout as the dialog view
        builder.setView(dialogView)

        // Set the dialog title
        builder.setTitle(context.getString(R.string.about_app))

        // Add a "Close" button to the dialog
        builder.setPositiveButton(context.getString(R.string.close)) { dialog, _ ->
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }
}
