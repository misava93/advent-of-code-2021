package com.adventofcode.days.day_7

import kotlin.math.abs

fun calculateFuelCostWithChangingRate(
    horizontalPositions: List<Int>,
    solution: Int
): Int =
    horizontalPositions.sumOf { (1..abs(it - solution)).sum() }

fun main (args: Array<String>) {
    val horizontalPositions = parseInputFile("day-7/input.txt")
    println(
        findOptimalHorizontalPosition(horizontalPositions) { positions, solution ->
            calculateFuelCostWithChangingRate(positions, solution)
        }
    )
}
