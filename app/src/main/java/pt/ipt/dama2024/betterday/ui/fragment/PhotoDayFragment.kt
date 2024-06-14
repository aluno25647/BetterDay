package pt.ipt.dama2024.betterday.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.ui.activity.TakePhotoActivity

/**
 * SettingsFragment is responsible for displaying the settings options.
 * Users can change the app language and log out from the app.
 */
@Suppress("DEPRECATION") // the method to retrieve a result from another activity is deprecated
class PhotoDayFragment : Fragment() {

    private lateinit var imageViewPhoto: ImageView
    private lateinit var mapView: MapView
    private lateinit var buttonTakePhoto: Button

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

        // Find the MapView and set its properties
        mapView = view.findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)

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

            // Update UI or process the latitude, longitude, and photoByteArray as needed
            updateUI(latitude, longitude, photoByteArray)
        }
    }

    private fun updateUI(latitude: Double?, longitude: Double?, photoByteArray: ByteArray?) {
        // Update photo if available
        if (photoByteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.size)
            imageViewPhoto.setImageBitmap(bitmap)
        }

        // Handle latitude and longitude for mapView
        val defaultLatitude = 0.0
        val defaultLongitude = 0.0

        // Debug
        Log.d("Coordinates", "Latitude: $latitude, Longitude: $longitude, Default Latitude: $defaultLatitude, Default Longitude: $defaultLongitude")
        val startPoint = GeoPoint(latitude ?: defaultLatitude, longitude ?: defaultLongitude)
        mapView.controller.setCenter(startPoint)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume() // Needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause() // Needed for compass, my location overlays, v6.0.0 and up
    }

    companion object {
        private const val TAKE_PHOTO_REQUEST_CODE = 101
    }

}
