package org.example

import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat

class SoccerTeamService(private val csvFilePath: String) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    init {
        FileWriter(csvFilePath, true).use { }
    }

    private fun getSoccerTeamsFromCSV(): List<SoccerTeam> {
        val teams = mutableListOf<SoccerTeam>()
        CSVReader(FileReader(csvFilePath)).use { csvReader ->
            var record: Array<String>?
            while (csvReader.readNext().also { record = it } != null) {
                val id = record!![0].toInt()
                val name = record!![1]
                val foundationDate = dateFormat.parse(record!![2])
                val netIncome = record!![3].toFloat()
                val isActive = record!![4].toBoolean()
                var playerIds = mutableListOf<Int>()
                if (record!![5]!=""){
                    playerIds = record!![5].split(";").map { it.toInt() }.toMutableList()
                }
                teams.add(SoccerTeam(id, name, foundationDate, netIncome, isActive, playerIds))
            }
        }
        return teams
    }

    private fun writeSoccerTeamsToCSV(teams: List<SoccerTeam>) {
        val csvWriter = CSVWriter(FileWriter(csvFilePath))
        val teamsData = teams.map {
            val playerIds = it.players.joinToString(";")
            arrayOf(
                it.id.toString(),
                it.name,
                dateFormat.format(it.foundationDate),
                it.netIncome.toString(),
                it.isActive.toString(),
                playerIds
            )
        }.toTypedArray()

        csvWriter.writeAll(teamsData.toList())
        csvWriter.close()
    }

    fun addSoccerTeam(team: SoccerTeam) {
        val teams = getSoccerTeamsFromCSV().toMutableList()
        teams.add(team)
        writeSoccerTeamsToCSV(teams)
    }

    fun getSoccerTeamById(id: Int): SoccerTeam? {
        return getSoccerTeamsFromCSV().find { it.id == id }
    }

    fun getAllSoccerTeams(): List<SoccerTeam> {
        return getSoccerTeamsFromCSV()
    }

    fun updateSoccerTeam(team: SoccerTeam) {
        val teams = getSoccerTeamsFromCSV().toMutableList()
        val existingTeamIndex = teams.indexOfFirst { it.id == team.id }

        if (existingTeamIndex != -1) {
            teams[existingTeamIndex] = team
            writeSoccerTeamsToCSV(teams)
        }
    }

    fun deleteSoccerTeam(id: Int) {
        val teams = getSoccerTeamsFromCSV().toMutableList()
        val teamToRemove = teams.find { it.id == id }
        teamToRemove?.let { teams.remove(it) }
        writeSoccerTeamsToCSV(teams)
    }
}