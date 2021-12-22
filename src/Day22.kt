import kotlin.math.max
import kotlin.math.min

data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {
    private fun overlappingRange(a: IntRange, b: IntRange): IntRange =
        if (a.first > b.last || a.last < b.first) 0 until 0
        else max(b.first, a.first) .. min(b.last, a.last)

    fun overlap(that: Cuboid): Cuboid = Cuboid(overlappingRange(this.x, that.x), overlappingRange(this.y, that.y), overlappingRange(this.z, that.z))
    fun volume(): Long = (x.last - x.first + 1).toLong() * (y.last - y.first + 1).toLong() * (z.last - z.first + 1).toLong()
}

typealias Rule = Pair<Boolean, Cuboid>

fun main() {
    fun parseSteps(input: List<String>): List<Rule> = input.map { l ->
        val isOn = "on" in l
        val numbers = Regex("-?[0-9]+").findAll(l).map { it.value.toInt() }.toList()
        isOn to Cuboid(numbers[0]..numbers[1], numbers[2]..numbers[3], numbers[4]..numbers[5])
    }

    fun nonOverlappingVolume(cuboid: Cuboid, rest: List<Rule>): Long {
        val conflicts = rest.filter { (_, cuboid2) -> cuboid.overlap(cuboid2).volume() > 0 }
        return conflicts.foldIndexed(cuboid.volume()) { idx, acc, (_, conflict) ->
            acc - nonOverlappingVolume(cuboid.overlap(conflict), conflicts.drop(idx+1))
        }
    }

    fun solve(input: List<String>, transform: (Cuboid) -> Cuboid = { it }): Long {
        val steps = parseSteps(input)
        return steps.foldIndexed(0L) { idx, acc, (isOn, cuboid) ->
            acc + if (!isOn) 0 else nonOverlappingVolume(transform(cuboid), steps.drop(idx + 1))
        }
    }

    fun part1(input: List<String>): Long = solve(input) { it.overlap(Cuboid(-50..50, -50..50, -50..50)) }
    fun part2(input: List<String>): Long = solve(input)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 474140L)
    check(part2(testInput) == 2758514936282235)

    val input = readInput("Day22")
    println(part1(input)) // 590784
    println(part2(input)) // 1323862415207825
}