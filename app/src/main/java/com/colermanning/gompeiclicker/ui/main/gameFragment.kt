package com.colermanning.gompeiclicker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.colermanning.gompeiclicker.R

class gameFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

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
