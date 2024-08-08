package com.montanhajr.seeforme.util

import kotlin.math.sqrt

fun String.cosineSimilarity(textToCompare: String): Double {
    val words1 = this.split(" ").groupingBy { it }.eachCount()
    val words2 = textToCompare.split(" ").groupingBy { it }.eachCount()

    val allWords = words1.keys + words2.keys
    val vector1 = allWords.map { words1[it] ?: 0 }
    val vector2 = allWords.map { words2[it] ?: 0 }

    val dotProduct = vector1.zip(vector2).sumOf { it.first * it.second }
    val magnitude1 = sqrt(vector1.sumOf { it * it }.toDouble())
    val magnitude2 = sqrt(vector2.sumOf { it * it }.toDouble())

    return if (magnitude1 == 0.0 || magnitude2 == 0.0) 0.0 else dotProduct / (magnitude1 * magnitude2)
}