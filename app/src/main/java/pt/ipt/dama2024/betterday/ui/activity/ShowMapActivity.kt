package pt.ipt.dama2024.betterday.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import pt.ipt.dama2024.betterday.R
import pt.ipt.dama2024.betterday.data.ObjectiveRepository
import pt.ipt.dama2024.betterday.model.Objective

/**
 * Most of this was learned and used in class
 *
 * https://github.com/IPT-MEI-DAMA-2023-2024/Open-Street-Maps
 */
class ShowMapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var objectiveRepository: ObjectiveRepository
    private lateinit var objective: Objective

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //get the id for the specific objective
        val objectiveId = intent.getLongExtra("objectiveId", -1)
        objective = objectiveRepository.getObjectiveById(objectiveId) ?: return


        // Initialize osmdroid configuration
        Configuration.getInstance()
            .load(this, applicationContext.getSharedPreferences("osmdroid", MODE_PRIVATE))

        // Find the MapView and set its properties
        mapView = findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)

        val defaultLatitude = 39.59963776764394
        val defaultLongitude = -8.391843590532233// Map initialized and centered on room O103, IPT

        val latitude = objective.latitude ?: defaultLatitude
        val longitude = objective.longitude ?: defaultLongitude

        val startPoint = GeoPoint(latitude, longitude) // Coordinates
        mapView.controller.setCenter(startPoint)

        if (latitude == defaultLatitude && longitude == longitude){
            // Add a marker to the map
            val marker = Marker(mapView)
            marker.position = startPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = getString(R.string.default_location_ipt)
            mapView.overlays.add(marker)
            Log.d("ShowMapActivity", "Map initialized and centered on room O103, IPT")
        }
        else{
            // Add a marker to the map
            val marker = Marker(mapView)
            marker.position = startPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker)
            Log.d("ShowMapActivity", "Map initialized and centered on the desired location")
        }

        // Set up the return button
        val returnButton: Button = findViewById(R.id.button_return_from_map)
        returnButton.setOnClickListener {
            navigateToEditObjective(objectiveId)
        }

    }

    private fun navigateToEditObjective(objectiveId: Long) {
        val intent = Intent(this, EditObjectiveActivity::class.java).apply {
            putExtra("objectiveId", objectiveId)
        }
        startActivity(intent)
    }



    override fun onResume() {
        super.onResume()
        mapView.onResume() // needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause() // needed for compass, my location overlays, v6.0.0 and up
    }
}
