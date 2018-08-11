package com.pressurelabs.hibernate.ui.activities

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.pressurelabs.hibernate.R
import com.ramotion.paperonboarding.PaperOnboardingPage
import android.support.v4.app.FragmentTransaction
import com.pressurelabs.hibernate.BaseActivity
import com.ramotion.paperonboarding.PaperOnboardingFragment


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class IntroductionActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_introduction)
        var intentAction = intent.action

        val scr2 = PaperOnboardingPage(
                getString(R.string.intro_title_cycles_wakeup),
                getString(R.string.intro_content_cycles_wakeup),
                Color.parseColor("#f06292"), R.drawable.draw_cycles_awake, R.drawable.ic_circle_bright)

        val scr1 = PaperOnboardingPage(
                getString(R.string.intro_title_groggy),
                getString(R.string.intro_content_groggy),
                Color.parseColor("#4fc3f7"), R.drawable.draw_grogginess, R.drawable.ic_circle_bright)

        val scr3 = PaperOnboardingPage(
                getString(R.string.intro_title_cycles_not_hours),
                getString(R.string.intro_content_cycles_not_hours),
                Color.parseColor("#ffb74d"), R.drawable.draw_sleep_cycles, R.drawable.ic_circle_bright)

        val scr4 = PaperOnboardingPage(
                getString(R.string.intro_title_hibernate),
                getString(R.string.intro_content_hibernate),
                Color.parseColor("#2ab8f7"), R.drawable.draw_bear_sleep, R.drawable.ic_circle_bright)

        var elements:ArrayList<PaperOnboardingPage> = ArrayList()
        elements.add(scr1)
        elements.add(scr2)
        elements.add(scr3)
        elements.add(scr4)

        var onBoardingFrag:PaperOnboardingFragment = PaperOnboardingFragment.newInstance(elements)

        var trn: FragmentTransaction = supportFragmentManager.beginTransaction()

        trn.add(R.id.intro_fragment_container,onBoardingFrag)
        trn.commit()
        
            onBoardingFrag.setOnRightOutListener {
                finish()
            }

        }

}
