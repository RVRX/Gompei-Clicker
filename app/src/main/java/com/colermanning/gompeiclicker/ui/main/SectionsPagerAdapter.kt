package com.colermanning.gompeiclicker.ui.main

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.colermanning.gompeiclicker.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm) {

    private var fragmentList = ArrayList<Fragment>()
    private var fragmentnameList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentnameList[position]
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment, string: String) {
        fragmentList.add(fragment)
        fragmentnameList.add(string)
    }
}