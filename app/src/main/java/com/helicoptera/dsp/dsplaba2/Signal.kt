package com.helicoptera.dsp.dsplaba2

import java.lang.Math.pow
import kotlin.math.*

fun amplitudeByFourier(values: List<Double>): Double {
    var sinSum = 0.0
    var cosSum = 0.0
    val N = values.size.toDouble()
    for (i in values.indices) {
        val value = values[i]
        val angle = 2 * Math.PI * i / N
        sinSum += value * sin(angle)
        cosSum += value * cos(angle)
    }
    sinSum *= 2.0 / N
    cosSum *= 2.0 / N

    return sqrt(sinSum.pow(2.0) + cosSum.pow(2.0))
}

fun rootMeanSquareWithConstantComponent(values: List<Double>): Double {
    val N = values.size.toDouble()
    val squareSum = values.toDoubleArray().sumOf { it.pow(2.0) }
    return sqrt(1.0 / (N + 1.0) * squareSum)
}

fun rootMeanSquareWithoutConstantComponent(values: List<Double>): Double {
    val N = values.size.toDouble()
    val squareSum = values.toDoubleArray().sumOf { it.pow(2.0) }
    return sqrt(1.0 / (N + 1.0) * squareSum - (1.0 / (N + 1.0) * values.toDoubleArray().sum()).pow(2))
}

fun kFromN(n: Int): Int {
    return n / 4
}

fun harmonicSignal(N: Int, M: Int, phase: Double = 0.0): List<Double> {
    val result = mutableListOf<Double>()
    for (x in 0 until M) {
        val value = sin((2 * PI * x) / N + phase)
        result.add(value)
    }

    return result
}

fun errorByM(
    N: Int,
    phase: Double,
    expected: Double,
    calculator: (List<Double>) -> Double
): Pair<List<Double>, List<Double>> {
    val k = kFromN(N)
    val xs = mutableListOf<Double>()
    val ys = mutableListOf<Double>()
    for (i in k..(N * 2)) {
        xs.add(i.toDouble())
        ys.add(expected - calculator(harmonicSignal(N, i, phase)))
    }

    return Pair(xs, ys)
}