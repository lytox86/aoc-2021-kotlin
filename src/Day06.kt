fun main() {
    fun solve(times: Int, initialState: LongArray): Long {
        val state = initialState.copyOf()
        repeat(times) {
            val zero = state[0]
            state.copyInto(state, 0, 1)
            state[6] += zero
            state[8] = zero
        }
        return state.sum()
    }

    fun part1(input: List<String>): Long {
        val initialState = LongArray(9).apply {
            for (fish in input.single().splitToSequence(',')) {
                this[fish.toInt()]++
            }
        }
        return solve(80, initialState)
    }

    fun part2(input: List<String>): Long {
        val initialState = LongArray(9).apply {
            for (fish in input.single().splitToSequence(',')) {
                this[fish.toInt()]++
            }
        }
        return solve(256, initialState)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input)) // 396210
    println(part2(input)) // 1770823541496
}