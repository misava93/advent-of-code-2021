package com.adventofcode.days.three

import kotlin.math.ceil


fun getIndexesOfBinaryNumbersWithMostCommonBitAtPosition(
    binaryBitsByIndex: Map<Int, List<Int>>,
    bitPosition: Int
): List<Int> {
    val allBinaryNumberBitsAtPosition = binaryBitsByIndex[bitPosition] ?: throw Exception("")
    val numBinaryNumbers = allBinaryNumberBitsAtPosition.size
    val mostCommonBit =
        if (allBinaryNumberBitsAtPosition.sum() >= ceil(numBinaryNumbers / 2.0))
            1
        else
            0

    val mostCommonIndices = mutableListOf<Int>()
    allBinaryNumberBitsAtPosition.forEachIndexed { index, bit ->
        if (bit == mostCommonBit)
            mostCommonIndices.add(index)
    }

    return mostCommonIndices
}

fun getIndexesOfBinaryNumbersWithLeastCommonBitAtPosition(
    binaryBitsByIndex: Map<Int, List<Int>>,
    bitPosition: Int
): List<Int> {
    val mostCommonIndices = getIndexesOfBinaryNumbersWithMostCommonBitAtPosition(binaryBitsByIndex, bitPosition)
    val numBinaryNumbers = binaryBitsByIndex[bitPosition]?.size ?: throw Exception("")

    return (0 until numBinaryNumbers).filter { it !in mostCommonIndices.toSet() }
}

fun deleteBinaryNumbersAtIndexes(
    binaryBitsByIndex: Map<Int, List<Int>>,
    indexes: List<Int>
): Map<Int, List<Int>> {
    val outMap = mutableMapOf<Int, List<Int>>()

    binaryBitsByIndex.forEach { (bitIndex, bits) ->
        outMap[bitIndex] = bits.filterIndexed { binaryNumberIndex, _ -> binaryNumberIndex !in indexes.toSet() }
    }

    return outMap
}

fun calculateOxygenGeneratorRating(
    binaryNumbers: List<BinaryNumber>,
    binaryBitsByIndex: Map<Int, List<Int>>,
    currentBitPosition: Int = 0
): BinaryNumber {
    if (binaryNumbers.size == 1)
        return binaryNumbers.first()

    val binaryNumberIndexes = getIndexesOfBinaryNumbersWithMostCommonBitAtPosition(
        binaryBitsByIndex, currentBitPosition
    )
    val binaryNumbersWithMostCommonBitAtPosition = binaryNumbers.filterIndexed { index, _ ->
        index in binaryNumberIndexes.toSet()
    }

    return calculateOxygenGeneratorRating(
        binaryNumbersWithMostCommonBitAtPosition,
        deleteBinaryNumbersAtIndexes(
            binaryBitsByIndex,
            getIndexesOfBinaryNumbersWithLeastCommonBitAtPosition(binaryBitsByIndex, currentBitPosition)
        ),
        currentBitPosition + 1
    )
}

fun calculateCo2ScrubberRating(
    binaryNumbers: List<BinaryNumber>,
    binaryBitsByIndex: Map<Int, List<Int>>,
    currentBitPosition: Int = 0
): BinaryNumber {
    if (binaryNumbers.size == 1)
        return binaryNumbers.first()

    val binaryNumberIndexes = getIndexesOfBinaryNumbersWithLeastCommonBitAtPosition(
        binaryBitsByIndex, currentBitPosition
    )
    val binaryNumbersWithLeastCommonBitAtPosition = binaryNumbers.filterIndexed { index, _ ->
        index in binaryNumberIndexes.toSet()
    }

    return calculateCo2ScrubberRating(
        binaryNumbersWithLeastCommonBitAtPosition,
        deleteBinaryNumbersAtIndexes(
            binaryBitsByIndex,
            getIndexesOfBinaryNumbersWithMostCommonBitAtPosition(binaryBitsByIndex, currentBitPosition)
        ),
        currentBitPosition + 1
    )
}

fun main(args: Array<String>) {
    val binaryNumbers = parseInputFile("/Users/misava/workplace/advent-of-code/src/main/kotlin/com/adventofcode/days/three/input.txt")
    val binaryDigitsByIndex = buildBinaryDigitsByIndexMap(binaryNumbers)
    val oxygenGeneratorRating = calculateOxygenGeneratorRating(
        binaryNumbers = binaryNumbers,
        binaryBitsByIndex = binaryDigitsByIndex
    )
    val co2ScrubberRating = calculateCo2ScrubberRating(
        binaryNumbers = binaryNumbers,
        binaryBitsByIndex = binaryDigitsByIndex
    )

    println(oxygenGeneratorRating.toInt() * co2ScrubberRating.toInt())
}
