package com.adventofcode.days.day_5

import com.adventofcode.utils.readFileFromClasspath
import com.adventofcode.utils.splitLine
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

data class Point(val x: Int, val y: Int)
data class LineSegment(val start: Point, val end: Point)

fun parseInputFile(filepath: String) =
    readFileFromClasspath(filepath).readLines().map {
        val coordinates = splitLine(it, "->")
            .flatMap { splitLine(it, ",") }
            .map { it.trim().toInt() }
        LineSegment(
            Point(coordinates[0], coordinates[1]),
            Point(coordinates[2], coordinates[3])
        )
    }

fun initializeCoordinateSystem(width: Int, height: Int): List<List<Int>> =
    (0 until height).map {
        (0 until width).map { 0 }.toList()
    }.toList()

fun findCoordinateSystemWidth(lineSegments: List<LineSegment>): Int =
    lineSegments.flatMap { listOf(it.start.x, it.end.x) }.maxOrNull()?.plus(1)
        ?: throw IllegalArgumentException("Empty list")

fun findCoordinateSystemHeight(lineSegments: List<LineSegment>): Int =
    lineSegments.flatMap { listOf(it.start.y, it.end.y) }.maxOrNull()?.plus(1)
        ?: throw IllegalArgumentException("Empty list")

fun getAllLineSegmentPoints(lineSegment: LineSegment, includeDiagonalSegments: Boolean = false): List<Point> {
    // first, find all x coordinates
    val isXIncreasing = lineSegment.start.x < lineSegment.end.x
    val xs = if (isXIncreasing)
        (lineSegment.start.x..lineSegment.end.x).toList()
    else
        (lineSegment.start.x downTo lineSegment.end.x).toList()

    // second, find all ys
    val isYIncreasing = lineSegment.start.y < lineSegment.end.y
    val ys = if (isYIncreasing)
        (lineSegment.start.y..lineSegment.end.y).toList()
    else
        (lineSegment.start.y downTo lineSegment.end.y).toList()

    // third, build points. We use following heuristics:
    //  - if xs and ys have the same number of coordinates, build points out of each pair of coordinates
    //  - if their differs, it means it is a horizontal line. In this case we find the one with the single
    //  coordinate and build points using the coordinates of the other one while keeping the single one fixed
    return if (xs.size == ys.size) {
        if (includeDiagonalSegments)
            (xs.indices).map { Point(xs[it], ys[it]) }
        else
            listOf()
    } else {
        if (xs.size == 1)
            ys.map { Point(xs[0], it) }
        else if (ys.size == 1)
            xs.map { Point(it, ys[0]) }
        else
            throw IllegalStateException("Found invalid coordinates")
    }
}

fun tallyPointsInCoordinateSystem(
    coordinateSystem: List<List<Int>>,
    points: List<Point>
): List<List<Int>> {
    val systemCopy = coordinateSystem.map { it.toMutableList() }.toMutableList()

    points.forEach {
        systemCopy[it.y][it.x] += 1
    }

    return systemCopy
}

fun main(args: Array<String>) {
    val lineSegments = parseInputFile("day-5/input.txt")
    val width = findCoordinateSystemWidth(lineSegments)
    val height = findCoordinateSystemHeight(lineSegments)
    val coordinateSystem = initializeCoordinateSystem(width, height)
    val allLineSegmentPoints = lineSegments.flatMap { getAllLineSegmentPoints(it) }
    val talliedCoordinateSystem = tallyPointsInCoordinateSystem(
        coordinateSystem, allLineSegmentPoints
    )

    // solution is the total number of points where at least two lines overlap
    val solution = talliedCoordinateSystem.flatMap {
        it.filter { it > 1 }
    }.size

    println(solution)
}
