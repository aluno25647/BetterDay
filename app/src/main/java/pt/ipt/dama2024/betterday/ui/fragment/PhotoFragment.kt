package pt.ipt.dama2024.betterday.ui.fragment

import SessionManager
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.PhotoDayRepository
import pt.ipt.dama2024.betterday.model.UserPhotoDay
import pt.ipt.dama2024.betterday.ui.activity.TakePhotoActivity

/**
 * Fragment responsible for capturing and displaying photos with associated GPS coordinates.
 */
@Suppress("DEPRECATION") // Deprecated method to retrieve results from another activity
class PhotoFragment : Fragment() {

    private lateinit var buttonTakePhoto: Button
    private lateinit var userPhotoDay: UserPhotoDay

    private lateinit var sessionManager: SessionManager
    private lateinit var photoDayRepository: PhotoDayRepository

    private lateinit var gps_google: Button
    private lateinit var gps_values: TextView

    private val locationPermissionCode = 2

    // Default coordinates
    private var lat = 39.6071754
    private var lng = -8.406121

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
        return inflater.inflate(R.layout.photo_fragment, container, false)
    }

    /**
     * Called immediately after the view is created, to initialize UI components and set up listeners.
     *
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize osmdroid configuration
        Configuration.getInstance()
            .load(requireContext(), requireActivity().getSharedPreferences("osmdroid", AppCompatActivity.MODE_PRIVATE))

        sessionManager = SessionManager(requireContext())
        photoDayRepository = PhotoDayRepository(requireContext())

        // Request GPS permission if not granted
        getGpsPermission()

        // Setup Google Maps button
        gps_google = view.findViewById(R.id.GPS_GOOGLE)
        gps_values = view.findViewById(R.id.GPS_Values)

        gps_google.setOnClickListener {

            // Construct the geo URI
            val geoUri = "http://maps.google.com/maps?q=loc:$lat,$lng"

            // Create an Intent to open a web browser
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))

            // Verify if there is a web browser available to handle the intent
            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                // Handle situation where no web browser is available
                Toast.makeText(requireContext(), "No app available to handle this action", Toast.LENGTH_SHORT).show()
            }
        }

        // Setup Take Photo button
        buttonTakePhoto = view.findViewById(R.id.buttonTakePhoto)
        buttonTakePhoto.setOnClickListener {
            startTakePhotoActivityForResult()
        }
    }

    /**
     * Starts the TakePhotoActivity to capture a photo.
     */
    private fun startTakePhotoActivityForResult() {
        val intent = Intent(requireContext(), TakePhotoActivity::class.java)
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
    }

    /**
     * Handles the result returned from the TakePhotoActivity.
     *
     * @param requestCode The request code passed to startActivityForResult().
     * @param resultCode The result code returned from the activity.
     * @param data An Intent with the result data.
     */
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val latitude = data?.getDoubleExtra("latitude", 0.0)
            val longitude = data?.getDoubleExtra("longitude", 0.0)

            updateUI(latitude, longitude)
        }
    }

    /**
     * Updates the UI with the given latitude and longitude coordinates.
     *
     * @param latitude The latitude coordinate to display.
     * @param longitude The longitude coordinate to display.
     */
    private fun updateUI(latitude: Double?, longitude: Double?) {

        // Default coordinates for logging
        val defaultLatitude = 39.6071754
        val defaultLongitude = -8.406121

        // Update fragment's latitude and longitude if provided
        if (latitude != null) {
            this@PhotoFragment.lat = latitude
        }
        if (longitude != null) {
            this@PhotoFragment.lng = longitude
        }

        // Debug logging
        Log.d("Coordinates", "Latitude: $latitude, Longitude: $longitude, Default Latitude: $defaultLatitude, Default Longitude: $defaultLongitude")

        // Display coordinates in the UI
        gps_values.text = "Latitude: ${lat} ,\n Longitude: ${lng}"
    }

    /**
     * Loads information when the fragment resumes.
     */
    override fun onResume() {
        super.onResume()
        loadInfo()
    }

    /**
     * Loads information from the repository.
     */
    private fun loadInfo() {
        userPhotoDay = photoDayRepository.getUserCurrentPhotoDayById(sessionManager.getUsername())
        if (userPhotoDay.photo != null && userPhotoDay.latitude != null) {
            updateUI(userPhotoDay.latitude, userPhotoDay.longitude)
        }
    }

    /**
     * Pauses any operations when the fragment is paused.
     */
    override fun onPause() {
        super.onPause()
        // Any operations to be done on fragment pause
    }

    companion object {
        private const val TAKE_PHOTO_REQUEST_CODE = 101
    }

    /**
     * Requests permission to access GPS location.
     */
    private fun getGpsPermission() {
        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
    }

    /**
     * Handles the result of permission requests.
     *
     * @param requestCode The request code passed to requestPermissions().
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(),
                    getString(R.string.gps_permission_granted), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.gps_permission_denied), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
