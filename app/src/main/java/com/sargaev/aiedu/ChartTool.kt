package com.sargaev.aiedu

import android.graphics.Color
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Scatter
import com.anychart.core.scatter.series.Marker
import com.anychart.enums.MarkerType


class ChartTool(anyChart: AnyChartView) {

    private var minx: Int = 1000000
    private var maxx: Int = -1
    private var miny: Int = 1000000
    private var maxy: Int = -1

    private val scatter: Scatter = AnyChart.scatter()

    init {
        anyChart.setChart(scatter)
    }

    public fun drawLineChart(
        features: Array<DoubleArray>,
        labels: IntArray, coefficients: DoubleArray, name: String = "Scatter Chart"
    ) {
        scatter.removeAllSeries()
        val (entriesRed, entriesBlue) = getDataPoints(features, labels)
        getPointSeries(entriesRed, "#FF0000", 1f, "class 0", scatter)
        getPointSeries(entriesBlue, "#0000FF", 1f, "class 1", scatter)
        val linePoints = getLinePoints(
            coefficients[0], coefficients[1], coefficients[2]
        )
        getPointSeries(linePoints, "#00FF00", 3f, "Linear Fit", scatter, lineFlag = true)
        setChartSettings(scatter, name)
    }

    private fun getLinePoints(
        w1: Double,
        w2: Double,
        b: Double,
        min: Int = this.minx,
        max: Int = this.maxx
    ): List<DataEntry> {
        val linePoints = mutableListOf<DataEntry>()
        linePoints.add(ValueDataEntry(min.toFloat(), -(w1 / w2) * min - (b / w2).toFloat()))
        linePoints.add(ValueDataEntry(max.toFloat(), -(w1 / w2) * max - (b / w2).toFloat()))

        return linePoints
    }

    private fun getDataPoints(
        features: Array<DoubleArray>,
        labels: IntArray
    ): Pair<List<DataEntry>, List<DataEntry>> {
        val allPoints = mutableListOf<DataEntry>()
        minx = 1000000
        maxx= -1
        miny = 1000000
        maxy = -1
        for(array in features){
            allPoints.add(ValueDataEntry(array[0].toFloat(), array[1].toFloat()))
            if(minx>array[0]){
                minx = array[0].toInt()
            }
            if(maxx<array[0]){
                maxx = array[0].toInt()
            }
            if(miny>array[1]){
                miny = array[1].toInt()
            }
            if(maxy<array[1]){
                maxy = array[1].toInt()
            }
        }
        val entriesRed = mutableListOf<DataEntry>()
        val entriesBlue = mutableListOf<DataEntry>()
        for (i in allPoints.indices) {
            if (labels[i] == 0) {
                entriesRed.add(allPoints[i])
            } else {
                entriesBlue.add(allPoints[i])
            }
        }
        return Pair(entriesRed, entriesBlue)
    }

    private fun getPointSeries(
        points: List<DataEntry>,
        color: String,
        radius: Float,
        label: String,
        scatter: Scatter,
        lineFlag: Boolean = false
    ) {
        if (lineFlag) {
            val line = scatter.line(points)
            line.name(label)
            line.color(color)
            line.stroke("$radius $color")
        } else {
            val series = scatter.marker(points)
            series.name(label)
            series.type(MarkerType.CIRCLE)
            series.fill(color)
            series.stroke(color)
            series.size(radius)
        }
    }

    private fun setChartSettings(
        scatter: Scatter,
        name: String
    ) {
        scatter.title(name)
        scatter.xAxis(0).title("X-Axis")
        scatter.yAxis(0).title("Y-Axis")
        scatter.xScale().minimum(minx).maximum(maxx)
        scatter.yScale().minimum(miny).maximum(maxy)
    }
}