package com.dev.agalperin.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dev.agalperin.R
import com.dev.agalperin.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

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

        binding?.apply {
            launchCoordinatesButton.setOnClickListener {
                viewModel.getAllPoints(24)
            }
        }

        initChart()
    }

    private fun initChart() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                // Update UI based on state
                if (state.isLoading) {
                    // TODO show glide loader
                } else {
                    // TODO Hide loader
                    val entries = state.points.map { point ->
                        Entry(point.x.toFloat(), point.y.toFloat())
                    }
                    val dataset = LineDataSet(entries, "points")
                    dataset.color = android.graphics.Color.BLUE
                    dataset.valueTextColor = android.graphics.Color.BLACK

                    val lineData = LineData(dataset)
                    binding?.apply {
                        coordinatesChart.data = lineData
                        coordinatesChart.invalidate()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}