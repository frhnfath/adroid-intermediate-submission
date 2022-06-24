package com.frhnfath.storyappfix.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.frhnfath.storyappfix.R
import com.frhnfath.storyappfix.data.AllStoriesResponse
import com.frhnfath.storyappfix.data.ListStoryItem

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.frhnfath.storyappfix.databinding.ActivityMapsBinding
import com.frhnfath.storyappfix.network.ApiConfig
import com.frhnfath.storyappfix.session.UserPreferences
import com.frhnfath.storyappfix.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mUserPreferences: UserPreferences
    private val mutableMap = MutableLiveData<List<ListStoryItem>>()
    private val locationMap: LiveData<List<ListStoryItem>> = mutableMap
    private val mapsViewModel: MapsViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mUserPreferences = UserPreferences(this)

        getLocationStories()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val monas = LatLng(-6.175392, 106.827153)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(monas))
        locationMap.observe(this) {
            for (i in locationMap.value?.indices!!) {
                val lat = locationMap.value?.get(i)?.lat
                val lon = locationMap.value?.get(i)?.lon
                val location = LatLng(lat!!, lon!!)
                val title = locationMap.value?.get(i)?.name
                val snippet = locationMap.value?.get(i)?.description
                mMap.addMarker(MarkerOptions().position(location).title(title).snippet(snippet))
            }
        }

        mMap.setOnMapLongClickListener { latLng ->
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("New Marker")
                    .snippet("Lat: ${latLng.latitude} Long: ${latLng.longitude}")
                    .icon(vectorToBitmap(R.drawable.ic_android_24, Color.parseColor("#3DDC84")))
            )
        }
    }

    private fun getData(i: Int): Array<String>? {
        val arr: Array<String>? = null
        arr?.set(0, locationMap.value?.get(i)?.lat.toString())
        arr?.set(1, locationMap.value?.get(i)?.lon.toString())
        arr?.set(2, locationMap.value?.get(i)?.name.toString())
        arr?.set(3, locationMap.value?.get(i)?.description.toString())
        return arr
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "vectorToBitmap: Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun getLocationStories() {
        val client = ApiConfig.getApiService().getStoriesLocation("Bearer " + mUserPreferences.getUser().token)
        client.enqueue(object : Callback<AllStoriesResponse> {
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.d("MapsActivity", "onResponse: ${responseBody.listStory}")
                    mutableMap.value = responseBody.listStory
                    Log.d("Maps Activity", "onResponse: ${response.message()}")
                } else {
                    Toast.makeText(applicationContext, "Story failed", Toast.LENGTH_SHORT).show()
                    Log.e("Story Failed", "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Story failed", Toast.LENGTH_SHORT).show()
                Log.e("Story Failed", "onResponse: ${t.message}")
            }

        })
    }
}