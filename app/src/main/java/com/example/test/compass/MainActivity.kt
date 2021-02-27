package com.example.test.compass

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator
import com.example.test.compass.common.isVisibleWhen
import com.example.test.compass.databinding.ActivityMainBinding
import com.example.test.compass.ui.CompassViewModel
import com.example.test.compass.ui.DestinationPickActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

private const val LOCATION_PERMISSION_REQUEST_CODE = 100

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var getDesiredLocation: ActivityResultLauncher<Intent>
    private lateinit var getPermissions: ActivityResultLauncher<String>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val compassViewModel: CompassViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = compassViewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {
        registerActivityCallbacks()
        setupListeners()
        setupObservers()
        checkPermissions()
        compassViewModel.init()
    }

    private fun setupListeners() {
        binding.btnPickLocation.setOnClickListener {
            getDesiredLocation.launch(Intent(this, DestinationPickActivity::class.java))
        }
        binding.viewDistance.onSwitchChange = { active -> compassViewModel.switchChanged(active) }
        binding.btnEnableLocation.setOnClickListener { checkGpsStatus() }
    }

    private fun setupObservers() {
        compassViewModel.viewState.observe(this, Observer { viewState ->
            AdditiveAnimator.animate(binding.imgArrow).rotation(viewState.compassAngle).start()
            binding.layoutTurnLeft isVisibleWhen viewState.isTurnLeftVisible
            binding.layoutTurnRight isVisibleWhen viewState.isTurnRightVisible
            binding.viewDistance isVisibleWhen viewState.isCurrentLocationSet
            binding.viewDistanceLoading isVisibleWhen !viewState.isCurrentLocationSet
            binding.btnEnableLocation isVisibleWhen !viewState.isCurrentLocationSet
        })
        compassViewModel.checkPermissionsEvent.observe(this, Observer {
            checkPermissions()
            checkGpsStatus()
        })
    }

    private fun checkGpsStatus() {
        val manager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showNoGpsDialog()
        } else {
            showGpsAvailableDialog()
        }
    }

    private fun showNoGpsDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("You need to enable location, do you want to do it now ?")
            setCancelable(false)
            setPositiveButton("Yes") { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        }.also {
            it.show()
        }
    }

    private fun showGpsAvailableDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("GPS is enabled, please wait for location update..")
            setCancelable(false)
            setPositiveButton("OK") { dialog, _ -> dialog.cancel() }
        }.also {
            it.show()
        }
    }

    private fun registerActivityCallbacks() {
        getDesiredLocation = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            DestinationPickActivity.getResult(it.data)?.let { location ->
                compassViewModel.setDesiredLocation(location)
            }
        }
        getPermissions = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) recreate()
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            )
                requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermissions.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            getPermissions.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}
