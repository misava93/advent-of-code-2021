package com.adventofcode.days.day_6

import com.adventofcode.utils.readFileFromClasspath
import com.adventofcode.utils.splitLine
import java.math.BigInteger

data class LanternFish(
    var internalClock: Int,
    val children: MutableList<LanternFish> = mutableListOf()
) {
    fun reduceClock() {
        children.forEach { it.reduceClock() }
        if (internalClock == 0) {
            internalClock = 6
            children.add(LanternFish(8))
        } else {
            internalClock -= 1
        }
    }

    fun getOffspringSize(): BigInteger {
        if (children.size == 0)
            return BigInteger.valueOf(1)
        return children.sumOf { it.getOffspringSize() }.plus(BigInteger.valueOf(1))
    }
}

fun parseInputFile(filepath: String) =
    readFileFromClasspath(filepath).readLines().flatMap {
        splitLine(it, ",").map { LanternFish(it.toInt()) }
    }

fun getNumberOfLanternfish(
    lanternFish: List<LanternFish>,
    numDays: Int
): BigInteger {
    (1..numDays).forEach { day ->
        lanternFish.forEach { it.reduceClock() }
    }

    println("AFTER REDUCING CLOCK")
    return lanternFish.sumOf { it.getOffspringSize() }
}

fun main(args: Array<String>) {
    val lanternFish = parseInputFile("day-6/input.txt")

    println(getNumberOfLanternfish(lanternFish, 80))
}
