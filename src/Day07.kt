import kotlin.math.abs
import kotlin.math.roundToLong

fun main() {
    fun t(x: Long): Long = x * (x + 1) / 2

    fun part1(input: List<String>): Long {
        val nums = input.flatMap { line -> line.splitToSequence(',').map { it.toLong() } }
        val median = nums.sorted()[nums.size / 2]
        return nums.sumOf { abs(it - median) }
    }

    fun part2(input: List<String>): Long {
        val nums = input.flatMap { line -> line.splitToSequence(',').map { it.toLong() } }
        val mean = nums.sum().toDouble() / nums.size
        val mean0 = mean.roundToLong()
        val mean1 = if (mean0 <= mean) mean0 + 1 else mean0 - 1
        return minOf(nums.sumOf { t(abs(it - mean0)) }, nums.sumOf { t(abs(it - mean1)) })
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37L)
    check(part2(testInput) == 168L)

    val input = readInput("Day07")
    println(part1(input)) // 331067
    println(part2(input)) // 92881128
}