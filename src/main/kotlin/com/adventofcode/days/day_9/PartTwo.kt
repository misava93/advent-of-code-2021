package com.adventofcode.days.day_9


data class Location(val rowIndex: Int, val colIndex: Int)

fun findBasinSize(
    heightMap: List<List<Int>>,
    currentRowIndex: Int,
    currentColIndex: Int,
    basinSize: Int = 0,
    locationsVisited: Set<Location> = setOf()
): Pair<Int, Set<Location>> {
    val currentLocationsVisited = locationsVisited + Location(currentRowIndex, currentColIndex)
    val numRows = heightMap.size
    val numCols = heightMap.first().size
    // terminal conditions:
    //  - if we past the edges
    //  - if we find the highest point (e.g. 9)
    //  - if we already passed by the current location
    if (currentRowIndex < 0 || currentRowIndex >= numRows ||
            currentColIndex < 0 || currentColIndex >= numCols)
        return Pair(0, locationsVisited)

    if (heightMap[currentRowIndex][currentColIndex] == 9)
        return Pair(0, locationsVisited)

    if (Location(currentRowIndex, currentColIndex) in locationsVisited)
        return Pair(0, locationsVisited)

    // recurse clockwise
    val upResults = findBasinSize(
        heightMap,
        currentRowIndex - 1,
        currentColIndex,
        basinSize,
        currentLocationsVisited)
    val rightResults = findBasinSize(
        heightMap,
        currentRowIndex,
        currentColIndex + 1,
        basinSize,
        upResults.second)
    val downResults = findBasinSize(
        heightMap,
        currentRowIndex + 1,
        currentColIndex,
        basinSize,
        rightResults.second)
    val leftResults = findBasinSize(
        heightMap,
        currentRowIndex,
        currentColIndex - 1,
        basinSize,
        downResults.second)

    val totalBasinSize = 1 + upResults.first + rightResults.first + downResults.first + leftResults.first
    return Pair(totalBasinSize, leftResults.second)
}

fun main(args: Array<String>) {
    val heightMap = parseInputFile("day-9/input.txt")

    val lowestPoints = findLowPoints(heightMap)
    val basinSizes = lowestPoints.map {
        val size = findBasinSize(heightMap, it.rowIndex, it.colIndex).first
        println("BasinSize=$size")
        size
    }.sortedDescending().take(3).reduce { acc, size -> acc * size }

    println(basinSizes)
}
