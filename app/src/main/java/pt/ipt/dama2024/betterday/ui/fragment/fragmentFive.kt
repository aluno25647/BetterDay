package pt.ipt.dama2024.betterday.ui.fragment

import SessionManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.ui.activity.LoginActivity

/**
 * FragmentFive
 */
class FragmentFive : Fragment() {

    private lateinit var sessionManager: SessionManager

    /**
     * Creates the view hierarchy associated with the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_five, container, false)
    }

    /**
     * Called immediately after the view created, to initialize UI components.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SessionManager
        sessionManager = SessionManager(requireContext())

        // Logout Button Click Listener
        view.findViewById<Button>(R.id.logout_btn).setOnClickListener {
            // Clear session data
            sessionManager.logout()

            Toast.makeText(requireContext(), getString(R.string.logout_successful), Toast.LENGTH_SHORT).show()

            // Redirect to login activity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}