package com.adventofcode.problems.two

import com.adventofcode.days.day_1.calculateNumberOfMeasurementIncreases
import com.adventofcode.days.day_1.parseInputFile

fun generateSlidingWindowMeasurements(
    measurements: List<Int>,
    windowSize: Int
): List<Int> {
    if (measurements.size < windowSize)
        throw IllegalArgumentException("Window size is bigger than measurements")

    return ((windowSize - 1) until (measurements.size)).map { currentIndex ->
        val indices = (0 until windowSize).map { currentIndex - it }
        measurements.slice(indices).sum()
    }
}

fun main(args: Array<String>) {
    val singleMeasurements = parseInputFile("day-1/input.txt")
    val slidingWindowMeasurements = generateSlidingWindowMeasurements(singleMeasurements, 3)
    val numberOfIncrease = calculateNumberOfMeasurementIncreases(slidingWindowMeasurements)

    println(numberOfIncrease)

}
