package battleship

import java.util.Stack
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

/*
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
*/
data class Ship(val name: String, val length: Int)

val colToNumMap =
    mutableMapOf('A' to 0, 'B' to 1, 'C' to 2, 'D' to 3, 'E' to 4, 'F' to 5, 'G' to 6, 'H' to 7, 'I' to 8, 'J' to 9)

val numToColMap =
    mutableMapOf(0 to 'A', 1 to 'B', 2 to 'C', 3 to 'D', 4 to 'E', 5 to 'F', 6 to 'G', 7 to 'H', 8 to 'I', 9 to 'J')

//val numToColMap = colToNumMap.map { entry -> Pair(entry.value, entry.key) }

fun main() {

    val player1 = Player("Player 1")
    val player2 = Player("Player 2")

    println("Player 1, place your ships on the game field")

    player2.placeShips()

    println("Press Enter and pass the move to another player")

    readln()

    println("Player 2, place your ships to the game field")

    player1.placeShips()

    println("Press Enter and pass the move to another player")

    readln()

    startGame(player1, player2)

    player2.printBattleField()
    player1.printBattleField()
}

fun startGame(player1: Player, player2: Player) {

    var finished: Boolean = false

    var activePlayer = player1
    var otherPlayer = player2
    var temp: Player? = null

    while (!finished) {
        activePlayer.printHiddenBattleField()
        println("---------------------")
        otherPlayer.printBattleField()
        println("${activePlayer.name}, it's your turn:")
        println()
        activePlayer.makeMove()
        finished = activePlayer.isGameFinished()
        if (!finished) {
            println("Press Enter and pass the move to another player")
            readln()
            temp = activePlayer
            activePlayer = otherPlayer
            otherPlayer = temp
        }
    }

    println("${activePlayer.name} sank the last ship. ${activePlayer.name} won. Congratulations!")

}

val alph = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")





