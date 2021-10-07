package com.colermanning.gompeiclicker.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.colermanning.gompeiclicker.R

class SettingsFragment : Fragment() {

    interface Callbacks {
        fun onSaveSettingsSelected()
    }

    private var callbacks: Callbacks? = null

    private lateinit var  SaveSettingsButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings, container, false)
        SaveSettingsButton = view.findViewById(R.id.save_settings)

        SaveSettingsButton.setOnClickListener{ view: View ->
            callbacks?.onSaveSettingsSelected()
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
        fun newInstance() : SettingsFragment{
            val args = Bundle().apply {

            }
            return SettingsFragment().apply {
                arguments = args
            }
        }
    }
}