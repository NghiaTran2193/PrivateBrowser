package com.example.privatebrowser.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.privatebrowser.R
import com.example.privatebrowser.model.App
import com.example.privatebrowser.model.Tabs
import com.example.privatebrowser.utils.ItemApp
import com.example.privatebrowser.utils.Itemtab
import com.github.hariprasanths.bounceview.BounceView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_app.*
import kotlinx.android.synthetic.main.item_image.*

class RecycleViewBookMarkAdapter : PagingDataAdapter<App, RecyclerView.ViewHolder>(DiffApp()) {

    class DiffApp : DiffUtil.ItemCallback<App>() {
        override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem == newItem
        }


    }

    private val listenEventClick = MutableLiveData<Any>()
    val eventClick: LiveData<Any> by lazy {
        listenEventClick
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as ItemApp).bindWhenInflated {
            getItem(position)?.let {
                when (holder) {
                    is AppViewHolder -> {
                        holder.bind(it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppViewHolder(ItemApp(parent.context).apply {
            inflate()
        }, listenEventClick)
    }

    class AppViewHolder(itemView: View, private val listenerEvent: MutableLiveData<Any>) :
        RecyclerView.ViewHolder(itemView),
        LayoutContainer {
        override val containerView: View
            get() = itemView

        fun bind(app: App) {
            if (app.logo == R.drawable.ic_add_more) {
                bookmarkIcon.setImageResource(app.logo)
                BounceView.addAnimTo(itemView).setScaleForPopOutAnim(1.1f, 1.1f)
            }
            else Glide.with(itemView.context).load("https://api.faviconkit.com/${app.url}/100").centerCrop().into(bookmarkIcon)
            bookmarkName.text = app.name
            if (app.isCheck){
                btnDeleteApp.visibility = View.VISIBLE
            }else{
                btnDeleteApp.visibility = View.GONE
            }
            itemView.setOnClickListener {
                if (app.logo == R.drawable.ic_add_more){
                    listenerEvent.value = app.logo
                }else{
                    listenerEvent.value = app
                }
            }
            btnDeleteApp.setOnClickListener {
                listenerEvent.value = app.id
            }
            itemView.setOnLongClickListener {
                if (app.logo == R.drawable.ic_add_more){

                }else{
                    listenerEvent.value= Tabs(id = app.id)
                }
                return@setOnLongClickListener true
            }
        }
    }

}