package com.adventofcode.days.day_8

import java.lang.IllegalArgumentException


/**
 * Provides a way to solve the seven segment digit pattern problem
 */
class SevenSegmentProblemSolver(val inputFileEntry: InputFileEntry) {
    private val digitToWireIndexes = mapOf(
        0 to listOf(0, 1, 2, 3, 4, 5),
        1 to listOf(1, 2),
        2 to listOf(0, 1, 3, 4 ,6),
        3 to listOf(0, 1, 2, 3, 6),
        4 to listOf(1, 2, 5, 6),
        5 to listOf(0, 2, 3, 5, 6),
        6 to listOf(0, 2, 3, 4, 5, 6),
        7 to listOf(0, 1, 2),
        8 to listOf(0, 1, 2, 3, 4, 5, 6),
        9 to listOf(0, 1, 2, 3, 5, 6)
    )



    private fun uniquePatternToDigit(pattern: String): Int =
        when (pattern.length) {
            // number 1 is only digit that uses 2 segments
            2 -> 1
            // number 7 is only digit that uses 3 segments
            3 -> 7
            // number 4 is only digit that uses 4 segments
            4 -> 4
            // number 8 is only digit that uses 7 segments
            7 -> 8
            else -> throw IllegalArgumentException("Pattern '$pattern' is not a unique pattern")
        }

    /**
     * Initialize solution tracker using digits that use unique patterns
     */
    private fun initSolutionTrackerFromUniquePatterns(
        uniquePatterns: List<SevenSegmentSignal>
    ): SevenSegmentProblemSolutionTracker {
        val solutionTracker = SevenSegmentProblemSolutionTracker()
        val lettersFoundFromUniqueSegments = mutableSetOf<Char>()
        val wireIndexesPopulatedFromUniqueSegments = mutableSetOf<Int>()

        uniquePatterns.forEach { pattern ->
            val digit = uniquePatternToDigit(pattern.signal)
            val wireIndexesToAddTo = digitToWireIndexes[digit]
                ?.filter { it !in wireIndexesPopulatedFromUniqueSegments }
            val lettersToAdd = pattern.signal.filter { it !in lettersFoundFromUniqueSegments }
            wireIndexesToAddTo?.forEach { wireIndex ->
                lettersToAdd.forEach { letter ->
                    solutionTracker.addPossibleLetterSolution(wireIndex, letter)
                    lettersFoundFromUniqueSegments.add(letter)
                    wireIndexesPopulatedFromUniqueSegments.add(wireIndex)
                }
            }
        }

        return solutionTracker
    }

    fun decodeOutputNumber(): Int {
        val solution = findSolution()
        val encodedDigitsSorted = getEncodingToDigitMap(solution)
        val patternsSorted = getSortedPatterns(inputFileEntry.outputs)

        return patternsSorted.map {
            encodedDigitsSorted[it]?.toString() ?: throw IllegalArgumentException("Did not find pattern: $it")
        }.joinToString("").toInt()
    }

    private fun findSolution(): SevenSegmentProblemSolutionTracker {
        // start with digits that use unique patterns
        val uniquePatterns = inputFileEntry.patterns
            .filter { it.hasUniqueNumberOfSegments() }
            .sortedBy { it.signal.length }
        val solutionTracker = initSolutionTrackerFromUniquePatterns(uniquePatterns)

        return findSolutionRecursively(solutionTracker)
    }

    private fun findSolutionRecursively(
        solutionTracker: SevenSegmentProblemSolutionTracker,
        currentWireIndex: Int = 0
    ): SevenSegmentProblemSolutionTracker {
        // terminal condition
        if (currentWireIndex > 6) {
            return solutionTracker
        }

        val possibleSolutionsAtWireIndex = solutionTracker.getPossibleSolutionsAtWireIndex(currentWireIndex)
        if (possibleSolutionsAtWireIndex.size == 1)
            return findSolutionRecursively(solutionTracker, currentWireIndex + 1)

        // choose a letter to fix and continue recursing to see if solution is valid
        val solutions = possibleSolutionsAtWireIndex.map {
            val updatedSolutionTracker = solutionTracker.copyAndSetSolutionAtWireIndex(currentWireIndex, it)
            findSolutionRecursively(updatedSolutionTracker, currentWireIndex + 1)
        }

        // return valid solution
        val validSolution = solutions.filter { isSolutionValid(it) }
       return if (validSolution.isNotEmpty()) validSolution.first()  else solutionTracker
    }

    private fun isSolutionValid(
        solutionTracker: SevenSegmentProblemSolutionTracker
    ): Boolean {
        val encodedDigitsSorted = getEncodingToDigitMap(solutionTracker).keys

        val patternsSorted = getSortedPatterns(inputFileEntry.patterns).toSet()

        return encodedDigitsSorted == patternsSorted
    }

    private fun getEncodingToDigitMap(
        solutionTracker: SevenSegmentProblemSolutionTracker
    ): Map<String, Int> =
        (0..9).associateBy { digit ->
            val wireIndexes = digitToWireIndexes[digit] ?: throw IllegalArgumentException("Invalid digit: $digit")
            val encodedDigit = wireIndexes.map { wireIndex ->
                solutionTracker.getPossibleSolutionsAtWireIndex(wireIndex).first()
            }.sorted().joinToString("")

            encodedDigit
        }


    private fun getSortedPatterns(patterns: List<SevenSegmentSignal>) =
        patterns.map { it.signal.toCharArray().sorted().joinToString("") }

    /**
     * Keeps track of possible solutions to the seven wire digit pattern problem
     *
     * [solutionTracker] is a map where each key is a number in range [0-6] and each value is a list
     * of possible letters that belong to that wire segment. Each key represents one of the
     * seven wire segments as follows:
     *
     *    __0__
     *  5|__6__|1
     *  4|__3__|2
     */
    private class SevenSegmentProblemSolutionTracker {
        private var solutionTracker: MutableMap<Int, MutableSet<Char>> = initSolutionTracker()

        private fun initSolutionTracker() =
            (0..6).associateWith { mutableSetOf<Char>() }.toMutableMap()

        fun solutionFound(): Boolean =
            solutionTracker.map {
                it.value
            }.filter { it.size == 1 }.size == 7

        fun addPossibleLetterSolution(wireIndex: Int, letter: Char) =
            solutionTracker[wireIndex]?.add(letter)

        fun print() = (0..6).forEach {
            println("$it -> ${solutionTracker[it]}")
        }

        fun getPossibleSolutionsAtWireIndex(wireIndex: Int) =
            solutionTracker[wireIndex] ?: throw IllegalArgumentException("Invalid wire index: $wireIndex")

        fun copyAndSetSolutionAtWireIndex(wireIndex: Int, letter: Char): SevenSegmentProblemSolutionTracker {
            val solutionTrackerCopy = copySolutionTracker().map {
                if (it.key == wireIndex)
                    it.key to mutableSetOf(letter)
                else
                    // we need to also remove this letter from possible solutions at other indexes
                    it.key to it.value.filter { it != letter }.toMutableSet()
            }.toMap().toMutableMap()

            val copy = SevenSegmentProblemSolutionTracker()
            copy.setSolutionTracker(solutionTrackerCopy)

            return copy
        }

        private fun setSolutionTracker(solutionTracker: MutableMap<Int, MutableSet<Char>>) {
            this.solutionTracker = solutionTracker
        }

        private fun copySolutionTracker() =
            solutionTracker.map { it.key to it.value.toMutableSet() }.toMap().toMutableMap()
    }
}

fun decodeEntry(entry: InputFileEntry): Int = SevenSegmentProblemSolver(entry).decodeOutputNumber()


fun main(args: Array<String>) {
    val entries = parseInputFile("day-8/input.txt")
    println(entries.sumOf { decodeEntry(it) })
}
