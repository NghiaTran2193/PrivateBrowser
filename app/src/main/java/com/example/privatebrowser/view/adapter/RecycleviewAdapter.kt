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
import com.example.privatebrowser.model.Tabs
import com.example.privatebrowser.utils.Itemtab
import com.example.privatebrowser.utils.SingleLiveEvent
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_image.*

class RecycleviewAdapter : PagingDataAdapter<Tabs,RecyclerView.ViewHolder>(DiffTabs()){

    class DiffTabs : DiffUtil.ItemCallback<Tabs>() {
        override fun areItemsTheSame(oldItem: Tabs, newItem: Tabs): Boolean {
            return oldItem.id== newItem.id
        }

        override fun areContentsTheSame(oldItem: Tabs, newItem: Tabs): Boolean {
            return oldItem == newItem
        }

    }
    private val listenEventClick = SingleLiveEvent<Any>()
    val eventClick: LiveData<Any> by lazy {
        listenEventClick
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as Itemtab).bindWhenInflated {
            getItem(position)?.let {
                when(holder){
                    is TabViewHolder -> {
                        holder.bind(it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TabViewHolder(Itemtab(parent.context).apply {
            inflate()
        },listenEventClick)
    }

    class TabViewHolder(itemView: View, private val listenerEvent: MutableLiveData<Any>): RecyclerView.ViewHolder(itemView),
            LayoutContainer
    {
        override val containerView: View
            get() = itemView
        fun bind(tabs: Tabs){
            Glide.with(itemView.context).load(tabs.imageWeb).into(itemView.findViewById(R.id.image1))
            tabName.text = tabs.title
            itemView.setOnClickListener {
                listenerEvent.value = tabs
            }
            closeButton.setOnClickListener {
                listenerEvent.value = tabs.id
            }
        }
    }

}