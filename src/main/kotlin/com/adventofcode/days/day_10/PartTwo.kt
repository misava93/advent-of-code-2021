package com.adventofcode.days.day_10

import java.lang.IllegalArgumentException
import java.util.ArrayDeque

fun findMissingClosingChars(incompleteLines: List<String>): List<String> {
    val missingClosingChars = mutableListOf<String>()

    incompleteLines.forEach { line ->
        val stack = ArrayDeque<Char>()

        line.forEach { char ->
            if (OpeningChars.isOpeningChar(char))
                stack.push(char)
            else if (ClosingChars.isClosingChar(char))
                stack.pop()
            else
                throw IllegalArgumentException("Found unexpected character: $char")
        }

        val missingCharsInLine = mutableListOf<Char>()
        while (!stack.isEmpty()) {
            missingCharsInLine.add(OpeningChars.getClosingChar(stack.pop()))
        }
        missingClosingChars.add(missingCharsInLine.joinToString(separator = ""))
    }

    return missingClosingChars
}

fun getAutoCompleteScore(line: String): Long {
    var score = (0).toLong()

    line.forEach {
        score = (score * 5) + ClosingChars.getCompletionScore(it)
    }

    return score
}

fun sortAndGetMiddleScore(scores: List<Long>) = scores.sorted()[scores.size / 2]

fun main(args: Array<String>) {
    val lines = parseInputFile("day-10/input.txt")
    val corruptedLines = findCorruptedLines(lines).map { it.line }.toSet()
    val incompleteLines = lines.filterNot { corruptedLines.contains(it) }
    val missingClosingChars = findMissingClosingChars(incompleteLines)
    val autoCompletionScores = missingClosingChars.map { getAutoCompleteScore(it) }

    println(sortAndGetMiddleScore(autoCompletionScores))
}
