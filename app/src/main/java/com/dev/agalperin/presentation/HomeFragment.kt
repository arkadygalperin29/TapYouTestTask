package com.dev.agalperin.presentation

import android.annotation.SuppressLint
import android.net.http.HttpException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dev.agalperin.R
import com.dev.agalperin.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException


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

        initErrorHandler()

        binding?.apply {
            launchCoordinatesButton.setOnClickListener {
                viewModel.getAllPoints(24)
            }
        }
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

    private fun initErrorHandler() {
        lifecycleScope.launch {
            viewModel.effects.collect { effect ->
                when (effect) {
                    is HomeScreenEffect.ShowError -> {
                        handleError(effect.error)
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private fun handleError(error: Throwable) {
        when (error) {
            is HttpException -> {
                val errorMessage = when {
                    error.message?.contains(SERVER_ERROR_STATUS) == true -> getString(R.string.server_error_message_ui)
                    else -> getString(R.string.unknown_error_message_ui, error.message)
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(requireContext(),
                    getString(R.string.Unknown_error_ui_message, error.message), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}