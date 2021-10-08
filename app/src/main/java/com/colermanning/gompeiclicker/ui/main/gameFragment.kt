package com.colermanning.gompeiclicker.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.colermanning.gompeiclicker.R

private const val TAG = "gameFragment"

class gameFragment : Fragment() {

    //lateinits
    private lateinit var gompeiImageView: ImageView
    private lateinit var pointsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        gompeiImageView.setOnClickListener { view: View ->
            Log.d(TAG, "Gompei Clicked!")
            gompeiClick() //run Gompei click action
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
    fun gompeiClick() {
        // todo, this is just temporary demo action
        var pointsAsInt = pointsTextView.text.toString().toInt()
        pointsAsInt++
        pointsTextView.text = pointsAsInt.toString()
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
}
