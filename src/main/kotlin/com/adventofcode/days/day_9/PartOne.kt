package com.adventofcode.days.day_9

import com.adventofcode.utils.readFileFromClasspath

fun parseInputFile(filepath: String): List<List<Int>> =
    readFileFromClasspath(filepath).readLines().map {
        it.map { it.digitToInt() }
    }

fun getAdjacentHeights(
    heightMap: List<List<Int>>,
    rowIndex: Int,
    colIndex: Int
): List<Int> {
    val locationsToCheck = listOf(
        // up
        Pair(rowIndex - 1, colIndex),
        // down
        Pair(rowIndex + 1, colIndex),
        // left
        Pair(rowIndex, colIndex - 1),
        // right
        Pair(rowIndex, colIndex + 1)
    )

    return locationsToCheck.mapNotNull {
        try {
            heightMap[it.first][it.second]
        } catch (ex: IndexOutOfBoundsException) {
            null
        }
    }
}

fun isALowPoint(
    heightMap: List<List<Int>>,
    rowIndex: Int,
    colIndex: Int
): Boolean {
    val center = heightMap[rowIndex][colIndex]
    val adjacentHeights = getAdjacentHeights(heightMap, rowIndex, colIndex)

    return adjacentHeights.minOf { it } > center
}

data class LowestPoint(val height: Int, val rowIndex: Int, val colIndex: Int)

tailrec fun findLowPoints(
    heightMap: List<List<Int>>,
    lowPoints: List<LowestPoint> = listOf(),
    rowIndex: Int = 0,
    colIndex: Int = 0
): List<LowestPoint> {
    val numRows = heightMap.size
    val numCols = heightMap.first().size
    val atBottomRightCorner = (rowIndex == (numRows - 1)) && (colIndex == (numCols - 1))

    val currentLowPoints = if (isALowPoint(heightMap, rowIndex, colIndex))
        lowPoints + LowestPoint(heightMap[rowIndex][colIndex], rowIndex, colIndex)
    else
        lowPoints

    // the terminal condition is if we are in the bottom right corner of the heatmap
    if (atBottomRightCorner) {
        return currentLowPoints
    }

    // if we are at the end of the row, keep recursing from beginning of next row
    if (colIndex == (numCols - 1))
        return findLowPoints(heightMap, currentLowPoints, rowIndex + 1, 0)
    else
        return findLowPoints(heightMap, currentLowPoints, rowIndex, colIndex + 1)

}

fun main(args: Array<String>) {
    val heightMap = parseInputFile("day-9/input.txt")

    val riskLevels = findLowPoints(heightMap).map { it.height + 1 }
    println(riskLevels.sum())
}
