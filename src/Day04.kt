fun main() {
    class BingoBoard(val data: Array<IntArray>) {
        private val positionMap = mutableMapOf<Int, Pair<Int, Int>>()
        private val rowCounter = IntArray(5)
        private val colCounter = IntArray(5)
        val isWinner: Boolean
            get() = rowCounter.any { it == 5 } or colCounter.any { it == 5 }
        private var lastDrawn = -1

        init {
            for (r in data.indices) {
                for (c in data[r].indices) {
                    positionMap[data[r][c]] = Pair(r, c)
                }
            }
        }

        fun addDrawnNumber(num: Int) {
            val (row, col) = positionMap[num] ?: return
            rowCounter[row]++
            colCounter[col]++
            lastDrawn = num
        }

        fun getScore(drawnSet: Set<Int>): Int {
            if (lastDrawn == -1 || !isWinner) return -1
            val sum = data.sumOf { arr -> arr.sumOf { if (it in drawnSet) 0 else it } }
            return sum * lastDrawn
        }
    }

    fun parseInput(input: List<String>): Pair<List<Int>, List<BingoBoard>> {
        val numbersToDraw = input[0].split(',').map { it.toInt() }
        val boards = mutableListOf<BingoBoard>()
        var i = 1
        val spacesRegex = Regex("\\s+")
        while (i < input.size) {
            if (input[i].isNotEmpty()) {
                val data = Array(5) { input[i++].trim().split(spacesRegex).map { str -> str.toInt() }.toIntArray() }
                val board = BingoBoard(data)
                boards.add(board)
            } else
                i++
        }
        return Pair(numbersToDraw, boards.toList())
    }

    fun part1(input: List<String>): Int {
        val (numbersToDraw, boards) = parseInput(input)
        val drawnSet = mutableSetOf<Int>()
        for (drawnNumber in numbersToDraw) {
            drawnSet.add(drawnNumber)
            for (board in boards) {
                board.addDrawnNumber(drawnNumber)
                if (board.isWinner) {
                    return board.getScore(drawnSet)
                }
            }
        }
        return -1
    }

    fun part2(input: List<String>): Int {
        val (numbersToDraw, boards) = parseInput(input)
        val drawnSet = mutableSetOf<Int>()
        var lastScore = 0
        val won = BooleanArray(boards.size)
        for (drawnNumber in numbersToDraw) {
            drawnSet.add(drawnNumber)
            for (j in boards.indices) {
                if (won[j]) continue
                boards[j].addDrawnNumber(drawnNumber)
                if (boards[j].isWinner) {
                    lastScore = boards[j].getScore(drawnSet)
                    won[j] = true
                }
            }
        }
        return lastScore
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")

    println(part1(input)) //
    println(part2(input)) //
}
