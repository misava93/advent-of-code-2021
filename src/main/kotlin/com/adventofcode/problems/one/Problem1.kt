package com.adventofcode.problems.one

import java.io.File

fun parseInputFile(filepath: String): List<Int> {
    val measurements = mutableListOf<Int>()

    File(filepath).forEachLine {
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
    val inputFilepath = "/Users/misava/workplace/advent-of-code/src/main/kotlin/com/adventofcode/problems/one/input.txt"
    val measurements = parseInputFile(inputFilepath)
    val numberOfIncreases = calculateNumberOfMeasurementIncreases(measurements)

    println(numberOfIncreases)
}
