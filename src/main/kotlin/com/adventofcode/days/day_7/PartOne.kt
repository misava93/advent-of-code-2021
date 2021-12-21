package com.adventofcode.days.day_7

import com.adventofcode.utils.readFileFromClasspath
import com.adventofcode.utils.splitLine
import java.util.Collections.max
import java.util.Collections.min
import kotlin.math.abs

fun parseInputFile(filepath: String) =
    readFileFromClasspath(filepath).readLines().flatMap {
        splitLine(it, ",").map { it.toInt() }
    }

fun calculateFuelCostWithConstantRate(
    horizontalPositions: List<Int>,
    solution: Int
): Int =
    horizontalPositions.sumOf { abs(it - solution) }

data class PossibleSolution(val horizontalPosition: Int, val fuelCost: Int)

fun findOptimalHorizontalPosition(
    horizontalPositions: List<Int>,
    fuelCostCalculator: (List<Int>, Int) -> Int
): PossibleSolution {
    val smallestPosition = min(horizontalPositions)
    val biggestPosition = max(horizontalPositions)

    return (smallestPosition..biggestPosition).map {
        PossibleSolution(it, fuelCostCalculator(horizontalPositions, it))
    }.sortedBy { it.fuelCost }.first()
}

fun main (args: Array<String>) {
    val horizontalPositions = parseInputFile("day-7/input.txt")
    println(
        findOptimalHorizontalPosition(horizontalPositions) { positions, solution ->
            calculateFuelCostWithConstantRate(positions, solution)
        }
    )
}
