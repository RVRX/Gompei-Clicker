package com.colermanning.gompeiclicker.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.util.Log

import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colermanning.gompeiclicker.GameViewModel
import com.colermanning.gompeiclicker.R
import com.colermanning.gompeiclicker.WeatherChecker
import com.google.android.gms.location.*
import java.util.*
import kotlin.math.ceil
import kotlin.math.sqrt


private const val TAG = "gameFragment"

class gameFragment : Fragment() {

    //lateinits
    private lateinit var gompeiImageView: ImageView
    private lateinit var backgroundImageView: ImageView
    private lateinit var pointsTextView: TextView

    private var currentlat = ""
    private var currentlong = ""

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    private val gameViewModel: GameViewModel by lazy {
        ViewModelProviders.of(this).get(GameViewModel::class.java)
        //todo https://stackoverflow.com/questions/53903762/viewmodelproviders-is-deprecated-in-1-1-0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var weatherLiveData: LiveData<String> = WeatherChecker().fetchContents(currentlat,currentlong)
        //todo, give fetchContents() the lat and long from device's current location
        weatherLiveData.observe(
            this,
            Observer { responseString ->
                Log.d("fortnite", "Response received: $responseString")
                gameViewModel.weatherString = responseString;

                when (responseString) {
                    "Snow" -> updateUIBackground(R.drawable.weather_snow)
                    "Rain" -> updateUIBackground(R.drawable.weather_rain)
                    "Clouds" -> updateUIBackground(R.drawable.weather_cloud)
                    "Clear" -> updateUIBackground(R.drawable.weather_sun)
                }
            }
        )

        //Gets accelerometer sensor, sets base accelerations to earth so it doesn't automatically shake
        sensorManager = context?.let { getSystemService(it, SensorManager::class.java) }
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    //Shake listener compares two last movements, if fast enough counts as a shake
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 8) {
                Log.d(TAG, "Shake it!")
               gompeiClick()
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        //get references to xml elements
        gompeiImageView = view.findViewById(R.id.gompeiImageView)
        backgroundImageView = view.findViewById(R.id.backgroundImage)
        pointsTextView = view.findViewById(R.id.pointValueText)

        // Gompei Image onClick Listener
        gompeiImageView.setOnTouchListener { v, event ->
            val bmp = convertViewToDrawable(v)
            var color: Int
            color = 0
            if (event.x.toInt() <= bmp.width && event.x.toInt() >= 0 && event.y.toInt() <= bmp.height && event.y.toInt() >= 0)
                color = bmp.getPixel(event.x.toInt(), event.y.toInt())
            if (color == Color.TRANSPARENT)
                return@setOnTouchListener false
            else {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gompeiClick() //run Gompei click action
                }

                return@setOnTouchListener true
            }

        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Observes the pointLiveData for changes,
         * will update everytime (from now on) that
         * the point live data is updated
         */
        gameViewModel.pointLiveData.observe(
            viewLifecycleOwner,
            Observer { points ->
                points?.let {
                    Log.i(TAG, "Got points: $points")
                    updateUIPoints(points)
                }
            })

        /**
         * Updates [gameViewModel]'s currentMultiplier when
         * a new upgrade is bought
         */
        gameViewModel.ownedClickValueUpgrades.observe(
            viewLifecycleOwner,
            Observer { upgrades ->
                upgrades?.let {
                    Log.i(TAG, "Upgrades Updated: $upgrades")
                    var newMultiplier = 1.0
                    for (upgrade in upgrades) {
                        newMultiplier = (newMultiplier * upgrade)
                    }
                    Log.i(TAG, "New multiplier: $newMultiplier")
                    gameViewModel.currentClickMultiplier = newMultiplier
                }
            })
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString("lat")?.let {
            currentlat = it
        }
        arguments?.getString("long")?.let {
            currentlong  = it
        }
    }

    /**
     * Runs action for a gompei Click, will add
     * the current multiplier to the click, then
     * update to the DB as such. There is no need to
     * manually update the UI, as the UI is observing
     * the pointLiveData, and therefore will update
     * automatically.
     */
    private fun gompeiClick(): Boolean {
        var oldPointValue = gameViewModel.pointLiveData.value
        var pointsToAdd = gameViewModel.currentClickMultiplier
        if (oldPointValue != null) {
            val newPointValue = oldPointValue + pointsToAdd
            Log.d(TAG, "old = $oldPointValue || add $pointsToAdd || result $newPointValue (${ceil(newPointValue).toInt()})")
            gameViewModel.setPoints(ceil(newPointValue).toInt())
        } else {
            Log.e(TAG,"Points is null! Will not update")
        }

        return true
    }

    private fun updateUIPoints(points: Int) {
        pointsTextView.text = points.toString()
    }

    private fun updateUIBackground(drawableRes: Int) {
        backgroundImageView.setImageResource(drawableRes)
    }

    companion object {
        fun newInstance(lat: String, long: String) : gameFragment {
            val args = Bundle().apply {
                putString("lat",lat)
                putString("long",long)
            }
            return gameFragment().apply {
                arguments = args
            }
        }
    }

    private fun convertViewToDrawable(view: View): Bitmap {
        val b = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight,
            Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        c.translate((-view.scrollX).toFloat(), (-view.scrollY).toFloat())
        view.draw(c)
        return b
    }
}
