package pt.ipt.dama2024.betterday.ui.activity

import SessionManager
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.PhotoDayRepository
import pt.ipt.dama2024.betterday.databinding.ActivityTakePhotoBinding
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Most of this was learned and used in class
 * The difference lies in the adaptation from only saving locally to also saving in the database
 * https://github.com/IPT-MEI-DAMA-2023-2024/Camera-X
 */
class TakePhotoActivity : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityTakePhotoBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var locationManager: LocationManager
    private lateinit var sessionManager: SessionManager
    private lateinit var photoDayRepository: PhotoDayRepository
    private var location: Location? = null
    private val locationPermissionCode = 2
    private var latitude = 0.0
    private var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        photoDayRepository = PhotoDayRepository(this)
        // Check and request necessary permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }
        getLocation()


        // Set click listener for capture button
        binding.imageCaptureButton.setOnClickListener {
            takePhoto()

        }
    }

    /**
     * Function to check if all required permissions are granted
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Function to request the necessary permissions
     */
    private fun requestPermissions() {
        requestMultiplePermissions.launch(REQUIRED_PERMISSIONS)
    }

    /**
     * Function that define if all permissions has been granted
     */
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_request_to_use_camera_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

    /**
     * Function to start the camera and bind it to the lifecycle of the activity
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE)
                as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        /*  Inicia o GPS, que vai autilizar a posição de 5 em 5 segundos,
            se a nova localização estiver pelo menos a 5 metros da última
            localização  */
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            500,
            5f,
            this
        )
    }

    /**
     * function to take the photo and retrieve coordinates
     */
    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture

        // Create a time stamped name
        // import java.text.SimpleDateFormat
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        // define the type of image, and where it should be stored
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Images")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        ).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        this@TakePhotoActivity,
                        getString(R.string.photo_capture_succeeded),
                        Toast.LENGTH_SHORT
                    ).show()

                    lifecycleScope.launch(Dispatchers.IO) {
                        val inputStream: InputStream? = contentResolver.openInputStream(output.savedUri!!)
                        val photoByteArray = inputStream?.readBytes()
                        inputStream?.close()

                        if (photoByteArray != null) {
                            photoDayRepository.insertUserCurrentPhotoDay(sessionManager.getUsername(),photoByteArray,this@TakePhotoActivity.latitude,this@TakePhotoActivity.longitude)
                        } //TODO in a possible refactor this substitutes the whole process of setResult


                        val intent = Intent().apply {
                            putExtra("latitude", this@TakePhotoActivity.latitude )
                            putExtra("longitude", this@TakePhotoActivity.longitude)
                            putExtra("photo", photoByteArray)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            })
    }

    /**
     * Asks for authorization to access GPS
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "Location update received: $location")
        this.location = location // Update the location
        updateCoordinates(location.latitude, location.longitude)
    }

    private fun updateCoordinates(latitude: Double, longitude: Double) {
        this@TakePhotoActivity.latitude =latitude
        this@TakePhotoActivity.longitude = longitude
    }
}