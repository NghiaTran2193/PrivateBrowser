package com.example.privatebrowser.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.privatebrowser.R
import io.github.inflationx.viewpump.ViewPumpContextWrapper


abstract class BaseActivity : AppCompatActivity() {
//    lateinit var tabsViewModel: TabsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        with(window) {
            addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            setWindowFlag(
                this@BaseActivity,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                false
            )
            window.statusBarColor = Color.TRANSPARENT
        }

        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        setContentView(layoutID())

        initData()
        initView()
        listenLiveData()
        listeners()
    }

    open fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win: Window = activity.window
        val winParams: WindowManager.LayoutParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    abstract fun layoutID(): Int
    abstract fun initData()
    abstract fun initView()
    abstract fun listenLiveData()
    abstract fun listeners()
    open fun onSaveState(outState: Bundle) {}
    open fun onRestoreState(savedInstanceState: Bundle) {}


    protected fun lightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    protected fun darkStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    open fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    open fun getNavigationBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    open fun marginStatusBar(listView: List<View>) {
        for (i in listView) {
            val params = i.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = params.topMargin + getStatusBarHeight()
            i.layoutParams = params
        }
    }

    open fun marginNavigationBar(listView: List<View>) {
        for (i in listView) {
            val params = i.layoutParams as ConstraintLayout.LayoutParams
            params.bottomMargin = params.bottomMargin + getNavigationBarHeight()
            i.layoutParams = params
        }
    }

    open fun paddingNavigationBar(listView: List<View>) {
        for (i in listView) {
            i.setPadding(i.paddingLeft, i.paddingTop, i.paddingRight, getNavigationBarHeight())
        }
    }

    open fun paddingRootView(rootViewActivity: View) {
        rootViewActivity.setPadding(rootViewActivity.paddingLeft, getStatusBarHeight(), rootViewActivity.paddingRight, rootViewActivity.paddingBottom)
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    fun goToActivityNew(activity: Class<*>, requestCode: Int) {
        startActivityForResult(Intent(this, activity), requestCode)
    }

    fun goToActivityNew(activity: Class<*>, bundle: Bundle) {
        startActivity(Intent(this, activity).putExtra("bundle", bundle))
    }

    fun getBundle(): Bundle? {
        return intent.getBundleExtra("bundle")
    }

    interface CallBackLoadImage {
        fun ready()
    }


    protected fun goToNewActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        onSaveState(outState)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        onRestoreState(savedInstanceState)
    }
    protected fun addOrShowFragment(fragment: Fragment, tag: String, isRemoveOldFragment: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        val currentFragment = supportFragmentManager.findFragmentByTag(tag)
        if (currentFragment == null) {
            transaction.add(R.id.privateBrowser, fragment, tag)
            transaction.show(fragment)
        } else {
            transaction.show(currentFragment)
        }
        supportFragmentManager.fragments.forEach {
            if (it.tag != tag) {
                if (isRemoveOldFragment) {
                    transaction.remove(it)
                } else {
                    transaction.hide(it)
                }
            }
        }
        transaction.commit()
    }

    protected fun addAndHideFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        val currentFragment = supportFragmentManager.findFragmentByTag(tag)
        if (currentFragment == null) {
            transaction.add(R.id.privateBrowser, fragment, tag)
            transaction.hide(fragment)
        }
        transaction.commit()
    }

}

