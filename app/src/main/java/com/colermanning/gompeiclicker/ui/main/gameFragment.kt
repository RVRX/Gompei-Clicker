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

            //todo, click action
            //  ...
        }


        return view
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
