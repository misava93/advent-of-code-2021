package com.adventofcode.days.day_13

import com.adventofcode.utils.readFileFromClasspath
import com.adventofcode.utils.splitLine
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

data class Point(
    val x: Int,
    val y: Int
)

abstract class FoldInstruction

data class FoldAlongYAxis(
    val y: Int
): FoldInstruction()

data class FoldAlongXAxis(
    val x: Int
): FoldInstruction()

data class FoldingPaper(
    val foldInstructions: List<FoldInstruction>,
    val filledPoints: Set<Point>
) {
    val paper: List<List<String>> = initPaper()

    fun foldPaper(onlyFirstInstruction: Boolean = true): FoldingPaper {
        var currentPaper = FoldingPaper(foldInstructions.toList(), filledPoints.map { it.copy() }.toSet())

        while (currentPaper.canFoldPaper()) {
            val foldInstruction = currentPaper.foldInstructions.first()

            val filledPoints = when (foldInstruction) {
                is FoldAlongYAxis -> currentPaper.foldPaperAlongYAxis(foldInstruction)
                is FoldAlongXAxis -> currentPaper.foldPaperAlongXAxis(foldInstruction)
                else -> throw IllegalStateException("Unsupported fold instruction: $foldInstruction")
            }

            currentPaper = FoldingPaper(
                foldInstructions = currentPaper.foldInstructions.drop(1),
                filledPoints = filledPoints
            )

            if (onlyFirstInstruction)
                break
        }

        return currentPaper
    }

    fun countFilledSpots(): Int = filledPoints.size

    fun print() = paper.forEach { println(it) }

    private fun initPaper(): List<List<String>> {
        val maxY = filledPoints.maxOf { it.y }
        val maxX = filledPoints.maxOf { it.x }

        return (0..maxY).map { y ->
            (0..maxX).map { x ->
                when {
                    Point(x = x, y = y) in filledPoints -> PaperMark.FILLED.char
                    else -> PaperMark.EMPTY.char
                }
            }
        }
    }

    private fun foldPaperAlongYAxis(
        foldInstruction: FoldAlongYAxis
    ): Set<Point> {
        val filledPoints = mutableSetOf<Point>()

        var rowTopIndex = 0
        var rowBottomIndex = paper.size - 1
        while(rowTopIndex < foldInstruction.y) {
            (0 until paper.first().size).forEach {
                if ((paper[rowTopIndex][it] == PaperMark.FILLED.char) ||
                    (paper[rowBottomIndex][it] == PaperMark.FILLED.char)) {
                    filledPoints.add(Point(x = it, y = rowTopIndex))
                }
            }

            rowTopIndex += 1
            rowBottomIndex -= 1
        }

        return filledPoints
    }


    private fun foldPaperAlongXAxis(foldInstruction: FoldAlongXAxis): Set<Point> {
        val filledPoints = mutableSetOf<Point>()

        paper.forEachIndexed { rowIndex, row ->
            var colLeftIndex = 0
            var colRightIndex = row.size - 1
            while(colLeftIndex < foldInstruction.x) {
                if ((paper[rowIndex][colLeftIndex] == PaperMark.FILLED.char) || (paper[rowIndex][colRightIndex] == PaperMark.FILLED.char))
                    filledPoints.add(Point(x = colLeftIndex, y = rowIndex))

                colLeftIndex += 1
                colRightIndex -= 1
            }
        }

        return filledPoints
    }

    private fun canFoldPaper() = foldInstructions.isNotEmpty()

    companion object {
        enum class PaperMark(val char: String) {
            FILLED("%"),
            EMPTY(" "),
        }
    }
}

fun transpose(matrix: List<List<String>>): List<List<String>> {
    val numRows = matrix.size
    val numCols = matrix.first().size
    val transpose = (0 until numCols).map {
        (0 until numRows).map {
            FoldingPaper.Companion.PaperMark.EMPTY.char
        }.toMutableList()
    }.toMutableList()


    (0 until numRows).forEach { i ->
        (0 until numCols).forEach { j ->
            transpose[j][i] = matrix[i][j]
        }
    }

    return transpose
}

fun parseInputFile(filepath: String): FoldingPaper {
    val points = mutableSetOf<Point>()
    val foldInstructions = mutableListOf<FoldInstruction>()

    readFileFromClasspath(filepath).readLines().forEach {
        if (it.startsWith("fold along")) {
            val foldAlong = splitLine(it, " ").last()
            val tokens = splitLine(foldAlong, "=")

            when (tokens.first()) {
                "y" -> foldInstructions.add(FoldAlongYAxis(tokens.last().toInt()))
                "x" -> foldInstructions.add(FoldAlongXAxis(tokens.last().toInt()))
                else -> throw IllegalArgumentException("Invalid input file")
            }
        }
        else {
            val tokens = splitLine(it, ",").map { it.toInt() }
            if (tokens.isNotEmpty())
                points.add(Point(x = tokens.first(), y = tokens[1]))
        }
    }

    return FoldingPaper(
        foldInstructions = foldInstructions,
        filledPoints = points
    )
}

fun main(args: Array<String>) {
    val foldingPaper = parseInputFile("day-13/input.txt")

    println(foldingPaper.foldInstructions)
    println("Part one answer: ${foldingPaper.foldPaper().countFilledSpots()}")

    val foldingPaperr = parseInputFile("day-13/input.txt")
    val partTwoPaper = foldingPaperr.foldPaper(onlyFirstInstruction = false)
    println("Part two answer: ${partTwoPaper.countFilledSpots()}")
    partTwoPaper.print()
   // E A H K R E C P

}
