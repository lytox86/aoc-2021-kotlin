fun main() {
    fun enhanceImage(input: List<String>, iterations: Int): Int {
        val algorithm = input[0]
        val image = input.subList(2, input.size)
        var pixels = mutableSetOf<Pair<Int, Int>>()
        image.forEachIndexed { y, row ->
            row.forEachIndexed { x, ch ->
                if (ch == '#') pixels.add(x to y)
            }
        }
        repeat(iterations) { iteration ->
            val xMin = pixels.minOf { (x, _) -> x }
            val yMin = pixels.minOf { (_, y) -> y }
            val xMax = pixels.maxOf { (x, _) -> x }
            val yMax = pixels.maxOf { (_, y) -> y }
            val newPixels = mutableSetOf<Pair<Int, Int>>()
            ((yMin-2)..(yMax+2)).forEach { y ->
                ((xMin-2)..(xMax+2)).forEach { x ->
                    var num = ""
                    listOf(
                        -1 to -1, 0 to -1, 1 to -1,
                        -1 to 0, 0 to 0, 1 to 0,
                        -1 to 1, 0 to 1, 1 to 1
                    ).forEach { (i, j) ->
                        val xx = x + i
                        val yy = y + j
                        val isInMap = pixels.contains(xx to yy)
                        val outOfBounds = !(xMin..xMax).contains(xx) || !(yMin..yMax).contains(yy)
                        num += if (isInMap || (algorithm[0] == '#' && iteration % 2 == 1 && outOfBounds)) "1" else "0"
                    }
                    val newValue = algorithm[num.toInt(2)]
                    if (newValue == '#') {
                        newPixels.add(x to y)
                    }
                }
            }
            pixels = newPixels
        }
        return pixels.size
    }

    fun part1(input: List<String>): Int {
        return enhanceImage(input, 2)
    }

    fun part2(input: List<String>): Int {
        return enhanceImage(input, 50)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input)) // 5081
    println(part2(input)) // 15088
}
