import kotlin.math.max

fun main() {
    data class Polymers(val template: String, val pairs: Map<String, Char>)

    fun parseInput(input: List<String>): Polymers {
        val template = input.first()

        val pairs = input.drop(1)
            .flatMap { line ->
                line.split("->")
                    .windowed(2, 2)
                    .map { it[0].trim() to it[1].trim()[0] }
            }
            .associate { it }

        return Polymers(template, pairs)
    }

    fun part1(input: List<String>): Int {
        val polymers = parseInput(input)
        var polymer = polymers.template

        for (i in 0 until 10) {
            val next = polymer
                .windowed(2, 1)
                .joinToString("") { sequence ->
                    "" + sequence[0] + (polymers.pairs[sequence] ?: throw Exception("Sequence not found! $sequence"))
                }
            polymer = next + polymer.last()
        }

        val sortedPolymers: List<List<Char>> = polymer.groupBy { it }.values.sortedBy { it.size }
        return sortedPolymers.last().size - sortedPolymers.first().size
    }

    fun part2(input: List<String>): Long {
        val polymers = parseInput(input)
        val polymer = polymers.template

        val letterFrequencies: MutableMap<Char, Long> = polymer
            .groupBy { it }
            .mapValues { it.value.size.toLong() }
            .toMutableMap()

        var pairFrequencies: Map<String, Long> = polymer
            .windowed(2, 1)
            .map { it[0].toString() + it[1].toString() }
            .groupBy { it }
            .mapValues { it.value.size.toLong() }

        for (i in 0 until 40) {
            val nextFrequencies = pairFrequencies.toMutableMap()

            pairFrequencies.forEach { (pair, num) ->
                val newElement = polymers.pairs[pair] ?: throw Exception("Sequence not found! $pair")
                // Add the new element to the map for each time the original pair appeared
                letterFrequencies[newElement] = letterFrequencies.getOrPut(newElement) {0} + num

                // Delete the original pair
                nextFrequencies[pair] = max(nextFrequencies.getOrPut(pair) {0} - num, 0)

                // Add the new pairs
                nextFrequencies[pair[0].toString() + newElement] = nextFrequencies.getOrPut( pair[0].toString() + newElement) {0} + num
                nextFrequencies[newElement.toString() + pair[1]] = nextFrequencies.getOrPut(newElement.toString() + pair[1]) {0} + num
            }

            pairFrequencies = nextFrequencies
        }

        val sortedPolymers = letterFrequencies.entries.sortedBy { it.value }
        return sortedPolymers.last().value - sortedPolymers.first().value
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input)) // 3259
    println(part2(input)) // 3459174981021
}