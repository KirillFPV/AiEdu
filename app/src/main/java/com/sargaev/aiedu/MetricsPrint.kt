package com.sargaev.aiedu

import android.util.Log
import smile.validation.metric.Accuracy
import smile.validation.metric.FScore.F1
import smile.validation.metric.Recall

class MetricsPrint {
    companion object {
        fun evaluateModel(
            actualTrain: IntArray,
            predictedTrain: IntArray,
            actualTest: IntArray,
            predictedTest: IntArray
        ): Map<String, Double> {
            Log.d("Accuracy train", Accuracy.of(actualTrain, predictedTrain).toString())
            Log.d("Recall train", Recall.of(actualTrain, predictedTrain).toString())
            Log.d("F1 Score train", F1.score(actualTrain, predictedTrain).toString())
            Log.d("Accuracy test", Accuracy.of(actualTest, predictedTest).toString())
            Log.d("Recall test", Recall.of(actualTest, predictedTest).toString())
            Log.d("F1 Score test", F1.score(actualTest, predictedTest).toString())

            val metrics = mapOf(
                "Accuracy train" to Accuracy.of(actualTrain, predictedTrain),
                "Recall train" to Recall.of(actualTrain, predictedTrain),
                "F1 Score train" to F1.score(actualTrain, predictedTrain),
                "Accuracy test" to Accuracy.of(actualTest, predictedTest),
                "Recall test" to Recall.of(actualTest, predictedTest),
                "F1 Score test" to F1.score(actualTest, predictedTest)
            )
            return metrics
        }
    }
}