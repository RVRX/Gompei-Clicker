package com.colermanning.gompeiclicker

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
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import java.lang.Math.sqrt
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), GameStartFragment.Callbacks, SettingsFragment.Callbacks {

    private lateinit var binding: ActivityMainBinding

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        //sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 1) {
                //todo, call a gompeiClick from fragment...
                Log.e(TAG, "SHAKEY SHAKE")
                Toast.makeText(applicationContext, "Shake event detected", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }

    override fun onStartGameSelected(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(gameFragment.newInstance(), "Game")
        sectionsPagerAdapter.addFragment(shopFragment.newInstance(), "Shop")
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

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(gameFragment.newInstance(), "Game")
        sectionsPagerAdapter.addFragment(shopFragment.newInstance(),"Shop")
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
}
