package com.adventofcode.days.day_6

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.Arrays


fun main(args: Array<String>) {
    val lanternFish = parseInputFile("day-6/input.txt")

    // NOTE: this requires really high memory due to exponential growth in population
    println(getNumberOfLanternfish(lanternFish, 256))
}
