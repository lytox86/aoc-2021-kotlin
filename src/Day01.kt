fun main() {
    fun part1(input: List<String>): Int {
        val intList = input.map(String::toInt)
        return intList.zipWithNext().count { (a, b) -> b > a }
    }

    fun part2(input: List<String>): Int {
        val intList = input.map(String::toInt)
        val windowsSums = intList.windowed(3) { it.sum() }
        return windowsSums.zipWithNext().count { (a, b) -> b > a }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")

    println(part1(input)) // 1759
    println(part2(input)) // 1805
}
