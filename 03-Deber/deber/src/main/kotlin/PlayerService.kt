package org.example

import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.text.SimpleDateFormat
import java.io.FileReader
import java.io.FileWriter
class PlayerService(private val csvFilePath: String)  {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    init {
        FileWriter(csvFilePath, true).use {}
    }

    private fun getPlayerFromCSV(): List<Player> {
        val players = mutableListOf<Player>()
        CSVReader(FileReader(csvFilePath)).use { csvReader ->
            var record: Array<String>?
            while (csvReader.readNext().also { record = it } != null) {
                players.add(
                    Player(
                        record!![0].toInt(),
                        dateFormat.parse(record!![1]),
                        record!![2].toBoolean(),
                        record!![3],
                        record!![4].toFloat()
                    )
                )
            }
        }
        return players
    }

    private fun writePlayerToCSV(players: List<Player>) {
        val csvWriter = CSVWriter(FileWriter(csvFilePath))
        val playersData =
            players
                .map {
                    arrayOf(
                        it.id.toString(),
                        dateFormat.format(it.debutDate),
                        it.isInjured.toString(),
                        it.name,
                        it.value.toString()
                    )
                }
                .toTypedArray()

        csvWriter.writeAll(playersData.toList())
        csvWriter.close()
    }

    fun addPlayer(player: Player) {
        val players = getPlayerFromCSV().toMutableList()
        players.add(player)
        writePlayerToCSV(players)
    }

    fun getPlayerById(id: Int): Player? {
        return getPlayerFromCSV().find { it.id == id }
    }

    fun getAllPlayers(): List<Player> {
        return getPlayerFromCSV()
    }

    fun updatePlayer(player: Player) {
        val players = getPlayerFromCSV().toMutableList()
        val existingPlayer = players.find { it.id == player.id }
        if (existingPlayer != null) {
            val updatedPlayer = Player(
                player.id,
                player.debutDate,
                player.isInjured ?: existingPlayer.isInjured,
                player.name ?: existingPlayer.name,
                player.value ?: existingPlayer.value
            )
            val index = players.indexOfFirst { it.id == player.id }
            if (index != -1) {
                players[index] = updatedPlayer
                writePlayerToCSV(players)
            }
        }
    }

    fun deletePlayer(id: Int) {
        val players = getPlayerFromCSV().toMutableList()
        val playerToRemove = players.find { it.id == id }
        playerToRemove?.let { players.remove(it) }
        writePlayerToCSV(players)
    }
}