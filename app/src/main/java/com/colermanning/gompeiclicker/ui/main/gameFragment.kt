package com.colermanning.gompeiclicker.ui.main

import android.annotation.SuppressLint
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
import android.util.Log

import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.colermanning.gompeiclicker.GameViewModel
import com.colermanning.gompeiclicker.R
import com.colermanning.gompeiclicker.WeatherChecker
import kotlin.math.ceil


private const val TAG = "gameFragment"

class gameFragment : Fragment() {

    //lateinits
    private lateinit var gompeiImageView: ImageView
    private lateinit var backgroundImageView: ImageView
    private lateinit var pointsTextView: TextView

    private val gameViewModel: GameViewModel by lazy {
        ViewModelProviders.of(this).get(GameViewModel::class.java)
        //todo https://stackoverflow.com/questions/53903762/viewmodelproviders-is-deprecated-in-1-1-0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //API Content:
        //todo, give fetchContents() the lat and long from device's current location
        val weatherLiveData: LiveData<String> = WeatherChecker().fetchContents()
        weatherLiveData.observe(
            this,
            Observer { responseString ->
                Log.d(TAG, "Response received: $responseString")
                gameViewModel.weatherString = responseString;

                when (responseString) {
                    "Snow" -> updateUIBackground(R.drawable.weather_snow)
                    "Rain" -> updateUIBackground(R.drawable.weather_rain)
                    "Clouds" -> updateUIBackground(R.drawable.weather_cloud)
                    "Clear" -> updateUIBackground(R.drawable.weather_sun)
                }
            }
        )
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
        fun newInstance() : gameFragment {
            val args = Bundle().apply {

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
