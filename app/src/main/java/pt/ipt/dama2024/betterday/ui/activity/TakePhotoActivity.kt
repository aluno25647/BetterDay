package pt.ipt.dama2024.betterday.ui.activity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
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
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.databinding.ActivityTakePhotoBinding
import pt.ipt.dama2024.betterday.model.Objective
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Most of this was learned and used in class
 * The difference lies in the adaptation from only saving locally to also saving in the database
 * https://github.com/IPT-MEI-DAMA-2023-2024/Camera-X
 */
class TakePhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTakePhotoBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var objectiveRepository: ObjectiveRepository
    private lateinit var objective: Objective

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Check and request necessary permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

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

    /**
     * function to take the photo and save it to storage
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
                            updateObjectiveWithPhoto(photoByteArray)
                        }
                    }
                }
            })
    }

    /**
     * Function to update the objective with the captured photo byte array
     */
    private fun updateObjectiveWithPhoto(photoByteArray: ByteArray) {
        // get the objectiveId
        val objectiveId = intent.getLongExtra("objectiveId", -1)
        objective = objectiveRepository.getObjectiveById(objectiveId) ?: return

        val objective = objectiveRepository.getObjectiveById(objectiveId)
        if (objective != null) {
            objective.photo1 = photoByteArray

            val updatedRows = objectiveRepository.updateObjective(objective)
            if (updatedRows > 0) {
                Log.d(TAG, "Objective updated successfully with photo")
                navigateToEditObjective(objectiveId)
            } else {
                Log.e(TAG, "Failed to update objective with photo")
            }
        }
    }

    private fun navigateToEditObjective(objectiveId: Long) {
        val intent = Intent(this, EditObjectiveActivity::class.java).apply {
            putExtra("objectiveId", objectiveId)
        }
        startActivity(intent)
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}
