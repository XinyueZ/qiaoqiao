package com.qiaoqiao.core.camera.ui

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Bundle.EMPTY
import android.provider.MediaStore
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.maps.MapsInitializer
import com.qiaoqiao.R
import com.qiaoqiao.app.App
import com.qiaoqiao.core.camera.CameraContract
import com.qiaoqiao.core.camera.CameraPresenter
import com.qiaoqiao.core.camera.awareness.AwarenessContract
import com.qiaoqiao.core.camera.awareness.AwarenessPresenter
import com.qiaoqiao.core.camera.awareness.map.PlaceWrapper
import com.qiaoqiao.core.camera.awareness.ui.SnapshotPlaceInfoFragment
import com.qiaoqiao.core.camera.crop.CropCallback
import com.qiaoqiao.core.camera.crop.CropContract
import com.qiaoqiao.core.camera.crop.CropPresenter
import com.qiaoqiao.core.camera.crop.model.CropSource
import com.qiaoqiao.core.camera.crop.ui.CropFragment
import com.qiaoqiao.core.camera.history.HistoryContract
import com.qiaoqiao.core.camera.history.HistoryPresenter2
import com.qiaoqiao.core.camera.vision.VisionContract
import com.qiaoqiao.core.camera.vision.VisionPresenter
import com.qiaoqiao.core.camera.vision.model.VisionEntity
import com.qiaoqiao.core.confidence.ConfidenceContract
import com.qiaoqiao.core.confidence.ConfidencePresenter
import com.qiaoqiao.core.detail.ui.DetailActivity
import com.qiaoqiao.databinding.ActivityCameraBinding
import com.qiaoqiao.repository.backend.model.wikipedia.geo.Geosearch
import com.qiaoqiao.views.ViewPagerAdapter
import de.greenrobot.event.EventBus
import de.greenrobot.event.Subscribe
import java.util.*
import javax.inject.Inject

const val REQ_FILE_SELECTOR = 0x19
private const val LAYOUT = R.layout.activity_camera
private val EXTRAS_BACK_CAMERA = CameraActivity::class.java.name + ".EXTRAS.back"

class CameraActivity : AppCompatActivity(), CameraContract.View, View.OnClickListener, AppBarLayout.OnOffsetChangedListener, FragmentManager.OnBackStackChangedListener, CropCallback, NavigationView.OnNavigationItemSelectedListener {

    internal var snackbar: Snackbar? = null
    internal var binding: ActivityCameraBinding? = null
    internal var alreadyOnBottom: Boolean = false

    @Inject lateinit var mCropPresenter: CropPresenter
    @Inject lateinit var mCameraPresenter: CameraPresenter
    @Inject lateinit var mVisionPresenter: VisionPresenter
    @Inject lateinit var mHistoryPresenter2: HistoryPresenter2
    @Inject lateinit var mAwarenessPresenter: AwarenessPresenter
    @Inject lateinit var mConfidencePresenter: ConfidencePresenter

    @Inject lateinit var mCropFragment: CropContract.View
    @Inject lateinit var mVisionFragment: VisionContract.View
    @Inject lateinit var mHistoryFragment2: HistoryContract.View2
    @Inject lateinit var mConfidenceFragment: ConfidenceContract.View
    @Inject lateinit var mSnapshotPlacesFragment: AwarenessContract.View

    companion object {
        //------------------------------------------------

        fun showInstance(cxt: Activity, backCamera: Boolean) {
            val intent = Intent(cxt, CameraActivity::class.java)
            intent.putExtra(EXTRAS_BACK_CAMERA, backCamera)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            ActivityCompat.startActivity(cxt, intent, EMPTY)
        }
    }
    //------------------------------------------------
    //Subscribes, event-handlers
    //------------------------------------------------

    /**
     * Handler for [com.qiaoqiao.core.camera.awareness.bus.OpenClusterItemEvent].
     *
     * @param e Event [com.qiaoqiao.core.camera.awareness.bus.OpenClusterItemEvent].
     */
    @Subscribe
    fun onEvent(e: com.qiaoqiao.core.camera.awareness.bus.OpenClusterItemEvent) {
        val clusterItem = e.clusterItem
        if (clusterItem is Geosearch) {
            DetailActivity.showInstance(this, clusterItem.pageId)
            return
        }
        if (clusterItem is PlaceWrapper) {
            SnapshotPlaceInfoFragment.newInstance(this, clusterItem)
                    .show(supportFragmentManager, null)
        }
    }

    internal val isBackCamera: Boolean
        get() = intent.getBooleanExtra(EXTRAS_BACK_CAMERA, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(this)
        PermissionHelper.setCameraActivity(this)
        setupDataBinding()
        setupAppBar()
        setupNavigationDrawer()
        App.inject(this)
        PermissionHelper.requireCameraPermission()
        supportFragmentManager.addOnBackStackChangedListener(this)
    }

    private fun setupDataBinding() {
        binding = DataBindingUtil.setContentView(this, LAYOUT)
        binding?.clickHandler = this
    }

    @Inject
    fun onInjected() {
        //Views(Fragments), presenters of vision, history are already created but they should be shown on screen.
        setupViewPager((mVisionFragment as Fragment?)!!)
        presentersBegin()
    }

    private fun setupNavigationDrawer() {
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }


        binding?.let {
            with(object : ActionBarDrawerToggle(this, it.drawerLayout, R.string.app_name, R.string.app_name) {
                override fun onDrawerOpened(drawerView: View) {}
            }) {
                it.actionBarToggle = this
                it.drawerLayout.setDrawerListener(this)
                it.navView.setNavigationItemSelectedListener(this@CameraActivity)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault()
                .register(this)

        binding?.actionBarToggle?.syncState()
    }

    override fun onPause() {
        EventBus.getDefault()
                .unregister(this)
        super.onPause()
    }

    override fun onBackStackChanged() {
        supportInvalidateOptionsMenu()
    }

    private fun setupAppBar() {
        binding?.let {
            it.appbar.addOnOffsetChangedListener(this)
            showCameraOnly()
            setSupportActionBar(it.toolbar)
            val supportActionBar = supportActionBar
            if (supportActionBar != null) {
                supportActionBar.setHomeButtonEnabled(true)
                supportActionBar.setDisplayShowTitleEnabled(false)
            }

            val params = it.appbar.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = AppBarLayout.Behavior()
            behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {

                    //MapView, CameraView , CoordinatorLayout and CollapsingToolbarLayout make scroll-effect difficultly , let's use a solution.
                    //http://stackoverflow.com/questions/31046147/android-mapview-in-a-collapsingtoolbarlayout
                    return false
                }
            })
            params.behavior = behavior
        }
    }

    private fun setDownAppBar() {
        binding?.appbar?.removeOnOffsetChangedListener(this)
    }

    internal fun showCameraOnly() {
        binding?.appbar?.setExpanded(true, true)
    }

    internal fun showVisionOnly() {
        binding?.let {
            it.appbar.setExpanded(false, true)
            it.expandMoreBtn.visibility = GONE
            it.expandLessBtn.visibility = VISIBLE
        }
    }

    private fun presentersBegin() {
        mCropPresenter.begin(this)
        mCameraPresenter.begin(this)
        mCameraPresenter.setVisionPresenter(mVisionPresenter)
        mVisionPresenter.begin(this)
        mHistoryPresenter2.begin(this)
        mHistoryPresenter2.visionPresenter = mVisionPresenter
        mAwarenessPresenter.begin(this)
        mConfidencePresenter.begin(this)
    }

    override fun onDestroy() {
        supportFragmentManager.removeOnBackStackChangedListener(this)
        setDownAppBar()
        presentersEnd()
        super.onDestroy()

        try {
            binding?.let {
                it.preview.stop()
                it.preview.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun presentersEnd() {
        mCropPresenter.end(this)
        mCameraPresenter.end(this)
        mVisionPresenter.end(this)
        mHistoryPresenter2.end(this)
        mAwarenessPresenter.end(this)
        mConfidencePresenter.end(this)
    }

    override fun showInputFromWeb(v: android.view.View?) {
        WebLinkActivity.showInstance(this, v)
    }

    override fun showLoadFromLocal(v: android.view.View?) {
        PermissionHelper.requireReadExternalStoragePermission()
    }

    override fun setPresenter(presenter: CameraPresenter) {
        mCameraPresenter = presenter
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Scenario.onActivityResult(this, requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showError(errorMessage: String) {
        binding?.let {
            with(Snackbar.make(it.root, errorMessage, Snackbar.LENGTH_LONG)
                    .setAction(android.R.string.ok, this)) {
                show()
                snackbar = this
            }
        }
    }

    override fun onClick(v: View) {
        ClickHandler.onClick(this, v)
    }

    internal fun openLocalDir() {
        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQ_FILE_SELECTOR)
    }

    override fun openCrop(cropSource: CropSource) {
        mCropPresenter.setCropSource(cropSource)
        supportFragmentManager.beginTransaction()
                .add(R.id.container,
                        mCropFragment as Fragment?,
                        mCropFragment.javaClass
                                .name)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    private fun closeCropView() {
        if ((mCropFragment as CropFragment).isAdded) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun onCropped(bytes: ByteArray) {
        mCameraPresenter.findAnnotateImages(bytes)
    }

    override fun onCroppedFail() {
        closeCropView()
    }

    override fun getBinding(): ActivityCameraBinding {
        return binding!!
    }

    override fun updateViewWhenResponse(visionEntity: VisionEntity) {
        binding?.let {
            it.barTitleLoadingPb.stopShimmerAnimation()
            closeCropView()
            when {
                it.viewpager.currentItem != 0 -> it.viewpager.currentItem = 0
            }

            SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setContentText(getString(R.string.vision_successfully_title))
                    .setTitleText(getString(R.string.vision_successfully_content, visionEntity.description))
                    .show()
        }
    }

    private fun setupViewPager(visionFragment: Fragment) {
        val adapter = ViewPagerAdapter(supportFragmentManager, object : ArrayList<Fragment>() {
            init {
                add(visionFragment)
            }
        }, object : ArrayList<String>() {
            init {
                add(getString(R.string.tab_vision))
            }
        })
        binding?.let {
            it.viewpager.adapter = adapter
            it.tabs.setupWithViewPager(it.viewpager)
        }
    }

    override fun updateViewWhenRequest() {
        mVisionPresenter.setRefreshing(true)
        binding?.barTitleLoadingPb?.startShimmerAnimation()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        Scenario.onOffsetChanged(this, appBarLayout, verticalOffset)
    }

    override fun onBackPressed() {
        if (BackPressHandler.onBackPressed(this)) {
            return
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_camera, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        Scenario.adjustUIForDifferentFragmentScenario(this, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuHandler.onOptionsItemSelected(this, item) || super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        MenuHandler.onNavigationItemSelected(this, item)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun tfOutput(tfOutput: String?) {
        binding?.tfTv?.text = tfOutput
    }
}
