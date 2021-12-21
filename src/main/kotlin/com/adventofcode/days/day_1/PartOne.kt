package com.adventofcode.days.day_1

import com.adventofcode.utils.readFileFromClasspath

fun parseInputFile(filepath: String): List<Int> {
    val measurements = mutableListOf<Int>()

   readFileFromClasspath(filepath).forEachLine {
        try {
            measurements.add(it.toInt())
        } catch (ex: Exception) {
            // do nothing
        }
    }

    return measurements
}

fun calculateNumberOfMeasurementIncreases(
    measurements: List<Int>
): Int {
    var previous = Int.MAX_VALUE

    return measurements.sumOf {
        val result =  if (it > previous) {
            1 as Int
        }
        else {
            previous = it
            0 as Int
        }

        previous = it
        result
    }
}

fun main(args: Array<String>) {
    val measurements = parseInputFile("day-1/input.txt")
    val numberOfIncreases = calculateNumberOfMeasurementIncreases(measurements)

    println(numberOfIncreases)
}
