package com.example.privatebrowser.view.activity

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import com.example.privatebrowser.R
import com.example.privatebrowser.repository.TabRepository
import com.example.privatebrowser.utils.SingleLiveEvent
import com.example.privatebrowser.view.fragment.HomeFragment
import com.example.privatebrowser.view.fragment.TabsFragment
import com.example.privatebrowser.view.fragment.WebViewFragment
import com.example.privatebrowser.viewmodel.PrivateBrowserViewModel
import com.example.privatebrowser.viewmodel.TabsViewModel
import java.io.File

class PrivateBrowserActivity : BaseActivity() {
    lateinit var tabRepository : TabRepository
    private  val tabsViewModel : TabsViewModel by viewModels()
    private  val mPrivateBrowserViewModel : PrivateBrowserViewModel by viewModels()
    private val listenEventFragment: SingleLiveEvent<Fragment> = SingleLiveEvent()
    val event: LiveData<Fragment> by lazy {
        listenEventFragment
    }
    private val homeFragment : HomeFragment by lazy {
        HomeFragment()
    }
    private lateinit var webViewFragment : WebViewFragment
    private val tabsFragment : TabsFragment by lazy {
        TabsFragment()
    }
   override fun layoutID(): Int = R.layout.activity_private_browser

    override fun initData() {
       tabRepository = TabRepository(application)
    }

    override fun initView() {
        addOrShowFragment(homeFragment,HomeFragment::class.java.simpleName)
    }

    override fun listenLiveData() {
        mPrivateBrowserViewModel.eventOnce.observe(this){
            when(it){
                is HomeFragment ->{
                    addOrShowFragment(homeFragment,HomeFragment::class.java.simpleName.toString())
                }
                is WebViewFragment ->{
                    addOrShowFragment(webViewFragment,WebViewFragment::class.java.simpleName.toString())
                }
                is TabsFragment ->{
                    addOrShowFragment(tabsFragment,TabsFragment::class.java.simpleName.toString())
                }
            }
        }

    }

    override fun listeners() {

    }

    override fun onPause() {
        File(applicationContext.filesDir.path).deleteRecursively()
        tabsViewModel.deleteAllTab()
        super.onPause()
    }

}