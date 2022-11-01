package com.example.privatebrowser.view.dialog

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.privatebrowser.R
import com.example.privatebrowser.model.App
import com.example.privatebrowser.utils.SingleLiveEvent
import kotlinx.android.synthetic.main.item_dialog_add.*

class DialogAddMore(context: Context) : BaseDialog(context) {
    private val listenEventDialog: SingleLiveEvent<App> = SingleLiveEvent()
    val event: LiveData<App> by lazy {
        listenEventDialog
    }
    override fun layoutID(): Int = R.layout.item_dialog_add

    override fun initData() {

    }

    override fun listener() {
        btnCancel.setOnClickListener { dismiss() }
        btnBack.setOnClickListener { dismiss() }
        btnOk.setOnClickListener {
            if ((edLink.text.isNotEmpty() && edTitle.text.isNotEmpty())&&(edLink.text.isNotBlank() && edTitle.text.isNotBlank()) ) {
                listenEventDialog.postValue(App(name = edTitle.text.toString(), url = edLink.text.toString()))
                dismiss()
            } else {
                Toast.makeText(context, "NoInformation", Toast.LENGTH_LONG).show()
                dismiss()
            }
        }
    }

}
