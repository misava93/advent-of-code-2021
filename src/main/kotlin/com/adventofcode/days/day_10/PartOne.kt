package com.adventofcode.days.day_10

import com.adventofcode.utils.readFileFromClasspath
import java.lang.IllegalArgumentException
import java.util.ArrayDeque


fun parseInputFile(filepath: String): List<String> =
    readFileFromClasspath(filepath).readLines().map { it.trim() }

object OpeningChars {
    private val OPENING_CHARS = setOf('(', '[', '{', '<')

    fun isOpeningChar(char: Char) =
        OPENING_CHARS.contains(char)

    fun getClosingChar(char: Char): Char =
        when (char) {
            '(' -> ')'
            '[' -> ']'
            '{' -> '}'
            '<' -> '>'
            else -> throw IllegalArgumentException("Unexpected character: $char")
        }
}

object ClosingChars {
    private val CLOSING_CHARS_WITH_CORRUPTION_SCORES = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )
    private val CLOSING_CHARS_WITH_COMPLETION_SCORES = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    fun isClosingChar(char: Char) =
        CLOSING_CHARS_WITH_CORRUPTION_SCORES.contains(char)

    fun getOpeningChar(char: Char): Char =
        when (char) {
            ')' -> '('
            ']' -> '['
            '}' -> '{'
            '>' -> '<'
            else -> throw IllegalArgumentException("Unexpected character: $char")
        }

    fun getCorruptionScore(char: Char) =
        CLOSING_CHARS_WITH_CORRUPTION_SCORES[char]
            ?: throw IllegalArgumentException("Char is not a closing char: $char")

    fun getCompletionScore(char: Char) =
        CLOSING_CHARS_WITH_COMPLETION_SCORES[char]
            ?: throw IllegalArgumentException("Char is not a closing char: $char")
}

data class CorruptedLine(
    val line: String,
    val firstCorruptedChar: Char
)

fun findCorruptedLines(lines: List<String>): MutableList<CorruptedLine> {
    val corruptedLines = mutableListOf<CorruptedLine>()

    lines.forEach { line ->
        val stack = ArrayDeque<Char>()
        var isCorrupted = false
        var firstCorruptedChar: Char? = null

        line.forEach { char ->
            if (!isCorrupted) {
                if (OpeningChars.isOpeningChar(char))
                    stack.push(char)
                else if (ClosingChars.isClosingChar(char)) {
                    val left = stack.peek()

                    if (left == ClosingChars.getOpeningChar(char))
                        stack.pop()
                    else {
                        isCorrupted = true
                        firstCorruptedChar = char
                    }
                } else
                    throw IllegalArgumentException("Found unexpected character: $char")
            }
        }

        if (firstCorruptedChar != null) {
            corruptedLines.add(CorruptedLine(line, firstCorruptedChar!!))
        }
    }

    return corruptedLines
}

fun main(args: Array<String>) {
    val lines = parseInputFile("day-10/input.txt")
    val corruptedLines = findCorruptedLines(lines)

    val syntaxErrorScore = corruptedLines.sumOf {
        ClosingChars.getCorruptionScore(it.firstCorruptedChar)
    }

    println(syntaxErrorScore)
}
