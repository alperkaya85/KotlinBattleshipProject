package battleship

import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Player(val name: String) {

    val battleFiled = mutableListOf<MutableList<Char>>(
        mutableListOf('~', '~', '~', '~', '~', '~', '~', '~', '~', '~'),
        mutableListOf('~', '~', '~', '~', '~', '~', '~', '~', '~', '~'),
        mutableListOf('~', '~', '~', '~', '~', '~', '~', '~', '~', '~'),
        mutableListOf('~', '~', '~', '~', '~', '~', '~', '~', '~', '~'),
        mutableListOf('~', '~', '~', '~', '~', '~', '~', '~', '~', '~'),
        mutableListOf('~', '~', '~', '~', '~', '~', '~', '~', '~', '~'),
        mutableListOf('~', '~', '~', '~', '~', '~', '~', '~', '~', '~'),
        mutableListOf('~', '~', '~', '~', '~', '~', '~', '~', '~', '~'),
        mutableListOf('~', '~', '~', '~', '~', '~', '~', '~', '~', '~'),
        mutableListOf('~', '~', '~', '~', '~', '~', '~', '~', '~', '~')
    )

    val shipsCoordinates = mutableListOf<MutableMap<String, Char>>()

    val unsankShipIndexList = mutableListOf<Int>(0, 1, 2, 3, 4)

    var sankShipIndex: Int = -1

    fun placeShips() {
        val ships = Stack<Ship>()
        ships.push(Ship("Destroyer", 2))
        ships.push(Ship("Cruiser", 3))
        ships.push(Ship("Submarine", 3))
        ships.push(Ship("Battleship", 4))
        ships.push(Ship("Aircraft Carrier ", 5))

        printBattleField()

        var ship = ships.pop()
        var inp: List<String>
        var valid: Boolean
        while (ship != null) {

            println("Enter the coordinates of the ${ship.name} (${ship.length} cells):\n")
            try {
                inp = readln().split(" ")
            } catch (e: Exception) {
                println("\nERROR: ${e.message}")
                continue
            }

            if (inp[0].first().isLetter() && !colToNumMap.containsKey(inp[0].first()) || inp[1].first()
                    .isLetter() && !colToNumMap.containsKey(inp[1].first())
            ) {
                println("Error! Invalid row")
                continue
            }

            if (inp[0].first() == inp[1].first()) {
                val col1 = inp[0].substring(1).toInt() - 1
                val col2 = inp[1].substring(1).toInt() - 1

                val length = abs(col2 - col1) + 1

                if (length != ship.length) {
                    println("Error! Wrong length of the ${ship.name}! Try again:")
                    continue
                }

                if (!shipExistsNearbyValid(
                        colToNumMap[inp[0].first()]!!,
                        Pair(min(col1, col2), max(col1, col2))
                    )
                ) {
                    println("Error! You placed it too close to another one. Try again:")
                    continue
                }

                for (i in min(col1, col2)..max(col1, col2)) {
                    battleFiled[colToNumMap[inp[0].first()]!!][i] = 'O'
                }

                //Add ship to ship map list
                val shipMap: MutableMap<String, Char> = mutableMapOf()

                for (i in min(col1, col2)..max(col1, col2)) {
                    shipMap[inp[0].first().toString() + i] = '0'
                }

                shipsCoordinates.add(shipMap)

            } else if (inp[0].substring(1) == inp[1].substring(1)) {
                val row1 = colToNumMap[inp[0].first()]!!
                val row2 = colToNumMap[inp[1].first()]!!

                val column = inp[0].substring(1).toInt() - 1

                val length = abs(row2 - row1) + 1

                if (length != ship.length) {
                    println("Error! Wrong length of the ${ship.name}! Try again:")
                    continue
                }

                if (!shipExistsNearbyValid(
                        Pair(min(row1, row2), max(row1, row2)),
                        column
                    )
                ) {
                    println("Error! You placed it too close to another one. Try again:")
                    continue
                }

                for (i in min(row1, row2)..max(row1, row2)) {
                    battleFiled[i][column] = 'O'
                }

                //Add ship to ship map list
                val shipMap: MutableMap<String, Char> = mutableMapOf()

                for (i in min(row1, row2)..max(row1, row2)) {
                    shipMap[numToColMap[i].toString() + column] = '0'
                }

                shipsCoordinates.add(shipMap)
            } else {
                println("Error! Wrong length of the ${ship.name}! Try again:")
                continue
            }

            printBattleField()

            if (ships.isNotEmpty()) {
                ship = ships.pop()
            } else {
                ship = null
            }
        }
    }

    fun makeMove() {

        var inp = readln()
        var valid = false

        while (!valid) {
            if (inp.first().isLetter() && !colToNumMap.containsKey(inp.first()) || inp.substring(1).toInt() > 10
            ) {
                println("Error! You entered wrong coordinates! Try again:")
                inp = readln()
                continue
            } else {
                valid = true
            }
        }


        val row = colToNumMap[inp.first()]!!
        val col = inp.substring(1).toInt() - 1

        if (battleFiled[row][col] == 'O') {
            battleFiled[row][col] = 'X'
            printHiddenBattleField()
            //if (!isGameFinished()) {

            signShipStatus("${inp.first()}$col")

            if (isShipSank()) {
                println("You sank a ship! Specify a new target:")
                unsankShipIndexList.removeAt(sankShipIndex)
            } else {
                println("You hit a ship!")
            }

            /*} else {
                println("You sank the last ship. You won. Congratulations!")
            }*/


        } else {
            if (battleFiled[row][col] != 'X') {
                battleFiled[row][col] = 'M'
            }
            printHiddenBattleField()
            println("You missed!")

        }


    }

    private fun signShipStatus(key: String) {
        for (i in shipsCoordinates.indices) {
            if (shipsCoordinates[i].containsKey(key)) {
                shipsCoordinates[i][key] = 'X'
            }
        }
    }

    fun printBattleField() {
        println("  1 2 3 4 5 6 7 8 9 10")
        for (row in battleFiled.indices) {
            print("${alph[row]} ")
            for (column in battleFiled[row].indices) {
                print("${battleFiled[row][column]} ")
            }
            println()
        }
    }

    fun printHiddenBattleField() {
        println("  1 2 3 4 5 6 7 8 9 10")
        for (row in battleFiled.indices) {
            print("${alph[row]} ")
            for (column in battleFiled[row].indices) {
                print(if (battleFiled[row][column] == 'M' || battleFiled[row][column] == 'X') "${battleFiled[row][column]} " else "~ ")
            }
            println()
        }
    }

    fun shipExistsNearbyValid(row: Int, p1: Pair<Int, Int>): Boolean {
        var valid: Boolean

        for (c in p1.first..p1.second) {
            if (row == 0) {
                if (c == 0) {
                    valid = control(1, 0)
                } else if (c == p1.first) {
                    valid = control(1, c) && control(0, c - 1)
                } else if (c == p1.second && c != battleFiled[row].size - 1) {
                    valid = control(1, c) && control(0, c + 1)
                } else {
                    valid = control(0, c + 1)
                }
            } else if (row == battleFiled.size - 1) {
                if (c == 0) {
                    valid = control(8, 0)
                } else if (c == p1.first) {
                    valid = control(8, c) && control(9, c - 1)
                } else if (c == p1.second && c != battleFiled[row].size - 1) {
                    valid = control(8, c) && control(9, c + 1)
                } else {
                    valid = control(8, c)
                }
            } else {
                if (c == 0) {
                    valid = control(row - 1, 0) && control(row + 1, 0)
                } else if (c == p1.first) {
                    valid = control(row - 1, c) &&
                            control(row + 1, c) &&
                            control(row, c - 1)
                } else if (c == p1.second && c != battleFiled[row].size - 1) {
                    valid = control(row - 1, c) &&
                            control(row + 1, c) &&
                            control(row, c + 1)
                } else {
                    valid = control(row - 1, c) &&
                            control(row + 1, c)
                }
            }
            if (!valid) {
                return false
            }
        }

        return true
    }

    fun shipExistsNearbyValid(p: Pair<Int, Int>, col: Int): Boolean {
        var valid: Boolean

        for (r in p.first..p.second) {
            if (col == 0) {
                if (r == 0) {
                    valid = control(0, col + 1)
                } else if (r == p.first) {
                    valid = control(r - 1, 0) && control(r, col + 1)
                } else if (r == p.second && r != battleFiled.size - 1) {
                    valid = control(r + 1, 0) && control(r, col + 1)
                } else {
                    valid = control(r, col + 1)
                }
            } else if (col == battleFiled.size - 1) {
                if (r == 0) {
                    valid = control(0, col - 1)
                } else if (r == p.first) {
                    valid = control(r, 8) && control(r - 1, 9)
                } else if (r == p.second && r != battleFiled.size - 1) {
                    valid = control(r, 8) && control(r + 1, 9)
                } else {
                    valid = control(r, 8)
                }
            } else {
                if (r == 0) {
                    valid = control(0, col - 1) && control(0, col + 1)
                } else if (r == p.first) {
                    valid = control(r, col - 1) &&
                            control(r, col + 1) &&
                            control(r - 1, col)
                } else if (r == p.second && r != battleFiled.size - 1) {
                    valid = control(r, col - 1) &&
                            control(r, col + 1) &&
                            control(r + 1, col)
                } else {
                    valid = control(r, col - 1) &&
                            control(r, col + 1)
                }
            }
            if (!valid) {
                return false
            }
        }

        return true
    }

    fun control(x: Int, y: Int): Boolean {
        if (battleFiled[x][y] == 'O') {
            return false
        }

        return true
    }

    fun isShipSank(): Boolean {
        var sank: Boolean = false

        for (i in unsankShipIndexList.indices) {
            if (shipsCoordinates[i].values.all { it == 'X' }) {
                sank = true
                sankShipIndex = i
            }
        }

        return sank
    }

    fun isGameFinished(): Boolean {
        var count: Int = 0

        for (i in battleFiled.indices) {
            count += battleFiled[i].count { it == 'X' }
        }

        return count == 17
    }
}