package com.example.test.compass.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.compass.R
import com.example.test.compass.location.Location
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_destination_pick.*

private const val EXTRA_LOCATION_KEY = "key-location"

class DestinationPickActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var currentLocation: Location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_pick)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnSetLocation.setOnClickListener {
            val intent = Intent().apply { putExtra(EXTRA_LOCATION_KEY, currentLocation) }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it))
            currentLocation = Location(it.longitude, it.latitude)
        }
    }

    companion object {
        fun getResult(intent: Intent?): Location? = intent?.getSerializableExtra(EXTRA_LOCATION_KEY) as? Location
    }
}
