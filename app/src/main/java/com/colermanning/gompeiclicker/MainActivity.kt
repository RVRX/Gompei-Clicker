package com.colermanning.gompeiclicker

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.colermanning.gompeiclicker.databinding.ActivityMainBinding
import com.colermanning.gompeiclicker.ui.main.*

class MainActivity : AppCompatActivity(), GameStartFragment.Callbacks, SettingsFragment.Callbacks {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.constraintLayout)

        if (currentFragment == null) {
            //val fragment = GameListFragment.newInstance(0)
            //val fragment = GameFragment.newInstance(UUID.fromString("7da90068-6943-4ff7-8082-2abeb9e42462"))
            val fragment = GameStartFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.constraintLayout, fragment)
                .commit()
        }
    }

    override fun onStartGameSelected(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(gameFragment.newInstance(), "Game")
        sectionsPagerAdapter.addFragment(shopFragment.newInstance(), "Shop")
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_videogame_asset_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_shop_24)

        fab.setOnClickListener { view ->
            setContentView(R.layout.fragment_main)
                val fragment = SettingsFragment.newInstance()
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.constraintLayout, fragment)
                    .commit()
            }
        }

    override fun onSaveSettingsSelected(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.addFragment(gameFragment.newInstance(), "Game")
        sectionsPagerAdapter.addFragment(shopFragment.newInstance(),"Shop")
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_videogame_asset_24)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_shop_24)

        fab.setOnClickListener { view ->
            setContentView(R.layout.fragment_main)
            val fragment = SettingsFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.constraintLayout, fragment)
                .commit()
        }
    }
}
