package com.example.privatebrowser.view.fragment

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privatebrowser.R
import com.example.privatebrowser.database.RoomDB
import com.example.privatebrowser.database.TabDao
import com.example.privatebrowser.model.Tabs
import com.example.privatebrowser.view.adapter.RecycleviewAdapter
import com.example.privatebrowser.viewmodel.TabsViewModel
import com.github.hariprasanths.bounceview.BounceView
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.item_dialog_add.*
import kotlinx.coroutines.flow.collectLatest


class TabsFragment: BaseFragment(){
    private  val tabsViewModel : TabsViewModel by activityViewModels()
    private val recyclerView: RecyclerView by lazy { view!!.findViewById(R.id.recyclerViewTabs) }
    lateinit var rcvAdapter: RecycleviewAdapter
    lateinit var gridLayoutManager : GridLayoutManager
//    private var roomDB : RoomDB? = null
//    private lateinit var tabDao: TabDao
    override fun layoutID(): Int = R.layout.fragment_tabs

    override fun initData() {
        BounceView.addAnimTo(tabsBtn).setScaleForPopOutAnim(1.1f,1.1f)
        BounceView.addAnimTo(homeIcon).setScaleForPopOutAnim(1.1f,1.1f)
        rcvAdapter = RecycleviewAdapter()
    }

    override fun initView() {
        topSearchBar.visibility = View.INVISIBLE
        loadImage(homeIcon, R.drawable.ic_add_tab)
        recyclerView.apply {
            setHasFixedSize(true)
            gridLayoutManager = GridLayoutManager(requireContext(),2)
            layoutManager = gridLayoutManager
            adapter = rcvAdapter
        }
        lifecycleScope.launchWhenResumed {
            tabsViewModel.listApp.collectLatest {
                rcvAdapter.submitData(it)
            }
        }
    }

    override fun listenLiveData() {
        rcvAdapter.eventClick.observe(requireActivity()){
            when(it){
                is Tabs -> {
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.privateBrowser,WebViewFragment(it.id, it.urlNew)).commit()
                }
                is Long -> {
                   tabsViewModel.deleteTab(it)
                }
            }
        }
        tabsViewModel.tabRepository.getCount().observe(this){
            tabsBtn.text = it.toString()
        }
    }

    override fun listener() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.privateBrowser,HomeFragment()).commit()
            }
            })
        homeIcon.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.privateBrowser,HomeFragment()).commit()
        }
    }
}