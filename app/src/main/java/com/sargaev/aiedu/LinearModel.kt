package com.sargaev.aiedu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import smile.classification.LogisticRegression
import java.util.logging.Handler


class LinearModel : Fragment(R.layout.fragment_linear_model) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_linear_model, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Теперь findViewById вызывается на view
        val anyChartView = view.findViewById<AnyChartView>(R.id.linear_chart_view)
        val result_text = view.findViewById<TextView>(R.id.textView12)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val learn_btn = view.findViewById<Button>(R.id.button)
        val dataMap = DataContainer.dataMap
        val dataSets = mapOf(
            R.id.radioButton to dataMap["two_clusters"],
            R.id.radioButton3 to dataMap["circle_classification"],
            R.id.radioButton4 to dataMap["spiral_data"],
            R.id.radioButton5 to dataMap["xor_data"],
        )

        val chartTool = ChartTool(anyChartView)

        learn_btn.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            val (features, labels) = dataSets[selectedRadioButtonId]
                ?: dataMap["two_clusters"]!!

            // Логика работы с данными
            val (trainData, testData) = DataPrepare.trainTestSplit(features, labels, 0.2f)
            val lr = LogisticRegression.fit(trainData.first, trainData.second)
            val yPredTrain = lr.predict(trainData.first)
            val yPredTest = lr.predict(testData.first)
            val metrics =
                MetricsPrint.evaluateModel(trainData.second, yPredTrain, testData.second, yPredTest)
            result_text.text = String.format(
                getString(R.string.metrics_text),
                metrics["Accuracy train"] ?: 0.0,
                metrics["Recall train"] ?: 0.0,
                metrics["F1 Score train"] ?: 0.0,
                metrics["Accuracy test"] ?: 0.0,
                metrics["Recall test"] ?: 0.0,
                metrics["F1 Score test"] ?: 0.0
            )
            val coefficients = (lr as LogisticRegression.Binomial).coefficients()
            chartTool.drawLineChart(trainData.first, trainData.second, coefficients, "LogicRegression")
        }
    }
}