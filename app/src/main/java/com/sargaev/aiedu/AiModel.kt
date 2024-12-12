package com.sargaev.aiedu

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import smile.base.mlp.HiddenLayerBuilder
import smile.base.mlp.Layer
import smile.base.mlp.LayerBuilder
import smile.base.mlp.OutputFunction
import smile.classification.mlp
import smile.math.TimeFunction
import smile.vq.hebb.Neuron

class AiModel : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ai_model, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutChoser = view.findViewById<Spinner>(R.id.spinner)
        val layersNames = view.resources.getStringArray(R.array.activations)
        val addButton = view.findViewById<Button>(R.id.button4)
        val learnButton = view.findViewById<Button>(R.id.button3)
        val neuronsCount = view.findViewById<EditText>(R.id.editTextNumberDecimal)
        val aiCompose = view.findViewById<TextView>(R.id.textView26)
        val results = view.findViewById<TextView>(R.id.textView27)
        val lrInput = view.findViewById<EditText>(R.id.editTextNumberDecimal2)
        val epochsInput = view.findViewById<EditText>(R.id.editTextNumberDecimal3)
        val datasetChose = view.findViewById<RadioGroup>(R.id.radioGroup)

        val layers = mutableListOf(Layer.input(2))
        var compose = "Input(2)\n"
        val dataMap = DataContainer.dataMap
        val dataSets = mapOf(
            R.id.radioButton to dataMap["two_clusters"],
            R.id.radioButton3 to dataMap["circle_classification"],
            R.id.radioButton4 to dataMap["spiral_data"],
            R.id.radioButton5 to dataMap["xor_data"],
        )

        addButton.setOnClickListener {
            val layer = layersNames[layoutChoser.selectedItemPosition]
            val count = neuronsCount.text.toString().toInt()
            layers.add(createLayer(layer, count))
            Log.d("Layers", layers.toString())
            compose += "${layer}($count)\n"
            aiCompose.text = compose
        }

        learnButton.setOnClickListener {
            layers.add(Layer.mle(1, OutputFunction.SOFTMAX))
            val epochs = epochsInput.text.toString().toInt()
            val learningRate: TimeFunction = when (lrInput.text.toString()) {
                "" -> TimeFunction.linear(0.01, 10000.0, 0.001)
                else -> TimeFunction.constant(lrInput.text.toString().toDouble())
            }
            val selectedRadioButtonId = datasetChose.checkedRadioButtonId
            val (features, labels) = dataSets[selectedRadioButtonId]
                ?: dataMap["two_clusters"]!!

            // Логика работы с данными
            val (trainData, testData) = DataPrepare.trainTestSplit(features, labels, 0.2f)
            val model = mlp(
                trainData.first,
                trainData.second,
                layers.toTypedArray(),
                epochs,
                learningRate
            )
            val yPredTrain = model.predict(trainData.first)
            val yPredTest = model.predict(testData.first)
            val metrics =
                MetricsPrint.evaluateModel(trainData.second, yPredTrain, testData.second, yPredTest)
            results.text = String.format(
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

    private fun createLayer(layerName: String, neurons: Int): HiddenLayerBuilder? {
        when (layerName) {
            "RELU" -> return Layer.rectifier(neurons)
            "Linear" -> return Layer.linear(neurons)
            "Tanh" -> return Layer.tanh(neurons)
            "Sigmoid" -> return Layer.sigmoid(neurons)
            else -> return Layer.rectifier(neurons)
        }

    }

}