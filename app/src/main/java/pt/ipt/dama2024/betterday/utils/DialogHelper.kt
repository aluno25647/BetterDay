package pt.ipt.dama2024.betterday.utils

import android.app.AlertDialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
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

    /**
     * Displays a confirmation dialog for logging out.
     *
     * @param context The context used to build the dialog.
     * @param onConfirm The action to perform when the user confirms the logout.
     */
    fun showLogoutConfirmationDialog(context: Context, onConfirm: () -> Unit) {
        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(context)

        // Set the dialog message and title
        builder.setTitle(context.getString(R.string.logout))
        builder.setMessage(context.getString(R.string.confirm_logout))

        // Add "Yes" and "No" buttons to the dialog
        builder.setPositiveButton(context.getString(R.string.yes)) { dialog, _ ->
            // Perform the logout action
            onConfirm()
            dialog.dismiss()
        }

        builder.setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
            // Dismiss the dialog
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * Displays a confirmation dialog for deleting an objective.
     *
     * @param context The context used to build the dialog.
     * @param onConfirm The action to perform when the user confirms the deletion.
     */
    fun showDeleteConfirmationDialog(context: Context, onConfirm: () -> Unit) {
        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(context)

        // Set the dialog message and title
        builder.setTitle(context.getString(R.string.delete_objective))
        builder.setMessage(context.getString(R.string.confirm_delete_objective))

        // Add "Yes" and "No" buttons to the dialog
        builder.setPositiveButton(context.getString(R.string.yes)) { dialog, _ ->
            // Perform the delete action
            onConfirm()
            dialog.dismiss()
        }

        builder.setNegativeButton(context.getString(R.string.no)) { dialog, _ ->
            // Dismiss the dialog
            dialog.dismiss()
        }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * Displays a dialog for login requirements.
     *
     * @param context The context used to build the dialog.
     */
    fun showLoginRequirementsDialog(context: Context) {
        // Create a TextView for the dialog message
        val messageTextView = TextView(context).apply {
            text = context.getString(R.string.requirements)
            gravity = Gravity.CENTER
            setPadding(25, 25, 25, 25)
            textSize = 16f
        }

        // Create an AlertDialog builder
        val builder = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.requirements_title))
            .setView(messageTextView)
            .setPositiveButton(context.getString(R.string.close)) { dialog, _ ->
                dialog.dismiss()
            }

        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
    }

}
