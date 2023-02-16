package app.jhau.appopsmanager.ui.base

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : ViewModelActivity<VB, VM>()