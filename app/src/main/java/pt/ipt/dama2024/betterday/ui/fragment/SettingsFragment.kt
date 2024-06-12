package pt.ipt.dama2024.betterday.ui.fragment

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.ui.activity.LoginActivity
import pt.ipt.dama2024.betterday.utils.DialogHelper

/**
 * SettingsFragment is responsible for displaying the settings options.
 * Users can change the app language and log out from the app.
 */
class SettingsFragment : Fragment() {

    private lateinit var sessionManager: SessionManager

    /**
     * Creates the view hierarchy associated with the fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    /**
     * Called immediately after the view is created, to initialize UI components.
     *
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SessionManager
        sessionManager = SessionManager(requireContext())

        // Find the logout button and set its click listener
        val logoutButton = view.findViewById<Button>(R.id.logout_btn)
        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Find the language radio group and set its change listener
        val languageRadioGroup = view.findViewById<RadioGroup>(R.id.language_radio_group)
        val englishRadioButton = view.findViewById<RadioButton>(R.id.english_radio_button)
        val portugueseRadioButton = view.findViewById<RadioButton>(R.id.portuguese_radio_button)

        // Set current language selection based on saved preference
        val currentLanguage = sessionManager.getCurrentLanguage()
        if (currentLanguage == "pt") {
            portugueseRadioButton.isChecked = true
        } else {
            englishRadioButton.isChecked = true
        }

        languageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.english_radio_button -> setLocale("en")
                R.id.portuguese_radio_button -> setLocale("pt")
            }
        }

        // About the App activity
        val aboutButton: Button = view.findViewById(R.id.button_about_app)
        aboutButton.setOnClickListener {
            DialogHelper.showAboutDialog(requireContext())
        }
    }

    /**
     * Sets the locale for the app and restarts the activity to apply changes.
     *
     * @param languageCode The language code to set (e.g., "en" for English, "pt" for Portuguese).
     */
    private fun setLocale(languageCode: String) {
        sessionManager.setLanguage(languageCode)

        // Recreate the activity to apply the new language
        requireActivity().recreate()
    }

    /**
     * Shows a confirmation dialog before logging out.
     */
    private fun showLogoutConfirmationDialog() {
        DialogHelper.showLogoutConfirmationDialog(requireContext()) {
            handleLogout()
        }
    }

    /**
     * Handles the logout process, including clearing session data and navigating to the login screen.
     */
    private fun handleLogout() {
        // Clear session data
        sessionManager.logout()

        // Show a toast message indicating successful logout
        Toast.makeText(requireContext(), getString(R.string.logout_successful), Toast.LENGTH_SHORT)
            .show()

        // Redirect to login activity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
