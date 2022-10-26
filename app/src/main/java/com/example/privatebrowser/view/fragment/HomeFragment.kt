package com.example.privatebrowser.view.fragment

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privatebrowser.MainActivity
import com.example.privatebrowser.R
import com.example.privatebrowser.model.App
import com.example.privatebrowser.view.adapter.RecycleViewBookMarkAdapter
import com.example.privatebrowser.view.dialog.DialogAddMore
import com.example.privatebrowser.viewmodel.AppViewModel
import com.example.privatebrowser.viewmodel.TabsViewModel
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : BaseFragment() {
    private val rcyView: RecyclerView by lazy { view!!.findViewById(R.id.rcvBookMark) }
    lateinit var gridLayoutManager: GridLayoutManager
    private val appViewModel: AppViewModel by activityViewModels()
    private val tabsViewModel: TabsViewModel by activityViewModels()
    lateinit var mAdapter: RecycleViewBookMarkAdapter
    override fun layoutID(): Int = R.layout.fragment_home

    override fun initData() {

        mAdapter = RecycleViewBookMarkAdapter()

    }

    override fun initView() {
        homeIcon.visibility = View.INVISIBLE
        lock.visibility = View.INVISIBLE
        topSearchBar.visibility = View.INVISIBLE
        rcyView.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(4)
            gridLayoutManager = GridLayoutManager(requireContext(), 4)
            layoutManager = gridLayoutManager
            adapter = mAdapter
        }
    }

    override fun listenLiveData() {
        appViewModel.getCount.observe(this) {
            if (it == 0) {
                appViewModel.load()
            } else {
                return@observe
            }

        }
        lifecycleScope.launchWhenResumed {
            appViewModel.listApp.collectLatest {
                mAdapter.submitData(it)
            }
        }
        mAdapter.eventClick.observe(viewLifecycleOwner) {
            when (it) {
                is Int -> {
                    DialogAddMore(requireContext()).apply {
                        show()
                        event.observe(viewLifecycleOwner) { app ->
                            appViewModel.insertApp(app)
                        }
                    }
                }
                is App -> {
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.privateBrowser, WebViewFragment(System.currentTimeMillis(),urlNew = it.url)).commit()
                }
                is Long -> {
                     appViewModel.deleteApp(it)
                }
            }
        }
        tabsViewModel.tabRepository.getCount().observe(this) {
            tabsBtn.text = it.toString()
        }
    }

    override fun listener() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goToNewActivity(MainActivity::class.java)
            }
        })

        tabsBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.privateBrowser, TabsFragment()).commit()
        }
        searchView.setQuery("", false)
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(result: String?): Boolean {
                requireActivity().supportFragmentManager.beginTransaction().replace(
                    R.id.privateBrowser, WebViewFragment(System.currentTimeMillis(),"$result"
                    )
                ).commit()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean = false
        })
    }
}

