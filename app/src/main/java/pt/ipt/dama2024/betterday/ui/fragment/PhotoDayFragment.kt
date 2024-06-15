package pt.ipt.dama2024.betterday.ui.fragment

import SessionManager
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
 * SettingsFragment is responsible for displaying the settings options.
 * Users can change the app language and log out from the app.
 */
@Suppress("DEPRECATION") // the method to retrieve a result from another activity is deprecated
class PhotoDayFragment : Fragment() {

    private lateinit var imageViewPhoto: ImageView
    private lateinit var buttonTakePhoto: Button
    private lateinit var userPhotoDay: UserPhotoDay

    private lateinit var sessionManager: SessionManager
    private lateinit var photoDayRepository: PhotoDayRepository

    private lateinit var gps_google: Button
    private lateinit var gps_values: TextView

    private val locationPermissionCode = 2

    //default values
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
        return inflater.inflate(R.layout.photo_day_fragment, container, false)
    }

    /**
     * Called immediately after the view is created, to initialize UI components.
     *
     * @param view The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize osmdroid configuration
        Configuration.getInstance()
            .load(requireContext(), requireActivity().getSharedPreferences("osmdroid",AppCompatActivity.MODE_PRIVATE))

        sessionManager = SessionManager(requireContext())
        photoDayRepository = PhotoDayRepository(requireContext())

        getGpsPermission() //TODO tentei meter as perms aqui (separando metade/metade)

        // Open Google Maps with predefined coordinates
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

        imageViewPhoto = view.findViewById(R.id.image_view_photo)
        buttonTakePhoto = view.findViewById(R.id.buttonTakePhoto)
        buttonTakePhoto.setOnClickListener{
            startTakePhotoActivityForResult()
        }
    }

    private fun startTakePhotoActivityForResult() {
        val intent = Intent(requireContext(), TakePhotoActivity::class.java)
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val latitude = data?.getDoubleExtra("latitude", 0.0)
            val longitude = data?.getDoubleExtra("longitude", 0.0)
            val photoByteArray = data?.getByteArrayExtra("photo")

//TODO nome apenas
            updateUI(latitude, longitude, photoByteArray)
        }
    }

    private fun updateUI(latitude: Double?, longitude: Double?, photoByteArray: ByteArray?) {
        // Update photo if available
        if (photoByteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.size)
            imageViewPhoto.setImageBitmap(bitmap)
        }

        // Default for the log
        val defaultLatitude = 39.6071754
        val defaultLongitude = -8.406121

        // Handle latitude and longitude
        if (latitude != null) {
            this@PhotoDayFragment.lat = latitude
        }
        if (longitude != null) {
            this@PhotoDayFragment.lng = longitude
        }
        // Debug
        Log.d("Coordinates", "Latitude: $latitude, Longitude: $longitude, Default Latitude: $defaultLatitude, Default Longitude: $defaultLongitude")

        //display coordinates
        gps_values.text =
            "Latitude: ${lat} ,\n Longitude: ${lng}"

    }

    override fun onResume() {
        super.onResume()
        loadInfo() // TODO o grande issue rn é isto, mas esta com o mesmo principio que o catsfrag

    }

    private fun loadInfo() {
        userPhotoDay = photoDayRepository.getUserCurrentPhotoDayById(sessionManager.getUsername())
        userPhotoDay.let {if (userPhotoDay.photo != null && userPhotoDay.latitude != null && userPhotoDay.longitude != null){
            updateUI(userPhotoDay.latitude, userPhotoDay.longitude, userPhotoDay.photo)
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {
        private const val TAKE_PHOTO_REQUEST_CODE = 101
        private val REQUIRED_PERMISSIONS_GPS = Manifest.permission.ACCESS_FINE_LOCATION
    }

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
     * Asks for authorization to access GPS
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
