package com.colermanning.gompeiclicker.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.colermanning.gompeiclicker.MainActivity
import com.colermanning.gompeiclicker.R
import com.zeugmasolutions.localehelper.Locales

class SettingsFragment : Fragment() {

    interface Callbacks {
        fun onSaveSettingsSelected()
        fun changeLocale(lang: String)
    }

    private var callbacks: Callbacks? = null

    private lateinit var  SaveSettingsButton: Button
    private lateinit var  previousButton: ImageButton
    private lateinit var  playButton: ImageButton
    private lateinit var  pauseButton: ImageButton
    private lateinit var  nextButton: ImageButton
    private lateinit var  enButton: Button
    private lateinit var  esButton: Button
    private lateinit var  jpButton: Button
    private lateinit var  krButton: Button


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
        previousButton = view.findViewById(R.id.previous_song)
        playButton = view.findViewById(R.id.play_button)
        pauseButton = view.findViewById(R.id.pause_button)
        nextButton = view.findViewById(R.id.next_song)
        enButton = view.findViewById(R.id.en)
        esButton = view.findViewById(R.id.es)
        jpButton = view.findViewById(R.id.jp)
        krButton = view.findViewById(R.id.kr)

        SaveSettingsButton.setOnClickListener{ view: View ->
            callbacks?.onSaveSettingsSelected()
        }

        playButton.setOnClickListener{ view: View ->
            val action = "PLAY"
            val myService = Intent(requireContext(), BackgroundSoundService::class.java)
            myService.action = action
            requireContext().startService(myService)
        }

        pauseButton.setOnClickListener{ view: View ->
            val action = "PAUSE"
            val myService = Intent(requireContext(), BackgroundSoundService::class.java)
            myService.action = action
            requireContext().startService(myService)
        }

        nextButton.setOnClickListener{ view: View ->
            val action = "NEXT"
            val myService = Intent(requireContext(), BackgroundSoundService::class.java)
            myService.action = action
            requireContext().startService(myService)
        }

        previousButton.setOnClickListener{ view: View ->
            val action = "PREVIOUS"
            val myService = Intent(requireContext(), BackgroundSoundService::class.java)
            myService.action = action
            requireContext().startService(myService)
        }

        esButton.setOnClickListener { view: View ->
            callbacks?.changeLocale("es")
            val manager = requireActivity().supportFragmentManager
            manager.beginTransaction().remove(this).commit()
        }


        enButton.setOnClickListener { view: View ->
            callbacks?.changeLocale("en")
            val manager = requireActivity().supportFragmentManager
            manager.beginTransaction().remove(this).commit()
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