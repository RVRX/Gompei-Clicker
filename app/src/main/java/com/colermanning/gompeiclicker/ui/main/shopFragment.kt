package com.colermanning.gompeiclicker.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.colermanning.gompeiclicker.R

class shopFragment : Fragment() {

    private lateinit var powerup1: ImageView
    private lateinit var layout1: LinearLayout
    private lateinit var layout2: LinearLayout
    private lateinit var layout3: LinearLayout
    private lateinit var layout4: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        powerup1 = view.findViewById(R.id.powerup_1)
        layout1 = view.findViewById(R.id.linear_layout_1)
        layout2 = view.findViewById(R.id.linear_layout_2)
        layout3 = view.findViewById(R.id.linear_layout_3)
        layout4 = view.findViewById(R.id.linear_layout_4)

        layout1.setOnClickListener { view: View ->
            Log.d("fortnite", "you clicked Hay For Gompei")
        }
        layout2.setOnClickListener { view: View ->
            Log.d("fortnite", "you clicked Sugar Cube")
        }
        layout3.setOnClickListener { view: View ->
            Log.d("fortnite", "you clicked Education")
        }
        layout4.setOnClickListener { view: View ->
            Log.d("fortnite", "you clicked Study Break")
        }

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