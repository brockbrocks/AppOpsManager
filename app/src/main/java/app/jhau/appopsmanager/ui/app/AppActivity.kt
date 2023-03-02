package app.jhau.appopsmanager.ui.app

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.data.repository.PackageInfoRepository
import app.jhau.appopsmanager.databinding.ActivityAppBinding
import app.jhau.appopsmanager.ui.appinfo.AppInfoActivity
import app.jhau.appopsmanager.ui.base.BaseActivity
import app.jhau.appopsmanager.ui.setting.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppActivity : BaseActivity<ActivityAppBinding, AppViewModel>(), View.OnClickListener {
    private val TAG = "AppActivity"
    private lateinit var menu: Menu

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

    private fun collectData() {
        lifecycleScope.launch {
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
    }

    private fun updateFilterTypesView(filterTypes: Set<PackageInfoRepository.FilterType>) {
        menu.findItem(R.id.filter_is_system_app).isChecked =
            filterTypes.contains(PackageInfoRepository.FilterType.SYSTEM_APP)
        menu.findItem(R.id.filter_disabled).isChecked =
            filterTypes.contains(PackageInfoRepository.FilterType.DISABLED)
    }

    private fun initView() {
        binding.rvApp.adapter = AppAdapter(this) { appItemUiState ->
            viewModel.getAppIcon(appItemUiState)
        }
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
        searchView.setOnQueryTextListener(object : OnQueryTextListener{
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
                viewModel.setSortType(PackageInfoRepository.SortType.NAME)
                return true
            }
            R.id.sort_uid -> {
                viewModel.setSortType(PackageInfoRepository.SortType.UID)
                return true
            }
            R.id.filter_is_system_app -> {
                val checked = menu.findItem(R.id.filter_is_system_app).isChecked
                if (checked) {
                    viewModel.removeFilter(PackageInfoRepository.FilterType.SYSTEM_APP)
                } else {
                    viewModel.addFilter(PackageInfoRepository.FilterType.SYSTEM_APP)
                }
                return true
            }
            R.id.filter_disabled -> {
                val checked = menu.findItem(R.id.filter_disabled).isChecked
                if (checked) {
                    viewModel.removeFilter(PackageInfoRepository.FilterType.DISABLED)
                } else {
                    viewModel.addFilter(PackageInfoRepository.FilterType.DISABLED)
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

    override fun onClick(v: View?) {
        v?.let {
            val position = binding.rvApp.getChildAdapterPosition(it)
            val packageInfo = viewModel.getPackageInfo(position)
            AppInfoActivity.start(this, packageInfo)
        }
    }

    override fun onResume() {
        super.onResume()
        val sortType = viewModel.appUiState.value.sortType
        val filterTypes = viewModel.appUiState.value.filterTypes
        viewModel.fetchPackageInfoList(sortType = sortType, filterTypes = filterTypes)
    }

    private fun updateSortTypeView(type: PackageInfoRepository.SortType) {
        when (type) {
            PackageInfoRepository.SortType.NAME -> {
                menu.findItem(R.id.sort_name).isChecked = true
            }
            PackageInfoRepository.SortType.UID -> {
                menu.findItem(R.id.sort_uid).isChecked = true
            }
        }
    }

    companion object {
        private const val REQUEST_PACKAGE_PERMISSION = 1
    }
}