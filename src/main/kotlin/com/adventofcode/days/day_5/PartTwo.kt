package com.adventofcode.days.day_5

fun main(args: Array<String>) {
    val lineSegments = parseInputFile("day-5/input.txt")
    val width = findCoordinateSystemWidth(lineSegments)
    val height = findCoordinateSystemHeight(lineSegments)
    val coordinateSystem = initializeCoordinateSystem(width, height)
    val allLineSegmentPoints = lineSegments.flatMap { getAllLineSegmentPoints(it, true) }
    val talliedCoordinateSystem = tallyPointsInCoordinateSystem(
        coordinateSystem, allLineSegmentPoints
    )

    // solution is the total number of points where at least two lines overlap
    val solution = talliedCoordinateSystem.flatMap {
        it.filter { it > 1 }
    }.size

    println(solution)
}
