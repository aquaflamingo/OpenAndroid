package com.pressurelabs.hibernate.ui.activities

import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar

import android.support.v4.view.ViewPager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.pressurelabs.hibernate.BaseActivity
import com.pressurelabs.hibernate.BuildConfig

import com.pressurelabs.hibernate.R
import com.pressurelabs.hibernate.ui.adapters.SectionsPagerAdapter
import com.pressurelabs.hibernate.ui.executors.EventCoordinator
import com.pressurelabs.hibernate.ui.services.HibernateReceiver
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null


    private var mViewPager: ViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.main_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.mipmap.ic_launcher)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.main_view_pager_container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter


        val tabLayout = findViewById(R.id.main_tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

        main_floating_ab_add.setOnClickListener {
            var startIntent =Intent(this,HibernateReceiver::class.java)
            startIntent.action = EventCoordinator.INTENT_START_TRACKING
            sendBroadcast(startIntent)
            toast("Started sleep tracking!")
            finish()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_debug).isVisible = false
        if (BuildConfig.DEBUG) {
            menu.findItem(R.id.action_debug).isVisible = true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when (id) {
            R.id.action_introduction -> {
                    var introIntent = Intent(this, IntroductionActivity::class.java)
                    startActivity(introIntent)
            }

            R.id.action_debug -> {
                startActivity(Intent(this,DebugActivity::class.java))
            }


        }

        return super.onOptionsItemSelected(item)
    }


}
