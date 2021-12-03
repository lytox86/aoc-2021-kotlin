
fun List<String>.charactersForColumn(n: Int): Map<Char, Int> = this.groupingBy { it[n] }.eachCount()
fun String.invertBinaryString() = this.map { if (it == '0') '1' else '0' }.joinToString("")

fun List<String>.filterColumnsForCharacter(bitIndices: IntRange, desiredCharacterByFrequency: (zeroes: Int, ones: Int) -> Char): String {
    var candidateList = this
    for (column in bitIndices) {
        val charFrequencyByColumn = candidateList.charactersForColumn(column)
        val zeroes = charFrequencyByColumn['0'] ?: 0
        val ones = charFrequencyByColumn['1'] ?: 0
        candidateList = candidateList.filter { it[column] == desiredCharacterByFrequency(zeroes, ones) }
        if (candidateList.size == 1) break
    }
    return candidateList.single()
}

fun main() {
    fun part1(input: List<String>): Int {
        val bitIndices = input[0].indices
        val charFrequencyByColumn = bitIndices.map { column ->
            input.charactersForColumn(column)
        }
        val gammaRate = charFrequencyByColumn.joinToString("") { frequencies ->
            val mostFrequentChar = frequencies
                .maxByOrNull { it.value }
                ?.key
                ?: error("Should find maximum in $frequencies!")
            mostFrequentChar.toString()
        }
        val epsilonRate = gammaRate.invertBinaryString()
        return gammaRate.toInt(2) * epsilonRate.toInt(2)
    }

    fun part2(input: List<String>): Int {
        val bitIndices = input[0].indices

        val oxyGenRating = input.filterColumnsForCharacter(bitIndices) { zeroes, ones ->
            if (zeroes > ones) '0' else '1'
        }

        val co2ScrubberRating = input.filterColumnsForCharacter(bitIndices) { zeroes, ones ->
            if (zeroes > ones) '1' else '0'
        }
        return oxyGenRating.toInt(2) * co2ScrubberRating.toInt(2)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")

    println(part1(input)) //
    println(part2(input)) //
}
