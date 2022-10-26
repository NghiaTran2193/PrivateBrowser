package com.example.privatebrowser.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

abstract class BaseDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(layoutID())
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.decorView?.setBackgroundColor(android.R.color.transparent)
        initData()
        listener()

    }
    abstract fun layoutID(): Int
    abstract  fun initData()
    abstract fun listener()

}