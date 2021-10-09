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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colermanning.gompeiclicker.GompeiClickerRepository
import com.colermanning.gompeiclicker.R
import com.colermanning.gompeiclicker.Upgrade

class shopFragment : Fragment() {

    private lateinit var powerup1: ImageView
    private lateinit var layout1: LinearLayout
    private lateinit var layout2: LinearLayout
    private lateinit var layout3: LinearLayout
    private lateinit var layout4: LinearLayout

    //todo, potentially put this in a ViewModel... use same ViewModel as gameFragment?
    private val gompeiClickerRepository = GompeiClickerRepository.get()

    private lateinit var shopRecyclerView: RecyclerView
    private var adapter: ShopAdapter? = ShopAdapter(emptyList())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.shop_recycler_view, container, false)

        /*powerup1 = view.findViewById(R.id.powerup_1)
        layout1 = view.findViewById(R.id.linear_layout_1)
        layout2 = view.findViewById(R.id.linear_layout_2)
        layout3 = view.findViewById(R.id.linear_layout_3)
        layout4 = view.findViewById(R.id.linear_layout_4)

        layout1.setOnClickListener { view: View ->
            Log.d("fortnite", "you clicked Hay For Gompei")
            gompeiClickerRepository.buyUpgradeById("Hay") //TODO, example usage, actual would be called through ViewModel, and set up in a RecyclerView?
            Toast.makeText(context, "Bought Hay", Toast.LENGTH_SHORT).show()
        }
        layout2.setOnClickListener { view: View ->
            Log.d("fortnite", "you clicked Sugar Cube")
            gompeiClickerRepository.buyUpgradeById("Sugar")
            Toast.makeText(context, "Bought Sugar", Toast.LENGTH_SHORT).show()
        }
        layout3.setOnClickListener { view: View ->
            Log.d("fortnite", "you clicked Education")
        }
        layout4.setOnClickListener { view: View ->
            Log.d("fortnite", "you clicked Study Break")
        } */
        shopRecyclerView =
            view.findViewById(R.id.shop_recycler_view) as RecyclerView
        shopRecyclerView.layoutManager = LinearLayoutManager(context)
        shopRecyclerView.adapter = adapter //set the recycler view's adapter

        return view
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

        //get variables from XML (list_item_game.xml)
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
            nameTextView.text = this.upgrade.upgradeType
            descTextView.text = this.upgrade.description
            costTextView.text = this.upgrade.cost.toString()


        }

        override fun onClick(v: View?) {
            Toast.makeText(context, "${upgrade.id} clicked!", Toast.LENGTH_SHORT)
                .show()

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