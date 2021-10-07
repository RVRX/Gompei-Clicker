package com.colermanning.gompeiclicker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.colermanning.gompeiclicker.R

class shopFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        return view
    }

    companion object {
        fun newInstance() : shopFragment{
            val args = Bundle().apply {

            }
            return shopFragment().apply {
                arguments = args
            }
        }
    }
}