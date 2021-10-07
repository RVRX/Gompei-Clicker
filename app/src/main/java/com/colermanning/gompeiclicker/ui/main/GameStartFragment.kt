package com.colermanning.gompeiclicker.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.colermanning.gompeiclicker.R
import java.util.*

class GameStartFragment : Fragment() {

    interface Callbacks {
        fun onStartGameSelected()
    }

    private var callbacks: Callbacks? = null

    private lateinit var GameStartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start_game, container, false)

        GameStartButton = view.findViewById(R.id.start_game)

        GameStartButton.setOnClickListener{ view: View ->
            callbacks?.onStartGameSelected()
        }
        return view
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance() : GameStartFragment{
            val args = Bundle().apply {

            }
            return GameStartFragment().apply {
                arguments = args
            }
        }
    }
}