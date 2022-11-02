package com.example.privatebrowser.view.fragment

import android.annotation.SuppressLint
import android.graphics.*
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.drawToBitmap
import androidx.fragment.app.activityViewModels
import com.example.privatebrowser.R
import com.example.privatebrowser.database.RoomDB
import com.example.privatebrowser.database.TabDao
import com.example.privatebrowser.model.Tabs
import com.example.privatebrowser.viewmodel.TabsViewModel
import com.github.hariprasanths.bounceview.BounceView
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_web_view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class WebViewFragment(var id : Long,var urlNew : String) : BaseFragment() {
    private var romDB: RoomDB? = null
    private var tabDao: TabDao? = null
    private  val tabsViewModel : TabsViewModel by activityViewModels()
    override fun layoutID(): Int = R.layout.fragment_web_view

    override fun initData() {
        BounceView.addAnimTo(tabsBtn).setScaleForPopOutAnim(1.1f,1.1f)
        BounceView.addAnimTo(homeIcon).setScaleForPopOutAnim(1.1f,1.1f)
        webView.apply {
            when {
                URLUtil.isValidUrl(urlNew) -> loadUrl(urlNew)
                urlNew.contains("com", ignoreCase = true) -> loadUrl("https://www.$urlNew")
                else -> loadUrl("https://www.google.com/search?q=$urlNew")
            }
        }
    }

    override fun initView() {
//        marginStatusBar(arrayListOf(homeIcon, tabsBtn))
        homeIcon.visibility = View.VISIBLE
        topSearchBar.visibility = View.VISIBLE
        loadImage(homeIcon, R.drawable.ic_home_24px)
    }
    override fun listenLiveData() {
        tabsViewModel.tabRepository.getCount().observe(this){
            tabsBtn.text = it.toString()
        }
    }
    override fun listener() {
        romDB = RoomDB.getAppDatabase(requireContext())
        tabDao = romDB?.tabDao()
        homeIcon.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.privateBrowser,HomeFragment()).commit()
        }
        tabsBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.privateBrowser,TabsFragment()).commit()

        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if( webView.canGoBack() == true){
                    webView.goBack()
                }else {
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.privateBrowser,HomeFragment()).commit()
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        tabsViewModel.insertTab(Tabs(id,"",urlNew = urlNew))
        webView.apply {
            settings.javaScriptEnabled = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.builtInZoomControls = false
            webViewClient = object : WebViewClient() {
                @SuppressLint("ResourceAsColor")
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    topSearchBar.text = SpannableStringBuilder(url)
                    progressBar.progress = 0
                    progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if ( view == webView) {
                        progressBar.visibility = View.GONE
                    }
                }
            }
            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (view == webView) {
                        progressBar.progress = newProgress
                    }
                }
            }
        }
        tabsViewModel.updateTab(Tabs(imageWeb = "", urlNew = webView.url.toString()))
    }
    private fun saveImage() : String{
        val bitmap = webView!!.drawToBitmap() //Bitmap.createBitmap(addBorder(view.drawToBitmap())!!, 0,dpToPx(60),view.width+10, view.height+10 -dpToPx(60))
        val folder = File(requireContext().filesDir.path +"/image")
        if (!folder.exists()){
            folder.mkdir()
        }
        val file = File(folder, "${System.currentTimeMillis()}.jpg")
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out)
            Log.e(javaClass.name,file.path)
            return file.path

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""

    }
    override fun onPause() {
        super.onPause()
        val path = saveImage()
        val url = webView?.url
        val title = webView.title.toString()
        tabsViewModel.updateTab(Tabs(id, path, url!!,title))
        webView.apply {
            clearCache(true)
            clearHistory()
            clearFormData()
            clearMatches()
            clearSslPreferences()
            CookieManager.getInstance().removeAllCookies(null)
            WebStorage.getInstance().deleteAllData()

        }
    }
}