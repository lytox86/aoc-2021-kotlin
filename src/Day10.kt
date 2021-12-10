fun main() {
    val openChars = listOf('<', '(', '{', '[')
    val closeChars = listOf('>', ')', '}', ']')
    val corruptedScores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )
    val missingScores = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    fun getChunkClosingCharFor(openChar: Char): Char {
        return closeChars[openChars.indexOf(openChar)]
    }

    fun getCorruptedChunkScore(line: String): Int {
        val chunkStack = ArrayDeque<Char>()
        line.toCharArray().forEach { char ->
            when (char) {
                in openChars ->
                    chunkStack.addFirst(getChunkClosingCharFor(char))
                in closeChars -> {
                    if (chunkStack.removeFirst() != char) {
                        return corruptedScores[char] ?: 0
                    }
                }
            }
        }
        return 0
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { getCorruptedChunkScore(it) }.toLong()
    }

    fun getIncompleteScore(line: String): Long {
        val chunkStack = ArrayDeque<Char>()
        line.toCharArray().forEach {
            when (it) {
                in openChars ->
                    chunkStack.addFirst(getChunkClosingCharFor(it))
                in closeChars -> {
                    if (chunkStack.removeFirst() != it) {
                        return -1
                    }
                }
            }
        }

        var score = 0L
        while (chunkStack.isNotEmpty()) {
            score *= 5
            score += missingScores[chunkStack.removeFirst()] ?: 0
        }
        return score
    }

    fun part2(input: List<String>): Long {
        val scores = input.map { getIncompleteScore(it) }.filter { it != -1L }.sorted()
        return scores[scores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397L)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input)) // 392421
    println(part2(input)) // 2769449099
}