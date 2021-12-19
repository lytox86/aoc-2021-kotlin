import java.io.File
import kotlin.math.abs

data class Scanner(
    val id: Int,
    val scannedBeacons: Set<Coordinate>
){
    companion object{
        fun of(input: String) : Scanner {
            val lines = input.lines()
            val id = lines[0].filter { it.isDigit() }.toInt()
            val scannedBeacons = lines.drop(1).map(Coordinate::of).toSet()
            return Scanner(id, scannedBeacons)
        }
    }

    val orientations: List<Set<Coordinate>> = listOf<(Coordinate) -> Coordinate>(
        { (x,y,z) -> Coordinate(x,y,z)      },
        { (x,y,z) -> Coordinate(x,z,-y)     },
        { (x,y,z) -> Coordinate(x,-y,-z)    },
        { (x,y,z) -> Coordinate(x,-z,y)     },
        { (x,y,z) -> Coordinate(y,-x,z)     },
        { (x,y,z) -> Coordinate(y,z,x)      },
        { (x,y,z) -> Coordinate(y,x,-z)     },
        { (x,y,z) -> Coordinate(y,-z,-x)    },
        { (x,y,z) -> Coordinate(-x,-y,z)    },
        { (x,y,z) -> Coordinate(-x,-z,-y)   },
        { (x,y,z) -> Coordinate(-x,y,-z)    },
        { (x,y,z) -> Coordinate(-x,z,y)     },
        { (x,y,z) -> Coordinate(-y,x,z)     },
        { (x,y,z) -> Coordinate(-y,-z,x)    },
        { (x,y,z) -> Coordinate(-y,-x,-z)   },
        { (x,y,z) -> Coordinate(-y,z,-x)    },
        { (x,y,z) -> Coordinate(z,y,-x)     },
        { (x,y,z) -> Coordinate(z,x,y)      },
        { (x,y,z) -> Coordinate(z,-y,x)     },
        { (x,y,z) -> Coordinate(z,-x,-y)    },
        { (x,y,z) -> Coordinate(-z,-y,-x)   },
        { (x,y,z) -> Coordinate(-z,-x,y)    },
        { (x,y,z) -> Coordinate(-z,y,x)     },
        { (x,y,z) -> Coordinate(-z,x,-y)    },
    ).map { scannedBeacons.map(it).toSet() }
}

data class Coordinate(val x: Int, val y: Int, val z: Int){
    companion object{
        fun of(input: String) = input.split(",").map(String::toInt).let{ (x,y,z) -> Coordinate(x,y,z) }
    }
    operator fun minus(o: Coordinate) = Coordinate(x - o.x, y - o.y, z - o.z)
    operator fun plus(o: Coordinate) = Coordinate(x + o.x, y + o.y, z + o.z)
}

const val MINIMALBEACONS = 12


fun main() {

    fun findOrientationThatFits(scanner: Scanner, totalBeacons: Set<Coordinate>) : Pair<Set<Coordinate>, Coordinate>? {
        return scanner.orientations.firstNotNullOfOrNull { orient ->
            totalBeacons.flatMap { c1 -> orient.map { c2 -> c1 - c2 } }
                .groupingBy { it }
                .eachCount()
                .filterValues { it >= MINIMALBEACONS }
                .keys
                .singleOrNull()
                ?.let { orient to it }
        }
    }

    fun solve(
        scannersToCheck: Set<Scanner>,  //set of scanners that still haven't found a correct overlap
        offsetScanners: Set<Coordinate>,//set of offsets that contain the relative position to scanner 0
        totalBeacons: Set<Coordinate>   //set of beacons that are guaranteed found relative to scanner 0
    ) : Pair<Int, Int>? {
        if(scannersToCheck.isEmpty())
            return totalBeacons.size to offsetScanners.flatMap { a -> offsetScanners.map { x -> a - x } }.maxOf { (a,b,c) -> abs(a) + abs(b) + abs(c) }
        val scannerOrientationsThatFit = scannersToCheck
            .mapNotNull{ scanner -> findOrientationThatFits(scanner, totalBeacons)?.let { scanner to it } }
            .toMap()
        if(scannerOrientationsThatFit.isNotEmpty()){
            val newScannersToCheck = scannersToCheck - scannerOrientationsThatFit.keys
            val newOffsetScanners = offsetScanners.toMutableSet()
            val beacons = totalBeacons.toMutableSet()
            for((orientation, offset) in scannerOrientationsThatFit.values){
                for(orient in orientation)
                    beacons += orient + offset
                newOffsetScanners += offset
            }
            return solve(newScannersToCheck, newOffsetScanners, beacons)
        }
        return null
    }

    fun part1(input: String): Int {
        val parsed = input.split("\n\n").map(Scanner::of)

        val result = solve(parsed.drop(1).toSet(), emptySet(), parsed[0].scannedBeacons)

        return result?.first ?: 0
    }

    fun part2(input: String): Int {
        val parsed = input.split("\n\n").map(Scanner::of)

        val result = solve(parsed.drop(1).toSet(), emptySet(), parsed[0].scannedBeacons)

        return result?.second ?: 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = File("src", "Day19_test.txt").readText()
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)

    val input = File("src", "Day19.txt").readText()
    println(part1(input)) // 332
    println(part2(input)) // 8507
}
