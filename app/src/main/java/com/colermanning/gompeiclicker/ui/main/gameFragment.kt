package com.colermanning.gompeiclicker.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.colermanning.gompeiclicker.R
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color

import android.view.MotionEvent

import android.view.View.OnTouchListener
import android.widget.Toast


private const val TAG = "gameFragment"

class gameFragment : Fragment() {

    //lateinits
    private lateinit var gompeiImageView: ImageView
    private lateinit var pointsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    /**
     * Runs action for a gompei Click...
     *
     * todo: this will call the repository (I think), which will look at current settings,
     *  and get upgrade info to figure out how much a click is worth, then
     *  add that many points via the DB.
     */
    fun gompeiClick(): Boolean {
        // todo, this is just temporary demo action
        var pointsAsInt = pointsTextView.text.toString().toInt()
        pointsAsInt++
        pointsTextView.text = pointsAsInt.toString()
        return true
    }

    companion object {
        fun newInstance() : gameFragment{
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
