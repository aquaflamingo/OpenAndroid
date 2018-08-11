package com.pressurelabs.hibernate.ui.fragments

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.pressurelabs.hibernate.BaseFragment
import com.pressurelabs.hibernate.R
import com.pressurelabs.hibernate.ui.activities.DebugActivity
import com.pressurelabs.hibernate.ui.activities.IntroductionActivity
import com.pressurelabs.hibernate.ui.presenter.IMainAlarmPresenter
import com.pressurelabs.hibernate.ui.presenter.MainAlarmPresenter
import com.pressurelabs.hibernate.ui.view.IMainAlarmView
import timber.log.Timber

class MainAlarmFragment : BaseFragment(), IMainAlarmView {
    private var presenter: IMainAlarmPresenter? = null;


    private var switchAlarmEnable: Switch?=null


    private var mainButton:Button? = null

    private var switch24hrClock: Switch?=null

    private var wallpaper: ImageView?=null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
            val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)
            mainButton = rootView.findViewById(R.id.button_main_set_alarm) as Button
            switchAlarmEnable = rootView.findViewById(R.id.frg_switch_alarm_on) as Switch
            switch24hrClock = rootView.findViewById(R.id.frg_switch_display_clock) as Switch
            photoAuthor=rootView.findViewById(R.id.main_tv_photo_author) as TextView
            photoTitle=rootView.findViewById(R.id.main_tv_photo_title) as TextView
            cycleCount = rootView.findViewById(R.id.main_tv_cycle_count) as TextView
            wallpaper = rootView.findViewById(R.id.main_imageview_wallpaper) as ImageView
            targetTime = rootView.findViewById(R.id.main_tv_target_time) as TextView

            mainButton?.setOnClickListener {
                presenter?.onAlarmButtonClick(context)
            }
            switchAlarmEnable?.setOnClickListener {
                presenter?.onAlarmEnabledChange()
                if (switchAlarmEnable!!.isChecked) {
                    presenter?.onAlarmButtonClick(context)
                }
            }

            switch24hrClock?.setOnClickListener {
                presenter?.onClockDisplayTypeChange()
            }

            presenter= MainAlarmPresenter(this,context)

            presenter?.introduceIfNeeded()

            presenter?.debug()
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): MainAlarmFragment {
                val fragment = MainAlarmFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }

    override fun onIntroduction() {
        var introIntent = Intent(context, IntroductionActivity::class.java)
        startActivity(introIntent)
    }

    override fun setWallpaperAuthor(author: String?) {

        photoAuthor?.text="Photo by " + author
    }

    private var targetTime: TextView?=null

    override fun setTargetSleep(timeAsString: String) {
        targetTime?.text=timeAsString
    }

    override fun setClockType24Hour(clock24Hr:Boolean) {
       switch24hrClock?.isChecked=clock24Hr
    }

    override fun setAlarmStatus(alarmIsOn:Boolean) {
        switchAlarmEnable?.isChecked = alarmIsOn

    }


    private var photoAuthor: TextView? = null
    private var photoTitle: TextView? = null


    override fun set500PXDetailsViewIntent(intent: Intent?) {
        photoAuthor?.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            if (intent!=null){
                startActivity(intent)
                true
            } else {
                false
            }
        }

    }


    fun debug(v:View) {
        startActivity(Intent(context, DebugActivity::class.java))
    }

    fun notAvaliable(v:View) {
        toast(getString(R.string.msg_not_avaliable))
    }


    override fun setWallpaperImage(img: Bitmap?) {
        var mDrawable: BitmapDrawable = BitmapDrawable(null,img)
        wallpaper?.setImageDrawable(mDrawable);
    }

    override fun showTimePicker(dialog: TimePickerDialog) {
        dialog.show()
    }


    private var cycleCount: TextView? = null

    override fun setCycleCount(cycles: Int) {
       cycleCount?.text = cycles.toString() + " Full Cycles"
    }

    override fun setAlarmWakeTime(time: String) {
        mainButton!!.text = time;

    }

    override fun setWallpaperTitle(title: String?) {
        photoTitle?.text=title
    }
}