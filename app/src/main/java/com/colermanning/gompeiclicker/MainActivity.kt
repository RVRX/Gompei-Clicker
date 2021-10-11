package com.colermanning.gompeiclicker

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.colermanning.gompeiclicker.databinding.ActivityMainBinding
import com.colermanning.gompeiclicker.ui.main.*
import com.colermanning.gompeiclicker.ui.main.BackgroundSoundService

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.zeugmasolutions.localehelper.LocaleAwareCompatActivity
import com.zeugmasolutions.localehelper.Locales


class MainActivity : LocaleAwareCompatActivity(), GameStartFragment.Callbacks, SettingsFragment.Callbacks {

    private lateinit var binding: ActivityMainBinding

    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location
    private lateinit var locationCallback: LocationCallback

    fun checkOrAskLocationPermission(callback: () -> Unit) {
        // Check GPS is enabled
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
            return
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            callback.invoke()
        } else {
            // callback will be inside the activity's onRequestPermissionsResult(
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                99
            )
        }
        locationRequest = LocationRequest.create()?.apply {
            interval = 30000
            fastestInterval = 30000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }!!
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        //val builder = LocationSettingsRequest.Builder()
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { // locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            getLastLocation()
            //startLocationUpdates()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkOrAskLocationPermission() {
            Log.d("fortnite", "yeah idk, we good?")
        }
        //API Content:
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    if (location!= null) {
                        currentLocation = location
                        Log.d("fortnite", currentLocation.latitude.toString())
                        Log.d("fortnite", currentLocation.longitude.toString())
                    }
                }
            }
        }

        setContentView(R.layout.fragment_main)
        val action = "PLAY"
        val myService = Intent(this@MainActivity, BackgroundSoundService::class.java)
        myService.action = action
        startService(myService)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.constraintLayout)

        if (currentFragment == null) {
            //val fragment = GameListFragment.newInstance(0)
            //val fragment = GameFragment.newInstance(UUID.fromString("7da90068-6943-4ff7-8082-2abeb9e42462"))
            val fragment = GameStartFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.constraintLayout, fragment)
                .commit()
        }

    }

    override fun onStartGameSelected(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startLocationUpdates()
        getLastLocation()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        val tab1name = R.string.tab_text_1
        val tab2name = R.string.tab_text_2

        if (::currentLocation.isInitialized) {
            sectionsPagerAdapter.addFragment(
                gameFragment.newInstance(
                    currentLocation.latitude.toString(),
                    currentLocation.longitude.toString()
                ),   tab1name.toString()
            )
        }
        else {
            sectionsPagerAdapter.addFragment(
                gameFragment.newInstance(
                    "0",
                    "0"
                ), tab1name.toString()
            )
        }

        sectionsPagerAdapter.addFragment(shopFragment.newInstance(), tab2name.toString())
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_videogame_asset_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_shop_24)

        fab.setOnClickListener { view ->
            setContentView(R.layout.fragment_main)
                val fragment = SettingsFragment.newInstance()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.constraintLayout, fragment)
                    .commit()
            }
        }

    override fun onSaveSettingsSelected(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startLocationUpdates()
        getLastLocation()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        val tab1name = R.string.tab_text_1
        val tab2name = R.string.tab_text_2


        if (::currentLocation.isInitialized) {
            sectionsPagerAdapter.addFragment(
                gameFragment.newInstance(
                    currentLocation.latitude.toString(),
                    currentLocation.longitude.toString()
                ), tab1name.toString()
            )
        }
        else {
            sectionsPagerAdapter.addFragment(
                gameFragment.newInstance(
                    "0",
                    "0"
                ), tab1name.toString()
            )
        }

        sectionsPagerAdapter.addFragment(shopFragment.newInstance(),tab2name.toString())
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_videogame_asset_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_shop_24)

        fab.setOnClickListener { view ->
            setContentView(R.layout.fragment_main)
            val fragment = SettingsFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.constraintLayout, fragment)
                .commit()
        }
    }

    override fun changeLocale(lang: String) {
        if (lang == "es")
            updateLocale(Locales.Spanish)
        if (lang == "en")
            updateLocale(Locales.English)
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { Location : Location? ->
                // Got last known location. In some rare situations this can be null.
                if (Location!= null) {
                    // use your location object
                    currentLocation = Location
                    Log.d("getlastlocation", currentLocation.latitude.toString())
                    Log.d("getlastlocation", currentLocation.longitude.toString())
                    fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(this)
                }else{
                    Log.d("fortnite","uh")
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {

        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())

    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
