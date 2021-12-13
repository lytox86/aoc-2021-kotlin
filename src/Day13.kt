import kotlin.math.abs

enum class FoldStrategy {
    x, y
}

fun main() {

    data class Cord(val x: Int, val y: Int)

    fun applyFold(input: List<Cord>, instruction: String): List<Cord> {
        val (foldStrategy, foldPowerStr) = instruction.split("fold along ")[1].split("=")
        val foldPower = foldPowerStr.toInt()
        return when (FoldStrategy.valueOf(foldStrategy)) {
            FoldStrategy.x -> {
                input.map {
                    Cord(
                        if (foldPower > it.x) it.x else foldPower - abs(it.x - foldPower) ,// fold left
                        it.y
                    )
                }
            }

            FoldStrategy.y -> {
                input.map {
                    Cord(
                        it.x,
                        if (foldPower > it.y) it.y else foldPower - abs(it.y - foldPower)  // fold up
                    )
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val emptyLineIndex = input.indexOf("")
        val dots = input.take(emptyLineIndex).map {
            val (x, y) = it.split(",")
            Cord(x.toInt(), y.toInt())
        }
        val instructions = input.takeLast(input.size - emptyLineIndex - 1)

        return applyFold(dots, instructions[0]).toSet().size
    }


    fun part2(input: List<String>) {
        val dots = input.take(input.indexOf("")).map {
            val (x, y) = it.split(",")
            Cord(x.toInt(), y.toInt())
        }
        val instructions = input.takeLast(input.size - input.indexOf("") - 1)
        val mappedDots = instructions.fold(dots) { acc, instruction -> applyFold(acc, instruction) }.toSet()
        for (i in (mappedDots.minByOrNull { it.y }?.y ?: 0)..(mappedDots.maxByOrNull { it.y }?.y ?: 1000)) {
            for (j in (mappedDots.minByOrNull { it.x }?.x ?: 0) .. (mappedDots.maxByOrNull { it.x }?.x ?: 1000)) {
                print(if (mappedDots.contains(Cord(j, i))) "#" else " ")
            }
            println()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    //check(part1(testInput) == 17)
    println(part2(testInput))
//    #####
//    #...#
//    #...#
//    #...#
//    #####
    val input = readInput("Day13")
    println(part1(input)) // 638
    println(part2(input)) //

//    .##....##..##..#..#.###...##..###..###.
//    #..#....#.#..#.#.#..#..#.#..#.#..#.#..#
//    #.......#.#....##...###..#..#.#..#.###.
//    #.......#.#....#.#..#..#.####.###..#..#
//    #..#.#..#.#..#.#.#..#..#.#..#.#....#..#
//    .##...##...##..#..#.###..#..#.#....###.
    // CJCKBAPB
}