package com.example.privatebrowser.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onCreate()
        return inflater.inflate(layoutID(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        listener()
        listenLiveData()
    }


    protected abstract fun layoutID(): Int
    open fun onCreate() {}
    protected abstract fun initData()
    protected abstract fun initView()
    protected abstract fun listenLiveData()
    protected abstract fun listener()

    fun goToNewActivity(activity: Class<*>) {
        startActivity(Intent(requireActivity(), activity))
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
            params.topMargin = params.bottomMargin + getStatusBarHeight()
            i.layoutParams = params
        }
    }

    fun marginViewWithStatusBar(view: View) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = params.topMargin + getStatusBarHeight()
        view.layoutParams = params
    }

    fun paddingViewWithNavigationBar(view: View) {
        view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, getNavigationBarHeight() + view.paddingBottom)
    }

    fun marginViewWithNavigationBar(view: View) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        params.bottomMargin = params.bottomMargin + getNavigationBarHeight()
        view.layoutParams = params
    }
    protected fun loadImage(view: ImageView, idRes: Int) {
        Glide.with(view.context).load(idRes).into(view)
    }
    protected fun loadImage(view: ImageView, url: String) {
        Glide.with(view.context).load(url).into(view)
    }

}