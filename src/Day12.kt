fun main() {
    fun findPaths(caves: Map<String, List<String>>, visited: List<String>, current:String) : Set<List<String>> {
        val paths = caves[current]!!.map { connection ->
            if (connection == "end") {
                setOf(visited+"end")
            } else if (connection.uppercase() == connection || !visited.contains(connection)) {
                findPaths(caves, visited + connection, connection)
            } else {
                setOf()
            }
        }.flatten()

        return paths.toSet()
    }
    fun findPaths2(caves: Map<String, List<String>>, visited: List<String>, current:String, revisited:String?) : Set<List<String>> {
        val paths = caves[current]!!.map { connection ->
            if (connection == "end") {
                setOf(visited + "end")
            } else if (connection == "start") {
                setOf()
            } else if (connection.uppercase() == connection) {
                findPaths2(caves, visited + connection, connection, revisited)
            } else if (visited.contains(connection) && revisited.isNullOrBlank()) {
                findPaths2(caves, visited + connection, connection, connection)
            } else if (!visited.contains(connection)) {
                findPaths2(caves, visited + connection, connection, revisited)
            } else {
                setOf()
            }
        }.flatten()

        return paths.toSet()
    }
    fun part1(input: List<String>): Int {
        val connections = input.map { it.split("-") }.map { it[0] to it[1] }
        val caves = (connections + connections.map { it.second to it.first}).groupBy({it.first}, {it.second})
        val paths = findPaths(caves, listOf("start"), "start")
        return paths.size
    }
    fun part2(input: List<String>): Int {
        val connections = input.map { it.split("-") }.map { it[0] to it[1] }
        val caves = (connections + connections.map { it.second to it.first}).groupBy({it.first}, {it.second})
        val paths = findPaths2(caves, listOf("start"), "start", null)
        return paths.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input)) // 4186
    println(part2(input)) // 92111
}