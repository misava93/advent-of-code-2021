package com.adventofcode.days.day_14

import com.adventofcode.utils.readFileFromClasspath
import com.adventofcode.utils.splitLine

data class Polymer(
    val template: String,
    val insertionRules: Map<String, Char>
) {
    fun getPolymerScore(polymer: Sequence<Char>): Int {
        val charCounts = mutableMapOf<Char, Int>()

        polymer.forEach {
            if (it in charCounts)
                charCounts[it] = charCounts[it]!! + 1
            else
                charCounts[it] = 1
        }

        val mostCommonChar = charCounts.maxOf { it.value }
        val leastCommonChar = charCounts.minOf { it.value }

        return mostCommonChar - leastCommonChar
    }

    fun applyInsertionRules(numSteps: Int) = applyInsertionRules(numSteps, template)

    private fun applyInsertionRules(numSteps: Int, polymer: String): Sequence<Char> {
        var newPolymer = polymer.asSequence()
        var currentNumSteps = numSteps

        while (currentNumSteps > 0) {
            val pairs = getPolymerPairs(newPolymer)
            newPolymer = applyRulesToPolymerPairs(pairs)
            currentNumSteps -= 1
        }

        return newPolymer
    }

    private fun getPolymerPairs(polymer: Sequence<Char>): Sequence<String> =
        polymer.windowed(2).map { it.joinToString("") }

    private fun insertCharacter(polymer: String, char: Char): String =
        StringBuilder(polymer).insert(1, char).toString()

    private fun applyRulesToPolymerPairs(
        pairs: Sequence<String>
    ): Sequence<Char> {
        val polymerParts = pairs.map { pair ->
            val charToInsert = insertionRules[pair]
            if (charToInsert != null)
                insertCharacter(pair, charToInsert)
            else
                pair
            // We need to remove last character due to overlaps between pairs
        }.map { it.dropLast(1) }

        // We need to add back the last character of the original polymer to the end
//        if (polymerParts.any())
        return (polymerParts + pairs.last().last().toString()).flatMap { it.map { it } }
//        else
//            return pairs
    }
}

fun parseInputFile(filepath: String): Polymer {
    var template = ""
    val insertionRules = mutableMapOf<String, Char>()

    readFileFromClasspath(filepath).readLines().forEachIndexed { index, line ->
        when (index) {
            0 -> template = line.trim()
            else -> {
                if (line.isNotBlank()) {
                    val tokens = splitLine(line, "->")
                    insertionRules[tokens[0]] = tokens[1].single()
                }
            }
        }
    }

    return Polymer(template, insertionRules)
}

fun main(args: Array<String>) {
    val polymer = parseInputFile("day-14/input.txt")

    var finalPolymer = polymer.applyInsertionRules(10)
    println("Part one answer: ${polymer.getPolymerScore(finalPolymer)}")

    finalPolymer = polymer.applyInsertionRules(30)
    println("Part two answer: ${polymer.getPolymerScore(finalPolymer)}")
}
