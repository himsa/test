package com.example.layanacomputindo.test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import im.delight.android.location.SimpleLocation



class CurrentLocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var location: SimpleLocation
    private lateinit var mLOCATION: LatLng
    val ZOOM_LEVEL = 18f
    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap ?: return
        with(googleMap) {
            moveCamera(CameraUpdateFactory.newLatLngZoom(mLOCATION, ZOOM_LEVEL))
            addMarker(MarkerOptions().position(mLOCATION))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)
        val mapFragment : SupportMapFragment? =
                supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        // construct a new instance of SimpleLocation
        location = SimpleLocation(this)

        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this)
        }
        mLOCATION = LatLng(location.latitude, location.longitude)
    }

    override fun onResume() {
        super.onResume()

        // make the device update its location
        if (true) {
            location.beginUpdates()
        }

        // ...
    }

    override fun onPause() {
        // stop location updates (saves battery)
        if (true) {
            location.endUpdates()
        }

        // ...

        super.onPause()
    }
}
