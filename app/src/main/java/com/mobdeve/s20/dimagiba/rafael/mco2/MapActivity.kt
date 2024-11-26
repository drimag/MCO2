package com.mobdeve.s20.dimagiba.rafael.mco2

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Location
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.tasks.OnSuccessListener
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.location.Geocoder
import android.location.Address
import android.widget.Toast
import org.osmdroid.api.IMapController
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.Locale


class MapActivity : AppCompatActivity() {
    companion object {
        const val NEW_LOCATION = "NEW_LOCATION"
    }

    private lateinit var cancelBtn: Button
    private lateinit var confirmLocationButton: Button
    private lateinit var currentLocationButton: LinearLayout
    private lateinit var locationTv: TextView

    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private var currentMarker: Marker? = null

    private lateinit var map : MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //handle permissions first, before map is created. not depicted here
        if (!checkPermissions()) {
            requestPermissions()
        }

        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, if you abuse osm's
        //tile servers will get you banned based on this string.

        //inflate and create the map
        setContentView(R.layout.activity_map)

        this.cancelBtn = findViewById<Button>(R.id.cancelBtn)
        this.confirmLocationButton = findViewById<Button>(R.id.confirmLocationBtn)
        this.currentLocationButton = findViewById<LinearLayout>(R.id.currentLocationBtn)
        this.locationTv = findViewById<TextView>(R.id.locationTv)

        this.currentLocationButton.setOnClickListener(View.OnClickListener {
            setMapToCurrentLocation()
        })

        this.cancelBtn.setOnClickListener(View.OnClickListener {
            finish()
        })

        this.confirmLocationButton.setOnClickListener(View.OnClickListener {
            val intent : Intent = Intent(this, AddTreasureActivity::class.java)
            intent.putExtra(MapActivity.NEW_LOCATION, locationTv.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        })

        map = findViewById<MapView>(R.id.osmmap)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                changeMarkerPosition(p)
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                TODO("Not yet implemented")
            }
        }

        val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
        map.overlays.add(mapEventsOverlay)

        setMapToCurrentLocation()
    }

    private fun changeMarkerPosition(p: GeoPoint) {
        val locationString = encodeLocation(p)
        currentMarker?.position = p
        currentMarker?.title = locationString

        this.locationTv.text = locationString

        map.controller.setZoom(19.0)
        map.controller.setCenter(p)
    }

    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
        setMapToCurrentLocation()
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun encodeLocation(p: GeoPoint):String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: MutableList<Address>? =
            geocoder.getFromLocation(p.latitude, p.longitude, 1)
        val addressString = StringBuilder()

        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses[0]

                // Get address components (you can adjust this based on what you need)
                if (address.thoroughfare != null) {
                    addressString.append(address.thoroughfare)  // Street name
                    addressString.append(", ")
                }
                if (address.locality != null) {
                    addressString.append(address.locality)  // City or locality
                    addressString.append(", ")
                }
                if (address.countryName != null) {
                    addressString.append(address.countryName)  // Country
                }
            }
        }

        return addressString.toString()
    }

    @SuppressLint("MissingPermission")
    private fun setMapToCurrentLocation() {
        if (checkPermissions()) {
            fusedLocationClient.lastLocation.addOnSuccessListener(OnSuccessListener<Location> { location ->
                if (location != null) {
                    if (currentMarker == null) {
                        val customIcon =
                            ContextCompat.getDrawable(this, R.drawable.ic_map_marker)
                        currentMarker = Marker(map).apply {
                            position = GeoPoint(location.latitude, location.longitude)
                            title = "You are here!"
                            icon = customIcon
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        }

                        map.overlays.add(currentMarker)
                    } else {
                        currentMarker?.position = GeoPoint(location.latitude, location.longitude)
                    }

                    val userLocation = GeoPoint(location.latitude, location.longitude)
                    val locationString = encodeLocation(userLocation)

                    this.locationTv.text = locationString

                    map.controller.setZoom(19.0)
                    map.controller.setCenter(userLocation)

                } else {
                    Toast.makeText(this, "There is something wrong with location services. Try again later.",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}