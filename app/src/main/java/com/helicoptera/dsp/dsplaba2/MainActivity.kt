package com.helicoptera.dsp.dsplaba2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chartZero = findViewById<LineChart>(R.id.chart_zero)
        val chartPhase = findViewById<LineChart>(R.id.chart_phase)

        calculate(chartZero, 0.0)
        calculate(chartPhase, PHASE)
    }

    private fun calculate(chart: LineChart, phase: Double) {
        val amplitude = errorByM(N, phase, EXPECTED_AMPLITUDE, ::amplitudeByFourier)
        val rootWith =
            errorByM(N, phase, EXPECTED_ROOT_MEAN_SQUARE, ::rootMeanSquareWithConstantComponent)
        val rootWithout =
            errorByM(N, phase, EXPECTED_ROOT_MEAN_SQUARE, ::rootMeanSquareWithoutConstantComponent)

        val dataSets = mutableListOf<ILineDataSet>()
        dataSets.add(getDataset(amplitude, "Amplitude error", BLUE))
        dataSets.add(getDataset(rootWith, "Root mean sqaure with constant component", YELLOW))
        dataSets.add(getDataset(rootWithout, "Root mean sqaure without constant component", GREEN))

        val data = LineData(dataSets)
        chart.data = data
        chart.invalidate()
    }

    private fun getDataset(
        values: Pair<List<Double>, List<Double>>,
        title: String,
        color: Int
    ): ILineDataSet {
        val entries = mutableListOf<Entry>()
        val xs = values.first
        val xy = values.second
        for (i in xs.indices) {
            val x = xs[i]
            val y = xy[i]
            val entry = Entry(x.toFloat(), y.toFloat())
            entries.add(entry)
        }

        val lineDataSet = LineDataSet(entries, title)
        lineDataSet.setCircleColor(color)

        return lineDataSet
    }

    companion object {
        private const val PHASE = Math.PI / 2
        private const val N = 64
        private const val EXPECTED_AMPLITUDE = 1.0
        private const val EXPECTED_ROOT_MEAN_SQUARE = 0.707

        private val GREEN = ColorTemplate.MATERIAL_COLORS[0]
        private val YELLOW = ColorTemplate.MATERIAL_COLORS[1]
        private val BLUE = ColorTemplate.MATERIAL_COLORS[3]
    }
}