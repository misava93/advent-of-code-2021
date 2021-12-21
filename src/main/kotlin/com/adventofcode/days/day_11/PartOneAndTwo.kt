package com.adventofcode.days.day_11

import com.adventofcode.utils.readFileFromClasspath

data class Location(
    val rowIndex: Int,
    val colIndex: Int
)

fun parseInputFile(filepath: String): List<List<Int>> =
    readFileFromClasspath(filepath).readLines().map {
        it.map { it.digitToInt() }
    }

fun findOctopusesThatFlashed(octopuses: List<List<Int>>): Set<Location> {
    val octopusesThatFlashed = mutableSetOf<Location>()
    val numRows = octopuses.size
    val numCols = octopuses.first().size
    (0 until numRows).forEach { rowIndex ->
        (0 until numCols).forEach { colIndex ->
            if (octopuses[rowIndex][colIndex] > 9)
                octopusesThatFlashed.add(Location(rowIndex, colIndex))
        }
    }

    return octopusesThatFlashed
}

fun applyStep(octopuses: List<List<Int>>): Pair<List<List<Int>>, Set<Location>> {
    val octopusesUpdated = octopuses.map {
        it.map { it + 1 }
    }
    val octopusesThatFlashed = findOctopusesThatFlashed(octopusesUpdated)

    return Pair(octopusesUpdated, octopusesThatFlashed)
}


fun spreadFlashes(
    octopuses: List<List<Int>>,
    currentLocation: Location,
    locationsVisited: Set<Location> = setOf(),
): Pair<List<List<Int>>, Set<Location>> {
    if (currentLocation in locationsVisited)
        return Pair(octopuses, locationsVisited)

    try {
        octopuses[currentLocation.rowIndex][currentLocation.colIndex]
    } catch (ex: IndexOutOfBoundsException) {
        return Pair(octopuses, locationsVisited)
    }

    val octopusesUpdated = octopuses.map { it.toMutableList() }.toMutableList()
    octopusesUpdated[currentLocation.rowIndex][currentLocation.colIndex] += 1

    if (octopusesUpdated[currentLocation.rowIndex][currentLocation.colIndex] > 9) {
        val locationsVisitedUpdated = locationsVisited.toMutableSet()
        locationsVisitedUpdated.add(currentLocation)
        // recurse clockwise
        // up
        var result = spreadFlashes(
            octopusesUpdated,
            currentLocation.copy(rowIndex = currentLocation.rowIndex - 1),
            locationsVisitedUpdated
        )
        // up-right
         result = spreadFlashes(
            result.first,
            currentLocation.copy(rowIndex = currentLocation.rowIndex - 1, colIndex = currentLocation.colIndex + 1),
            result.second
        )
        // right
        result = spreadFlashes(
            result.first,
            currentLocation.copy(colIndex = currentLocation.colIndex + 1),
            result.second
        )
        // down-right
        result = spreadFlashes(
            result.first,
            currentLocation.copy(rowIndex = currentLocation.rowIndex + 1, colIndex = currentLocation.colIndex + 1),
            result.second
        )
        // down
        result = spreadFlashes(
            result.first,
            currentLocation.copy(rowIndex = currentLocation.rowIndex + 1),
            result.second
        )
        // down-left
        result = spreadFlashes(
            result.first,
            currentLocation.copy(rowIndex = currentLocation.rowIndex + 1, colIndex = currentLocation.colIndex - 1),
            result.second
        )
        // left
        result = spreadFlashes(
            result.first,
            currentLocation.copy(colIndex = currentLocation.colIndex - 1),
            result.second
        )
        // up-left
        result = spreadFlashes(
            result.first,
            currentLocation.copy(rowIndex = currentLocation.rowIndex - 1, colIndex = currentLocation.colIndex - 1),
            result.second
        )

        return result
    }
    else
        return Pair(octopusesUpdated, locationsVisited)
}

fun resetOctopusesThatFlashes(
    octopuses: List<List<Int>>,
    octopusesThatFlashed: MutableSet<Location>
): List<List<Int>> {
    val octopusesUpdated = octopuses.map { it.toMutableList() }.toMutableList()

    octopusesThatFlashed.forEach {
        octopusesUpdated[it.rowIndex][it.colIndex] = 0
    }

    return octopusesUpdated
}

fun startSimulation(octopuses: List<List<Int>>, numSteps: Int): Int {
    val totalNumOfOctopuses = octopuses.size * octopuses.first().size
    var flashCount = 0
    var octopusesUpdated = octopuses

    (1..numSteps).forEach {
        val stepResult = applyStep(octopusesUpdated)
        octopusesUpdated = stepResult.first
        var octopusesThatFlashed = stepResult.second
        val locationsVisited = mutableSetOf<Location>()

        octopusesThatFlashed.forEach {
            val result = spreadFlashes(
                octopusesUpdated,
                it,
                locationsVisited
            )

            octopusesUpdated = result.first
            locationsVisited += result.second
        }

        octopusesThatFlashed = findOctopusesThatFlashed(octopusesUpdated)
        // part two of the problem: when all octopuses synchronize and flash at the same time
        if (octopusesThatFlashed.size == totalNumOfOctopuses) {
            println("Part two answer: $it")
            return flashCount
        }

        octopusesUpdated = resetOctopusesThatFlashes(octopusesUpdated, octopusesThatFlashed.toMutableSet())
        flashCount += octopusesThatFlashed.size
    }

    return flashCount
}

fun main(args: Array<String>) {
    val octopuses = parseInputFile("day-11/input.txt")

    println("Part One answer: ${startSimulation(octopuses, 100)}")
    // part two
    startSimulation(octopuses, Int.MAX_VALUE)
}
