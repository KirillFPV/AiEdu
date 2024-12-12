package com.sargaev.aiedu

import smile.data.DataFrame
import smile.data.vector.DoubleVector
import smile.data.vector.IntVector
import kotlin.random.Random

class DataPrepare {
    companion object {
        fun csv2df(features: Array<DoubleArray>, labels: IntArray): DataFrame {
            val x = DoubleVector.of("Feature1", features.map { it[0] }.toDoubleArray())
            val y = DoubleVector.of("Feature2", features.map { it[1] }.toDoubleArray())
            val target = IntVector.of("Class", labels)

            val df = DataFrame.of(x, y, target)
            return df
        }

        fun f2df(features: Array<DoubleArray>): DataFrame? {
            val x = DoubleVector.of("Feature1", features.map { it[0] }.toDoubleArray())
            val y = DoubleVector.of("Feature2", features.map { it[1] }.toDoubleArray())
            val df = DataFrame.of(x, y)
            return df
        }
        fun trainTestSplit(
            features: Array<DoubleArray>,
            labels: IntArray,
            testSize: Float
        ): Pair<Pair<Array<DoubleArray>, IntArray>, Pair<Array<DoubleArray>, IntArray>> {
            require(features.size == labels.size) { "Features and labels must have the same size" }
            require(testSize in 0.0..1.0) { "Test size must be between 0 and 1" }

            // Создание списка индексов и перемешивание
            val indices = features.indices.toList().shuffled(Random.Default)

            // Определяем размер тестовой выборки
            val testCount = (features.size * testSize).toInt()

            // Разделяем индексы на тестовые и тренировочные
            val testIndices = indices.take(testCount)
            val trainIndices = indices.drop(testCount)

            // Формируем тренировочные и тестовые выборки
            val trainFeatures = trainIndices.map { features[it] }.toTypedArray()
            val trainLabels = trainIndices.map { labels[it] }.toIntArray()

            val testFeatures = testIndices.map { features[it] }.toTypedArray()
            val testLabels = testIndices.map { labels[it] }.toIntArray()

            // Возвращаем пары данных (тренировочные, тестовые)
            return Pair(Pair(trainFeatures, trainLabels), Pair(testFeatures, testLabels))
        }
    }
}