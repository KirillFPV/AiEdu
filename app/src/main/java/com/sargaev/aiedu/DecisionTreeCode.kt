package com.sargaev.aiedu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import smile.base.cart.SplitRule
import smile.classification.DecisionTree
import smile.data.formula.Formula

class DecisionTreeCode : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_decision_tree, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val result_text = view.findViewById<TextView>(R.id.textView19)
        val datasetChose = view.findViewById<RadioGroup>(R.id.radioGroup)
        val splitRuleChose = view.findViewById<RadioGroup>(R.id.radioGroup2)
        val learn_btn = view.findViewById<Button>(R.id.button2)
        val maxDepthInput = view.findViewById<EditText>(R.id.editTextTextMultiLine)
        val maxNodesInput = view.findViewById<EditText>(R.id.editTextTextMultiLine2)
        val nodeSizeInput = view.findViewById<EditText>(R.id.editTextTextMultiLine3)
        val dataMap = DataContainer.dataMap
        val dataSets = mapOf(
            R.id.radioButton to dataMap["two_clusters"],
            R.id.radioButton3 to dataMap["circle_classification"],
            R.id.radioButton4 to dataMap["spiral_data"],
            R.id.radioButton5 to dataMap["xor_data"],
        )
        Log.d("DEBUG", "View: $view")
        Log.d("DEBUG", "Button: $learn_btn")
        learn_btn.setOnClickListener {
            val selectedRadioButtonId = datasetChose.checkedRadioButtonId
            val (features, labels) = dataSets[selectedRadioButtonId]
                ?: dataMap["two_clusters"]!!

            // Логика работы с данными
            val (trainData, testData) = DataPrepare.trainTestSplit(features, labels, 0.2f)

            val dfTrain = DataPrepare.csv2df(trainData.first, trainData.second)
            val dfTest = DataPrepare.csv2df(testData.first, testData.second)
            val maxDepth: Int
            val maxNodes: Int
            val nodeSize: Int
            var splitRule = SplitRule.GINI
            if (splitRuleChose.checkedRadioButtonId == R.id.radioButton2){
                splitRule = SplitRule.GINI
            }
            else if (splitRuleChose.checkedRadioButtonId == R.id.radioButton6){
                splitRule = SplitRule.ENTROPY
            }
            if (maxDepthInput.text.toString() == ""){
                maxDepth = Int.MAX_VALUE
            }
            else{
                maxDepth = maxDepthInput.text.toString().toInt()
            }
            if (maxNodesInput.text.toString() == ""){
                maxNodes = Int.MAX_VALUE
            }
            else{
                maxNodes = maxNodesInput.text.toString().toInt()
            }
            if (nodeSizeInput.text.toString() == ""){
                nodeSize = Int.MAX_VALUE
            }
            else{
                nodeSize = nodeSizeInput.text.toString().toInt()
            }

            val decisionTree = DecisionTree.fit(
                Formula.lhs("Class"),
                dfTrain,
                splitRule,
                maxDepth,
                maxNodes,
                nodeSize
            )

            val predictionsTrain = decisionTree.predict(dfTrain)
            val predictionsTest = decisionTree.predict(dfTest)
            val metrics =
                MetricsPrint.evaluateModel(trainData.second, predictionsTrain, testData.second, predictionsTest)
            result_text.text = String.format(
                getString(R.string.metrics_text),
                metrics["Accuracy train"] ?: 0.0,
                metrics["Recall train"] ?: 0.0,
                metrics["F1 Score train"] ?: 0.0,
                metrics["Accuracy test"] ?: 0.0,
                metrics["Recall test"] ?: 0.0,
                metrics["F1 Score test"] ?: 0.0
            )
        }
    }
}