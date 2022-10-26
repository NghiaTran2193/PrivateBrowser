package com.example.privatebrowser.utils

import android.content.Context
import android.widget.FrameLayout
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.example.privatebrowser.R

class Itemtab(context: Context) : CommonItemCell(context) {
    override val layoutId = R.layout.item_image
    override val param: LayoutParams
        get() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

}
class ItemApp(context: Context) : CommonItemCell(context){
    override val layoutId = R.layout.item_app
    override val param: LayoutParams
        get() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
}
open class CommonItemCell(context: Context) : FrameLayout(context) {
    open val param: LayoutParams =
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    open val layoutId = -1
    private var isInflated = false
    private val bindingFunctions: MutableList<CommonItemCell.() -> Unit> = mutableListOf()
    fun inflate() {
        layoutParams = param
        AsyncLayoutInflater(context).inflate(layoutId, this) { view, _, _ ->
            isInflated = true
            addView(view)
            bindView()
        }
    }

    private fun bindView() {
        with(bindingFunctions) {
            forEach { it() }
            clear()
        }
    }

    fun bindWhenInflated(bindFunc: CommonItemCell.() -> Unit) {
        if (isInflated) {
            bindFunc()
        } else {
            bindingFunctions.add(bindFunc)
        }
    }

}