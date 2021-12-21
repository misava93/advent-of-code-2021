package com.adventofcode.days.day_2

import com.adventofcode.problems.three.Direction
import com.adventofcode.problems.three.Movement
import com.adventofcode.problems.three.Position
import com.adventofcode.problems.three.parseInputFile


data class PositionWithAim(
    val position: Position = Position(),
    val aim: Int = 0
)

fun PositionWithAim.applyMovement(movement: Movement): PositionWithAim {
    return when (movement.direction) {
        Direction.RIGHT -> {
            this.copy(
                position = this.position.copy(
                    x = this.position.x + movement.magnitude,
                    y = this.position.y + (this.aim * movement.magnitude)
                ))
        }
        Direction.UP ->
            // since the Y axis represents depth, moving up decreases aim
            this.copy(aim = this.aim - movement.magnitude)
        Direction.DOWN ->
            this.copy(aim = this.aim + movement.magnitude)
    }
}

fun PositionWithAim.applyMovements(movements: List<Movement>): PositionWithAim {
    var currentPosition = this.copy()

    movements.forEach {
        currentPosition = currentPosition.applyMovement(it)
    }

    return currentPosition
}

fun calculateFinalPosition(movements: List<Movement>): PositionWithAim {
    return PositionWithAim().applyMovements(movements)
}

fun main(args: Array<String>) {
    val movements = parseInputFile("day-2/input.txt")
    val finalPosition = calculateFinalPosition(movements)

    println(finalPosition.position.x * finalPosition.position.y)
}
