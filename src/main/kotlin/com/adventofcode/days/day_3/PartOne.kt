package com.adventofcode.days.day_3

import com.adventofcode.utils.readFileFromClasspath
import kotlin.math.pow

data class BinaryNumber(
    val digits: List<Int>
)

fun BinaryNumber.toInt(): Int {
    val numDigits = this.digits.size

    return ((numDigits - 1) downTo 0).sumOf {
        val digit = this.digits[numDigits - 1 - it]
        if (digit == 0) {
            0
        } else {
            (2F * digit).pow(it).toInt()
        }
    }
}

fun BinaryNumber.flipDigits(): BinaryNumber =
    this.copy(
        digits = this.digits.map {
            if (it == 0)
                1
            else
                0
        }
    )

fun parseInputFile(filepath: String): List<BinaryNumber> {
    val binaryStrings = mutableListOf<BinaryNumber>()

    readFileFromClasspath(filepath).forEachLine {
        if (it.isNotBlank()) {
            binaryStrings.add(BinaryNumber(it.map { it.toString().toInt() }))
        }
    }

    return binaryStrings
}

fun buildBinaryDigitsByIndexMap(binaryStrings: List<BinaryNumber>): Map<Int, List<Int>> {
    val map = mutableMapOf<Int, MutableList<Int>>()

    binaryStrings.forEach {
        for (i in 0 until (it.digits.size)) {
            val digit = it.digits[i]
            map[i]?.add(digit) ?: map.put(i, mutableListOf(digit))
        }
    }

    return map
}

fun calculateGammaRate(binaryDigitsByIndex: Map<Int, List<Int>>): BinaryNumber {
    val numBinaryStrings = binaryDigitsByIndex[0]?.size ?: throw Exception()

    val digits = (0 until (binaryDigitsByIndex.size)).map {
        val sum = binaryDigitsByIndex[it]?.sum() ?: throw Exception("Error")
        if (sum >= (numBinaryStrings / 2))
            1
        else
            0
    }

    return BinaryNumber(digits)
}

fun calculateEpsilonRate(gammaRate: BinaryNumber) = gammaRate.flipDigits()


fun main(args: Array<String>) {
    val binaryStrings = parseInputFile("day-3/input.txt")
    val binaryDigitsByIndex = buildBinaryDigitsByIndexMap(binaryStrings)
    val gammaRate = calculateGammaRate(binaryDigitsByIndex)
    val epsilonRate = calculateEpsilonRate(gammaRate)

    println(gammaRate.toInt() * epsilonRate.toInt())
}
