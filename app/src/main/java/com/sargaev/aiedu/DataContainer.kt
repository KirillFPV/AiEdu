package com.sargaev.aiedu

import android.content.Context

object DataContainer{
    lateinit var dataMap: Map<String, Pair<Array<DoubleArray>, IntArray>>

    fun initialize(context: Context) {
        val csvReader = CSVReader(context)
        dataMap = mapOf(
            "two_clusters" to csvReader.readCsvData(R.raw.two_clusters),
            "circle_classification" to csvReader.readCsvData(R.raw.circle_classification),
            "spiral_data" to csvReader.readCsvData(R.raw.spiral_data),
            "xor_data" to csvReader.readCsvData(R.raw.xor_data)
        )
    }
}