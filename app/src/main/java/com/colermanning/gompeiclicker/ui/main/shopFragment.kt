package com.colermanning.gompeiclicker.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colermanning.gompeiclicker.R
import com.colermanning.gompeiclicker.ShopViewModel
import com.colermanning.gompeiclicker.Upgrade


private const val TAG = "ShopFragment"

class shopFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        //fun onGameSelected(gameId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var shopRecyclerView: RecyclerView
    private var adapter: ShopAdapter? = ShopAdapter(emptyList())

    /**
     * Get the view model for this fragment
     */
    private val shopViewModel: ShopViewModel by lazy {
        ViewModelProviders.of(this).get(ShopViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //callbacks = context as Callbacks?
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.shop_recycler_view, container, false)

        shopRecyclerView =
            view.findViewById(R.id.shop_recycler_view) as RecyclerView
        shopRecyclerView.layoutManager = LinearLayoutManager(context)
        shopRecyclerView.adapter = adapter //set the recycler view's adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopViewModel.shopListLiveData.observe(
            viewLifecycleOwner,
            Observer { upgrades ->
                upgrades?.let {
                    if(upgrades.size < 2){
                        Log.i(TAG, "Populating database")
                        shopViewModel.fillUpgrades()
                    }
                    Log.i(TAG, "Got upgrades ${upgrades.size}")
                    updateUI(upgrades)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        //callbacks = null
    }

    private fun updateUI(upgrades: List<Upgrade>) {
        adapter = ShopAdapter(upgrades)
        shopRecyclerView.adapter = adapter
    }

    /**
     * Each 'item' in the list is a ShopHolder (this)
     * A ShopHolder consists of:
     *  - Name (nameTextView)
     *  - Description (descTextView)
     *  - Cost (costTextView)
     *  - Icon (iconImageView)
     */

    private inner class ShopHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var upgrade: Upgrade

        //get variables from XML (list_item_upgrade.xml)
        val nameTextView: TextView = itemView.findViewById(R.id.upgrade_name)
        val descTextView: TextView = itemView.findViewById(R.id.upgrade_description)
        val costTextView: TextView = itemView.findViewById(R.id.upgrade_cost)
        //val avatarImageView: ImageView = itemView.findViewById(R.id.imageView2)

        init {
            // init the listener
            itemView.setOnClickListener(this)
        }

        fun bind(upgrade: Upgrade) {
            this.upgrade = upgrade
            nameTextView.text = this.upgrade.id
            descTextView.text = this.upgrade.description
            if(upgrade.bought){
                costTextView.text = getString(R.string.upgrade_purchased)
            }
            else{
                costTextView.text = this.upgrade.cost.toString()
            }



        }

        override fun onClick(v: View?) {
            Log.d(TAG, "${upgrade.id} clicked!")

            //buy upgrade
            var result = shopViewModel.buyUpgrade(upgrade)
            costTextView.text = getString(R.string.upgrade_purchased)
            if (result){
                Toast.makeText(context, "${upgrade.id} Purchased!", Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                Toast.makeText(context, "${upgrade.id} has already been purchased!", Toast.LENGTH_SHORT)
                    .show()
            }


        }

    }


/**
 * Adapter to populate the RecyclerView
 *
 * adapter is a controller object that sits between the RecyclerView and
 * the data set that the RecyclerView should display
 *
 */
private inner class ShopAdapter(var upgrades: List<Upgrade>)
    : RecyclerView.Adapter<ShopHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ShopHolder {
        val view = layoutInflater.inflate(R.layout.list_item_upgrade, parent, false)
        return ShopHolder(view)
    }

    override fun onBindViewHolder(holder: ShopHolder, position: Int) {
        val upgrade = upgrades[position]
        holder.bind(upgrade)

        if(position %2 == 1) { //alternate coloring
            holder.itemView.setBackgroundColor(ContextCompat.getColor(activity as Context, R.color.clear))
        }
        else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(activity as Context, R.color.grey_alpha10))
        }
    }

    override fun getItemCount() = upgrades.size
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