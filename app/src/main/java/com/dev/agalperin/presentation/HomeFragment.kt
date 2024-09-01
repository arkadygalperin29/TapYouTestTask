package com.dev.agalperin.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.dev.agalperin.R
import com.dev.agalperin.databinding.FragmentHomeBinding
import com.dev.agalperin.utils.DecimalInputFilter
import com.dev.agalperin.utils.ErrorType
import com.dev.agalperin.utils.KeyboardUtil
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false).run {
            binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    saveChartImage()
                } else {
                    showPermissionDeniedSnackbar()
                }
            }

        initChart()

        initViews()

        initSaveChartButton(requestPermissionLauncher)

        clearFocusByLayoutTapping()

        clearFocusByChartTapping()

        initErrorHandler()

    }

    private fun initChart() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding?.apply {
                    if (state.isLoading) {
                        showLoader()
                    } else {
                        hideLoader()
                        val entries = state.points
                            .map { point ->
                                Entry(point.x, point.y)
                            }

                        if (entries.isEmpty()) {
                            coordinatesChart.clear()
                            return@collect
                        }

                        val dataset = LineDataSet(
                            entries,
                            requireContext().getString(R.string.linedata_label)
                        ).apply {
                            mode = LineDataSet.Mode.CUBIC_BEZIER
                            cubicIntensity = 0.2f
                            color = android.graphics.Color.BLUE
                            valueTextColor = android.graphics.Color.BLACK
                        }

                        val lineData = LineData(dataset)

                        coordinatesChart.apply {
                            setVisibleXRangeMaximum(1000f);
                            data = lineData
                            invalidate()
                        }
                    }
                }
            }
        }
    }

    private fun initViews() {
        binding?.apply {
            // Just a little addon for samsung keyboards just in case, we don't want a user to print a dot
            // or anything numberDecimal allows outside of numbers depending on locale's.
            // Samsung keyboards are known for not being able to read xml declarations,
            // that's why need a custom DecimalInputFilter for this.
            inputDotsNumberEt.filters = arrayOf(DecimalInputFilter(), InputFilter.LengthFilter(4))

            launchCoordinatesButton.isEnabled = false

            inputDotsNumberEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Check if the input is a valid integer within the range, so we don't allow user to send errors intentionally ;)
                    val text = s.toString()
                    val number = text.toIntOrNull()
                    launchCoordinatesButton.isEnabled = number != null && number in 1 until 1001
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            launchCoordinatesButton.setOnClickListener {
                val text = inputDotsNumberEt.text.toString()
                val number = text.toInt()

                viewModel.getAllPoints(number)

                inputDotsNumberEt.text.clear()

                KeyboardUtil.hideKeyboard(requireContext(), it)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearFocusByLayoutTapping() {
        binding?.apply {
            homeFragmentLayout.setOnTouchListener { _, _ ->
                KeyboardUtil.hideKeyboard(requireContext(), homeFragmentLayout)
                inputDotsNumberEt.clearFocus()
                true
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearFocusByChartTapping() {
        binding?.apply {
            val gestureDetector = GestureDetector(
                requireContext(),
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        KeyboardUtil.hideKeyboard(requireContext(), root)
                        inputDotsNumberEt.clearFocus()
                        return true
                    }
                })

            coordinatesChart.setOnTouchListener { v, event ->
                if (gestureDetector.onTouchEvent(event)) return@setOnTouchListener true
                else return@setOnTouchListener v.onTouchEvent(event)
            }
        }
    }

    private fun checkPermissionsAndSaveImage(requestPermissionLauncher: ActivityResultLauncher<String>) {
        binding?.apply {
            when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    } else {
                        saveChartImage()
                    }
                }

                else -> saveChartImage()
            }
        }
    }

    private fun saveChartImage() {
        binding?.apply {
            val bitmap = Bitmap.createBitmap(
                coordinatesChart.width,
                coordinatesChart.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            coordinatesChart.draw(canvas)

            viewModel.saveChartImage(bitmap)
        }
    }

    private fun initSaveChartButton(requestPermissionLauncher: ActivityResultLauncher<String>) {
        binding?.apply {
            saveChartToStorageButton.setOnClickListener {
                checkPermissionsAndSaveImage(requestPermissionLauncher)
                KeyboardUtil.hideKeyboard(requireContext(), it)
            }
        }
    }

    private fun showImageSavedSnackbar(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "image/png")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        val snackbar = Snackbar.make(
            binding?.root ?: return,
            requireContext().getString(R.string.image_saved_click_to_navigate_to_gallery),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(requireContext().getString(R.string.navigate_to_gallery_after_saved_image_hint)) {
            startActivity(intent)
        }
        snackbar.show()
    }

    private fun showPermissionDeniedSnackbar() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }

        val snackbar = Snackbar.make(
            binding?.root ?: return,
            requireContext().getString(R.string.permission_to_save_images_declined_hint),
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction(requireContext().getString(R.string.navigate_to_settings_snackbar_action_hint)) {
            startActivity(intent)
        }

        snackbar.show()
    }

    private fun showLoader() {
        binding?.apply {
            val loader = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.loader_tap_you
            )
            glide.load(loader).into(loaderIv)
            loaderIv.visibility = View.VISIBLE
            inputDotsNumberEt.isClickable = false
            launchCoordinatesButton.isClickable = false
        }
    }

    private fun hideLoader() {
        binding?.apply {
            loaderIv.visibility = View.GONE
            inputDotsNumberEt.isClickable = true
            launchCoordinatesButton.isClickable = true
        }
    }


    private fun initErrorHandler() {
        lifecycleScope.launch {
            viewModel.effects.collect { effect ->
                when (effect) {
                    is HomeScreenEffect.ShowError -> {
                        handleError(effect.error)
                    }
                    is HomeScreenEffect.ShowImageSavedSnackbar -> {
                        if (effect.uri == null) {
                            Toast.makeText(requireContext(),
                                getString(R.string.couldnt_save_the_uri_for_chart_toast_hint), Toast.LENGTH_LONG)
                        } else {
                            showImageSavedSnackbar(effect.uri)
                        }
                    }
                }
            }
        }
    }


    private fun handleError(error: ErrorType) {
        val errorMessage = when (error) {
            is ErrorType.HttpError -> error.message
            is ErrorType.UndefinedError -> getString(
                R.string.unknown_error_ui_message,
                error.error.message
            )
        }
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}