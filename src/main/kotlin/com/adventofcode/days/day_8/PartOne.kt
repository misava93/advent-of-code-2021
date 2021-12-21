package com.adventofcode.days.day_8

import com.adventofcode.utils.readFileFromClasspath
import com.adventofcode.utils.splitLine

/**
 * Represents a seven-segment digit signal
 *
 * The [signal] is a string consisting of letters in range [a-g], where each letter represents
 * a segment in a seven-segment digit display
 */
data class SevenSegmentSignal(
    val signal: String
) {
    /**
     * Helper function that tells whether the digit represented by this [signal]
     * uses a unique number of segments
     *
     * NOTE: Only digits 1, 4, 7 and 8 use a unique number of segments
     */
    fun hasUniqueNumberOfSegments(): Boolean =
        when (signal.length) {
            // number 1 is only digit that uses 2 segments
            2 -> true
            // number 7 is only digit that uses 3 segments
            3 -> true
            // number 4 is only digit that uses 4 segments
            4 -> true
            // number 8 is only digit that uses 7 segments
            7 -> true
            else -> false
        }
}

data class InputFileEntry(
    val patterns: List<SevenSegmentSignal>,
    val outputs: List<SevenSegmentSignal>
)

fun parseInputFile(filepath: String): List<InputFileEntry> {
    val entries = mutableListOf<InputFileEntry>()

    readFileFromClasspath(filepath).forEachLine {
        val tokens = it.split("|")
        assert(tokens.size == 2)
        val patterns = splitLine(tokens[0]).map { SevenSegmentSignal(it) }
        val outputs = splitLine(tokens[1]).map { SevenSegmentSignal(it) }

        entries.add(
            InputFileEntry(
                patterns = patterns,
                outputs = outputs
            )
        )
    }

    return entries
}

fun main(args: Array<String>) {
    val entries = parseInputFile("day-8/input.txt")
    val result = entries.flatMap { it.outputs }.filter { it.hasUniqueNumberOfSegments() }

    println(result.size)
}
