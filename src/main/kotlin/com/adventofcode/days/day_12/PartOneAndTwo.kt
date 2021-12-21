package com.adventofcode.days.day_12

import com.adventofcode.utils.readFileFromClasspath
import com.adventofcode.utils.splitLine

data class CaveMap(
    val edges: Map<String, Set<String>>
)

fun parseInputFile(filepath: String): CaveMap {
    val caveMap = mutableMapOf<String, MutableSet<String>>()
    readFileFromClasspath(filepath).readLines().map {
        val tokens = splitLine(it, "-").map { it.trim() }
        val caveOne = tokens[0]
        val caveTwo = tokens[1]

        if (caveOne !in caveMap)
            caveMap[caveOne] = mutableSetOf(caveTwo)
        else
            caveMap[caveOne]?.add(caveTwo)

        if (caveTwo !in caveMap)
            caveMap[caveTwo] = mutableSetOf(caveOne)
        else
            caveMap[caveTwo]?.add(caveOne)
    }

    return CaveMap(caveMap)
}

fun getMostVisitedSmallCaveCount(smallCavesVisited: Map<String, Int>): Int =
    smallCavesVisited.values.maxOrNull() ?: 0

fun traverse(
    caveMap: CaveMap,
    startingPoint: String = "start",
    currentPath: List<String> = listOf(),
    smallCavesVisited: Map<String, Int> = mapOf(),
    routesToEnd: List<List<String>> = listOf(),
    smallCaveVisitLimit: Int = 1,
): List<List<String>>  {
//    println("StartingPoint=$startingPoint")
//    println("CurrentPath=$currentPath")
//    println("SmallCavesVisited=$smallCavesVisited")
//    println("RoutesToEnd=$routesToEnd")
//    println()
    val isSmallCave = startingPoint[0].isLowerCase() && startingPoint != "start"

    if (isSmallCave &&  (startingPoint in smallCavesVisited)) {
        if (getMostVisitedSmallCaveCount(smallCavesVisited) >= smallCaveVisitLimit)
            return routesToEnd
    }
    if ((startingPoint == "start") && currentPath.isNotEmpty())
        return routesToEnd

    val route = currentPath.toMutableList() + startingPoint
    if (startingPoint == "end") {
        return routesToEnd.toMutableList() + listOf(route)
    }

    val currentSmallCavesVisited = smallCavesVisited.toMutableMap()
    if (isSmallCave) {
        if (startingPoint in currentSmallCavesVisited)
            currentSmallCavesVisited[startingPoint] = currentSmallCavesVisited[startingPoint]!! + 1
        else
            currentSmallCavesVisited[startingPoint] = 1
    }

    var currentRoutesToEnd = routesToEnd.map { it.toList() }.toList()
    caveMap.edges[startingPoint]?.forEach {
        currentRoutesToEnd = traverse(
            caveMap,
            it,
            route,
            currentSmallCavesVisited,
            currentRoutesToEnd,
            smallCaveVisitLimit
        )
    }

    return currentRoutesToEnd
}

fun main(args: Array<String>) {
    val caveMap = parseInputFile("day-12/input.txt")
    caveMap.edges.entries.map {
        println(it)
    }

    val partOneAnswer = traverse(
        caveMap
    )
    print("Part One answer = ${partOneAnswer.size}")
    val partTwoAnswer = traverse(
        caveMap,
        smallCaveVisitLimit = 2
    )
    println("Part Two answer = ${partTwoAnswer.size}")
}
