package com.adventofcode.days.four

import com.adventofcode.days.three.BinaryNumber
import java.io.File
import java.util.UUID

data class BingoBoardEntry(
    val number: Int,
    var marked: Boolean = false
) {
    override fun toString(): String {
        return "$number"
    }
}

data class BingoBoard(
    val id: UUID = UUID.randomUUID(),
    val rows: List<List<BingoBoardEntry>>,
    val cols: List<List<BingoBoardEntry>>
) {
    override fun toString(): String {
        var rowsString = ""
        var colsString = ""
        rows.forEach { rowsString += "$it\n" }
        cols.forEach { colsString += "$it\n" }

        return "Rows=\n$rowsString\nCols=\n$colsString\n\n"
    }

    fun isWinningBoard(rowIndex: Int, colIndex: Int): Boolean {
        val isWinningRow = rows[rowIndex].filterNot { it.marked }.isEmpty()
        val isWinningCol = cols[colIndex].filterNot { it.marked }.isEmpty()

        return isWinningRow || isWinningCol
    }

    fun markNumberDrawnIfPresent(numberDrawn: Int): Boolean {
        var matchRowIndex: Int? = null
        var matchColIndex: Int? = null

        rows.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, bingoBoardEntry ->
                if (bingoBoardEntry.number == numberDrawn) {
                    bingoBoardEntry.marked = true
                    matchRowIndex = rowIndex
                    matchColIndex = colIndex
                }
            }
        }

        if (matchRowIndex != null && matchColIndex != null ) {
            cols[matchColIndex!!][matchRowIndex!!].marked = true

            return isWinningBoard(matchRowIndex!!, matchColIndex!!)
        }

        return false
    }

    fun getBoardScore()= rows.sumOf { row ->
        row.filterNot { it.marked }.sumOf { it.number }
    }
}

fun createBingoBoard(bingoBoardRows: List<List<BingoBoardEntry>>): BingoBoard {
    var bingoBoardCols = mutableListOf<List<BingoBoardEntry>>()

    val numRows = bingoBoardRows.size
    val numCols = bingoBoardRows[0].size
    (0 until numCols).forEach { colIndex ->
        val column = (0 until numRows).map { rowIndex ->
            bingoBoardRows[rowIndex][colIndex]
        }
        bingoBoardCols.add(column)
    }

    return BingoBoard(
        rows = bingoBoardRows,
        cols = bingoBoardCols
    )
}

fun parseInputFile(filepath: String): Pair<List<Int>, List<BingoBoard>> {
    var numbersDrawn = listOf<Int>()
    var bingoBoards = mutableListOf<BingoBoard>()

    var lineCounter = 0
    var currentBingoBoardRows = mutableListOf<List<BingoBoardEntry>>()

    File(filepath).forEachLine {
        if (lineCounter == 0)
            numbersDrawn = it.trim().split(",").map { it.toInt() }
        else {
            // blank lines indicate new bingo board begins
            if (it.isBlank()) {
                if (lineCounter != 1) {
                    bingoBoards.add(createBingoBoard(currentBingoBoardRows))
                    currentBingoBoardRows = mutableListOf()
                }
            }
            else {
                currentBingoBoardRows.add(it.trim().split(" ")
                    .filter { it.isNotBlank() }
                    .map { BingoBoardEntry(it.toInt()) })
            }
        }

        lineCounter += 1
    }

    // add last bingo board
    bingoBoards.add(createBingoBoard(currentBingoBoardRows))

    return Pair(numbersDrawn, bingoBoards)
}


fun main(args: Array<String>) {
    val (numbersDrawn, bingoBoards) = parseInputFile("/Users/misava/workplace/advent-of-code/src/main/kotlin/com/adventofcode/days/four/input.txt")

    val numberOfBoards = bingoBoards.size
    var numWinningBoards = 0
    val winningBoards = mutableSetOf<UUID>()

    numbersDrawn.forEach { numberDrawn ->
        bingoBoards.forEach { board ->
            val isWinningBoard = board.markNumberDrawnIfPresent(numberDrawn)

            if (isWinningBoard) {
                if (board.id !in winningBoards) {
                    winningBoards.add(board.id)
                    numWinningBoards += 1

                    if (numWinningBoards >= numberOfBoards) {
                        println(board.getBoardScore() * numberDrawn)
                        return
                    }
                }
            }
        }
    }
}
