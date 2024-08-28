package com.dev.agalperin.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dev.agalperin.R
import com.dev.agalperin.databinding.FragmentHomeBinding
import com.dev.agalperin.utils.KeyboardUtil
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    companion object {
        private const val LINEDATA_LABEL = "points"
        private const val SERVER_ERROR_STATUS = "500"
    }

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

        initChart()

        initViews()
    }

    override fun onResume() {
        super.onResume()
        initErrorHandler()
    }

    private fun initChart() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding?.apply {
                    if (state.isLoading) {
                        // TODO show glide loader
                    } else {

                        val entries = state.points
                            .sortedBy { point -> point.x }
                            .map { point ->
                                Entry(point.x, point.y)
                            }

                        if (entries.isEmpty()) {
                            coordinatesChart.clear()
                            return@collect
                        }

                        val dataset = LineDataSet(entries, LINEDATA_LABEL).apply {
                            color = android.graphics.Color.BLUE
                            valueTextColor = android.graphics.Color.BLACK
                        }

                        val lineData = LineData(dataset)

                        coordinatesChart.apply {
                            coordinatesChart.setVisibleXRangeMaximum(1000f);
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
            val textInputLayout = inputDotsNumberEt
            val launchButton = launchCoordinatesButton

            // Initially disable the button
            launchButton.isEnabled = false

            // Add a TextWatcher to the EditText to listen for text changes
            textInputLayout.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No-op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Check if the input is a valid integer within the range
                    val text = s.toString()
                    val number = text.toIntOrNull()
                    launchButton.isEnabled = number != null && number in 1 until 1000
                }

                override fun afterTextChanged(s: Editable?) {
                    // No-op
                }
            })

            launchButton.setOnClickListener {
                val text = textInputLayout.text.toString()
                val number = text.toInt()

                viewModel.getAllPoints(number)

                KeyboardUtil.hideKeyboard(requireContext(), it)
            }
        }
    }


    private fun initErrorHandler() {
        lifecycleScope.launch {
            viewModel.effects.collect { effect ->
                Log.d("EffectDebug", "Received effect: $effect")
                when (effect) {
                    is HomeScreenEffect.ShowError -> {
                        handleError(effect.error)
                    }
                }
            }
        }
    }


    private fun handleError(error: Throwable) {
        Log.d("ToastDebug", "Handling error: $error")
        val errorMessage = when (error) {
            is HttpException -> {
                when {
                    error.message?.contains(SERVER_ERROR_STATUS) == true -> getString(R.string.server_error_message_ui)
                    else -> getString(R.string.unknown_error_message_ui, error.message)
                }
            }

            else -> getString(R.string.Unknown_error_ui_message, error.message)
        }
        Log.d("ToastDebug", "Toast message: $errorMessage")
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}