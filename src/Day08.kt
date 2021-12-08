
fun main() {

    fun parseInput(input: List<String>): List<Pair<List<String>, List<String>>> {
        return input.map {
            val (t1, t2) = it.split(" | ")
            val list1 = t1.split(' ')
            val list2 = t2.split(' ')
            Pair(list1, list2)
        }
    }

    fun allMatch(first: String, charsToMatch: String): Boolean =
        charsToMatch == charsToMatch.filter { first.contains(it) }

    fun decode(digits: List<String>, map: Map<Set<Char>, String>): Int =
        digits.map { map[it.toSet()] }.joinToString(separator = "").toInt()

    fun decodeMap(signals: List<String>): Map<Set<Char>, String> {
        val one = signals.find { it.length == 2 } ?: "?"
        val four = signals.find { it.length == 4 } ?: "?"
        val seven = signals.find { it.length == 3 } ?: "?"
        val eight = signals.find { it.length == 7 } ?: "?"
        val nine = signals.find {
            it.length == 6 &&
                    allMatch(it, one) &&
                    allMatch(it, four) &&
                    allMatch(it, seven)
        } ?: "?"
        val zero = signals.find {
            it.length == 6 &&
                    allMatch(it, one) &&
                    !allMatch(it, four) &&
                    allMatch(it, seven)
        } ?: "?"
        val six = signals.find {
            it.length == 6 &&
                    !allMatch(it, one) &&
                    !allMatch(it, four) &&
                    !allMatch(it, seven)
        } ?: "?"

        val three = signals.find {
            it.length == 5 &&
                    allMatch(it, one) &&
                    !allMatch(it, four) &&
                    allMatch(it, seven)
        } ?: "?"

        val five = signals.find {
            it.length == 5 &&
                    !allMatch(it, one) &&
                    !allMatch(it, four) &&
                    !allMatch(it, seven) &&
                    allMatch(nine, it)
        } ?: "?"

        val two = signals.find {
            it.length == 5 &&
                    it != three &&
                    it != five
        } ?: "?"

        return mapOf(
            zero.toSet() to "0",
            one.toSet() to "1",
            two.toSet() to "2",
            three.toSet() to "3",
            four.toSet() to "4",
            five.toSet() to "5",
            six.toSet() to "6",
            seven.toSet() to "7",
            eight.toSet() to "8",
            nine.toSet() to "9"
        )
    }

    fun part1(input: List<String>): Int {
        val data = parseInput(input)

        val uniqueCounts = setOf(2, 4, 3, 7)

        return data.flatMap { it.second }.map{it.length}.count{uniqueCounts.contains(it)}
    }



    fun part2(input: List<String>): Int {
        val data = parseInput(input)
        val signals =data.map{it.first}
        val digits = data.map{it.second}
        val maps = signals.map { decodeMap(it) }
        val values = maps.zip(digits) { map, dig -> decode(dig, map) }
        return values.sum()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26) // 26
    check(part2(testInput) == 61229)  // 61229

    val input = readInput("Day08")
    println(part1(input)) // 548
    println(part2(input)) // 1074888
}