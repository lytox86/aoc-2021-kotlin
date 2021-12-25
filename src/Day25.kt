fun main() {

    fun part1(input: List<String>): Int {
        val data = input.map { it.toList() }
        val width = data[0].size
        val height = data.size
        val size = width * height

        fun Int.east() = this - (this % width) + ((this % width) + 1) % width
        fun Int.south() = (this + width) % size

        fun solve(): Int {
            val field = CharArray(size) { i -> data[i / width][i % width] }
            var moves = 0

            do {
                val east = field.withIndex().filter { (i, c) -> c == '>' && field[i.east()] == '.' }
                east.forEach { (i, c) ->
                    field[i] = '.'
                    field[i.east()] = c
                }

                val south = field.withIndex().filter { (i, c) -> c == 'v' && field[i.south()] == '.' }
                south.forEach { (i, c) ->
                    field[i] = '.'
                    field[i.south()] = c
                }

                moves++
            } while (east.isNotEmpty() || south.isNotEmpty())

            return moves
        }
        return solve()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == 58)

    val input = readInput("Day25")
    println(part1(input)) // 579
}