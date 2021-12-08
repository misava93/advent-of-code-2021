package com.adventofcode.problems.four

import com.adventofcode.problems.three.Direction
import com.adventofcode.problems.three.Movement
import com.adventofcode.problems.three.Position
import com.adventofcode.problems.three.applyMovement
import com.adventofcode.problems.three.applyMovements
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
    val movements = parseInputFile("/Users/misava/workplace/advent-of-code/src/main/kotlin/com/adventofcode/problems/four/input.txt")
    val finalPosition = calculateFinalPosition(movements)

    println(finalPosition.position.x * finalPosition.position.y)
}
