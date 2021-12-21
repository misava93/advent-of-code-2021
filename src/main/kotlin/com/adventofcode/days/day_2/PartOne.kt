package com.adventofcode.problems.three

import com.adventofcode.utils.readFileFromClasspath

data class Position(
    val x: Int = 0,
    val y: Int = 0
)

fun Position.applyMovement(movement: Movement): Position {
    return when (movement.direction) {
        Direction.RIGHT ->
            this.copy(x = this.x + movement.magnitude)
        Direction.UP ->
            // since the Y axis represents depth, moving up decreases it
            this.copy(y = this.y - movement.magnitude)
        Direction.DOWN ->
            this.copy(y = this.y + movement.magnitude)
    }
}

fun Position.applyMovements(movements: List<Movement>): Position {
    var currentPosition = this.copy()

    movements.forEach {
        currentPosition = currentPosition.applyMovement(it)
    }

    return currentPosition
}

enum class Direction(val value: String) {
    RIGHT("forward"),
    UP("up"),
    DOWN("down")
}

data class Movement(
    val direction: Direction,
    val magnitude: Int
)

fun parseInputFile(filepath: String): List<Movement> {
    val movements = mutableListOf<Movement>()

    readFileFromClasspath(filepath).forEachLine {
        if (it.isNotBlank()) {
            val tokens = it.split(" ")
            movements.add(
                Movement(
                    direction = Direction.values().first { it.value == tokens[0].trim() },
                    magnitude = tokens[1].trim().toInt()
                )
            )
        }
    }

    return movements
}

fun calculateFinalPosition(movements: List<Movement>): Position {
    return Position(0, 0).applyMovements(movements)
}

fun main(args: Array<String>) {
    val movements = parseInputFile("day-2/input.txt")
    val finalPosition = calculateFinalPosition(movements)

    println(finalPosition.x * finalPosition.y)
}
