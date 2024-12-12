package com.sargaev.aiedu

import android.content.Context
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import smile.data.DataFrame
import java.io.InputStreamReader


class CSVReader(private val context: Context) {
    public fun readCsvData(id: Int): Pair<Array<DoubleArray>, IntArray> {
        val inputStream = context.resources.openRawResource(id) // CSV файл
        val reader = InputStreamReader(inputStream)
        val csvParser = CSVParser(reader, CSVFormat.Builder.create(CSVFormat.DEFAULT)
            .setHeader()
            .setSkipHeaderRecord(true)
            .build())

        val featuresList = mutableListOf<DoubleArray>()
        val labelsList = mutableListOf<Int>()

        for (record: CSVRecord in csvParser) {
            val cord1 = record.get("x").toDouble()
            val cord2 = record.get("y").toDouble()
            val label = record.get("class").toInt()

            featuresList.add(doubleArrayOf(cord1, cord2))
            labelsList.add(label)
        }

        return Pair(featuresList.toTypedArray(), labelsList.toIntArray())
    }
}