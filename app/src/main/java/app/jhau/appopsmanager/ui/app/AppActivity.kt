package app.jhau.appopsmanager.ui.app

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppBinding
import app.jhau.appopsmanager.ui.appsetting.AppSettingActivity
import app.jhau.appopsmanager.ui.base.BaseActivity
import app.jhau.appopsmanager.ui.setting.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.LinkedList

@AndroidEntryPoint
class AppActivity : BaseActivity<ActivityAppBinding, AppViewModel>() {
    private lateinit var menu: Menu
    private val attachedChildView = LinkedList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        checkAppPermission()
        collectData()
    }

    private fun checkAppPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val checkPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.QUERY_ALL_PACKAGES
            )
            if (checkPermission != PermissionChecker.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.QUERY_ALL_PACKAGES),
                    REQUEST_PACKAGE_PERMISSION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PACKAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this,
                    getString(R.string.package_permission_notice),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                checkAppPermission()
            }
        }
    }

    private fun collectData() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.CREATED) {
            launch {
                viewModel.appUiState.map { it.apps }.collect {
                    (binding.rvApp.adapter as AppAdapter).submitList(it)
                }
            }

            launch {
                viewModel.appUiState.map { it.sortType }.collect {
                    if (!this@AppActivity::menu.isInitialized) return@collect
                    updateSortTypeView(it)
                }
            }

            launch {
                viewModel.appUiState.map { it.filterTypes }.collect {
                    if (!this@AppActivity::menu.isInitialized) return@collect
                    updateFilterTypesView(it)
                }
            }
        }
    }

    private fun updateFilterTypesView(filterTypes: Set<AppViewModel.FilterType>) {
        menu.findItem(R.id.filter_is_system_app).isChecked =
            filterTypes.contains(AppViewModel.FilterType.SYSTEM_APP)
        menu.findItem(R.id.filter_disabled).isChecked =
            filterTypes.contains(AppViewModel.FilterType.DISABLED)
    }

    private fun initView() {
        binding.rvApp.adapter = AppAdapter(this::onAppClick, this::onLoadIcon)
        setupAppIconLoadOptimize(binding.rvApp, attachedChildView)
    }

    private fun setupAppIconLoadOptimize(
        recyclerView: RecyclerView,
        attachedChildView: LinkedList<View>
    ) {
        recyclerView.addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                attachedChildView.add(view)
            }

            override fun onChildViewDetachedFromWindow(view: View) {
                attachedChildView.remove(view)
            }
        })
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    (binding.rvApp.adapter as AppAdapter).apply {
                        if (!loadOptimize) loadOptimize = true
                    }
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    (binding.rvApp.adapter as AppAdapter).apply {
                        if (loadOptimize) loadOptimize = false
                    }
                    for (child in attachedChildView) {
                        onLoadIcon(child.tag as String, child.findViewById(R.id.app_icon))
                    }
                }
            }
        })
    }

    private fun onLoadIcon(pkgName: String, icon: ImageView) {
        val iconDrawable = viewModel.loadIcon(pkgName)
        icon.setImageDrawable(iconDrawable)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu!!
        updateSortTypeView(viewModel.appUiState.value.sortType)
        updateFilterTypesView(viewModel.appUiState.value.filterTypes)
        setSearchView()
        return super.onPrepareOptionsMenu(menu)
    }

    private fun setSearchView() {
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setOnSearchClickListener {
            menu.findItem(R.id.sort).isVisible = false
            menu.findItem(R.id.filter).isVisible = false
        }
        searchView.setOnCloseListener {
            menu.findItem(R.id.sort).isVisible = true
            menu.findItem(R.id.filter).isVisible = true
            viewModel.clearSearch()
            false
        }
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchApp(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_name -> {
                viewModel.setSortType(AppViewModel.SortType.NAME)
                return true
            }
            R.id.sort_uid -> {
                viewModel.setSortType(AppViewModel.SortType.UID)
                return true
            }
            R.id.filter_is_system_app -> {
                val checked = menu.findItem(R.id.filter_is_system_app).isChecked
                if (checked) {
                    viewModel.removeFilter(AppViewModel.FilterType.SYSTEM_APP)
                } else {
                    viewModel.addFilter(AppViewModel.FilterType.SYSTEM_APP)
                }
                return true
            }
            R.id.filter_disabled -> {
                val checked = menu.findItem(R.id.filter_disabled).isChecked
                if (checked) {
                    viewModel.removeFilter(AppViewModel.FilterType.DISABLED)
                } else {
                    viewModel.addFilter(AppViewModel.FilterType.DISABLED)
                }
                return true
            }
            R.id.settings -> {
                val intent = Intent(this@AppActivity, SettingsActivity::class.java)
                intent.action = Intent.ACTION_VIEW
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun onAppClick(pkgName: String) {
        val pkg = viewModel.getPackageInfo(pkgName) ?: return
        AppSettingActivity.start(this, pkg)
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.appUiState.value.searching) {
            viewModel.fetchPackageInfoList(true)
        }
    }

    private fun updateSortTypeView(type: AppViewModel.SortType) {
        when (type) {
            AppViewModel.SortType.NAME -> {
                menu.findItem(R.id.sort_name).isChecked = true
            }
            AppViewModel.SortType.UID -> {
                menu.findItem(R.id.sort_uid).isChecked = true
            }
        }
    }

    companion object {
        private const val REQUEST_PACKAGE_PERMISSION = 1
    }
}