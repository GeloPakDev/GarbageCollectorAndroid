package com.example.garbagecollector.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.garbagecollector.databinding.ActivityMyGarbageBinding

class MyGarbageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyGarbageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyGarbageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        val viewPager = binding.tabViewpager
        val tabLayout = binding.tabTablayout

        setSupportActionBar(toolbar)
        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        // LoginFragment is the name of Fragment and the Login
        // is a title of tab
        adapter.addFragment(PostedGarbageFragment(), "Posted")
        adapter.addFragment(ClaimedGarbageFragment(), "Claimed")
        // setting adapter to view pager.
        viewPager.adapter = adapter
    }

    class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
        FragmentPagerAdapter(supportFragmentManager) {

        // objects of arraylist. One is of Fragment type and
        // another one is of String type.*/
        private var fragmentList1: ArrayList<Fragment> = ArrayList()
        private var fragmentTitleList1: ArrayList<String> = ArrayList()

        // returns which item is selected from arraylist of fragments.
        override fun getItem(position: Int): Fragment {
            return fragmentList1[position]
        }

        // returns which item is selected from arraylist of titles.
        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList1[position]
        }

        // returns the number of items present in arraylist.
        override fun getCount(): Int {
            return fragmentList1.size
        }

        // this function adds the fragment and title in 2 separate  arraylist.
        fun addFragment(fragment: Fragment, title: String) {
            fragmentList1.add(fragment)
            fragmentTitleList1.add(title)
        }
    }
}