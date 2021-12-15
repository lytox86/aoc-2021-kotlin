
fun main() {
    data class Coord(val riskValue: Int, var minRiskToReach: Int)


    fun calculateEndRiskValues(grid: List<List<Coord>>, startingPos: Pair<Int, Int>, endPoint: Pair<Int, Int>) {
        val pointsToCheck = mutableListOf(startingPos)
        grid[0][0].minRiskToReach = 0
        while (pointsToCheck.any { it != endPoint }) {
            val currentPos = pointsToCheck.removeAt(0)
            val currentPosRiskToReach = grid[currentPos.first][currentPos.second].minRiskToReach
            if (currentPos.first > 0) {
                val newCoord = grid[currentPos.first - 1][currentPos.second]
                if (newCoord.riskValue + currentPosRiskToReach < newCoord.minRiskToReach) {
                    newCoord.minRiskToReach = newCoord.riskValue + currentPosRiskToReach
                    pointsToCheck.add(Pair(currentPos.first - 1, currentPos.second))
                }
            }
            if (currentPos.first < grid.size - 1) {
                val newCoord = grid[currentPos.first + 1][currentPos.second]
                if (newCoord.riskValue + currentPosRiskToReach < newCoord.minRiskToReach) {
                    newCoord.minRiskToReach = newCoord.riskValue + currentPosRiskToReach
                    pointsToCheck.add(Pair(currentPos.first + 1, currentPos.second))
                }
            }
            if (currentPos.second > 0) {
                val newCoord = grid[currentPos.first][currentPos.second - 1]
                if (newCoord.riskValue + currentPosRiskToReach < newCoord.minRiskToReach) {
                    newCoord.minRiskToReach = newCoord.riskValue + currentPosRiskToReach
                    pointsToCheck.add(Pair(currentPos.first, currentPos.second - 1))
                }
            }
            if (currentPos.second < grid.size - 1) {
                val newCoord = grid[currentPos.first][currentPos.second + 1]
                if (newCoord.riskValue + currentPosRiskToReach < newCoord.minRiskToReach) {
                    newCoord.minRiskToReach = newCoord.riskValue + currentPosRiskToReach
                    pointsToCheck.add(Pair(currentPos.first, currentPos.second + 1))
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val grid = input
            .map { line -> line.toCharArray().map { el -> Coord(Character.getNumericValue(el), Int.MAX_VALUE) }.toList() }
        val startingPos = Pair(0,0)
        val endPoint = Pair(grid.size-1, grid.size-1)
        calculateEndRiskValues(grid, startingPos, endPoint)
        return grid[grid.size - 1][grid.size - 1].minRiskToReach
    }

    fun part2(input: List<String>): Int {
        val readGrid = input
            .map { line -> line.toCharArray().map { el -> Character.getNumericValue(el) }.toMutableList() }.toMutableList()
        val megaGrid = mutableListOf<MutableList<Coord>>()
        for (xStep in 0..4) {
            for (x in 0 until readGrid.size) {
                val list = mutableListOf<Coord>()
                for (yStep in -1..3) {
                    for (y in 0 until readGrid.size) {
                        list.add(Coord(((readGrid[x][y] + (yStep + xStep) ) % 9 + 1).coerceAtLeast(1), Int.MAX_VALUE))
                    }
                }
                megaGrid.add(list)
            }
        }

        val startingPos = Pair(0,0)
        val endPoint = Pair(megaGrid.size-1, megaGrid.size-1)
        calculateEndRiskValues(megaGrid, startingPos, endPoint)
        return megaGrid[megaGrid.size - 1][megaGrid.size - 1].minRiskToReach
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input)) // 714
    println(part2(input)) // 2948
}