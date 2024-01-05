package org.example

import java.text.SimpleDateFormat
import java.util.Date

fun main() {
    var exit = false
    while (!exit) {
        println("\nGestión de Fútbol")
        println("1. Usar Gestión de Equipos")
        println("2. Usar Gestión de Jugadores")
        println("3. Salir")
        print("Ingrese su elección: ")

        when (readLine()?.toIntOrNull() ?: 3) {
            1 -> {
                teamCRUD()
            }

            2 -> {
                playerCRUD()
            }

            3 -> {
                println("\n¡Hasta pronto!")
                exit = true
            }

            else -> println("\nOpción inválida, por favor intente de nuevo.")
        }
    }
}

fun teamCRUD() {
    var exit = false
    val teamService = SoccerTeamService("teams.csv")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val playerService = PlayerService("players.csv")

    while (!exit) {
        println("\nEquipos de Fútbol")
        println("1. Agregar un Equipo de Fútbol")
        println("2. Encontrar un Equipo de Fútbol por ID")
        println("3. Mostrar Todos los Equipos de Fútbol")
        println("4. Actualizar un Equipo de Fútbol")
        println("5. Agregar Jugador a un Equipo de Fútbol")
        println("6. Eliminar Jugador de un Equipo de Fútbol")
        println("7. Eliminar un Equipo de Fútbol")
        println("8. Salir")
        print("Ingrese su elección: ")

        when (readLine()?.toIntOrNull() ?: 6) {
            1 -> {
                println("\nIngrese información del Equipo:")
                print("ID: ")
                val id = readlnOrNull()?.toIntOrNull() ?: -1
                print("Fecha de debut(yyyy-MM-dd) [En blanco para la fecha actual]: ")
                val debutDateString = readlnOrNull()
                val debutDate =
                    debutDateString?.let {
                        if (it.isNotBlank()) dateFormat.parse(it) else Date()
                    }
                        ?: Date()
                print("Esta activo (si/no): ")
                val isActive = readlnOrNull() == "si"
                print("Nombre: ")
                val name = readlnOrNull() ?: ""
                print("Ingreso Neto [$]: ")
                val value = readlnOrNull()?.toFloatOrNull() ?: 0.0f
                val newTeam = SoccerTeam(id, name, debutDate, value, isActive, mutableListOf<Int>())
                teamService.addSoccerTeam(newTeam)
                println("Equipo añadido")
            }

            2 -> {
                print("\nIngrese ID de Equipo: ")
                val teamId = readlnOrNull()?.toIntOrNull() ?: -1
                val team = teamService.getSoccerTeamById(teamId)
                if (team != null) {
                    println("\nEquipo:\n $team")
                } else {
                    println("\nEquipo no encontrado!")
                }
            }

            3 -> {
                val all = teamService.getAllSoccerTeams()
                println("\n Equipos:\n")
                all.forEach {
                    println("$it")
                }
            }

            4 -> {
                println("\nActualizar:")
                print("ID Equipo: ")
                val id = readLine()?.toIntOrNull() ?: -1
                val existing = teamService.getSoccerTeamById(id)
                if (existing != null) {
                    print(
                        "Fecha fundación (yyyy-MM-dd) [Enter para mantener valor actual '${dateFormat.format(existing.foundationDate)}']: "
                    )
                    val dateString = readLine()
                    val date =
                        dateString?.let {
                            if (it.isNotBlank()) dateFormat.parse(it)
                            else existing.foundationDate
                        }
                            ?: existing.foundationDate

                    print(
                        "Esta activo (si/no) [Enter para mantener valor actual '${existing.isActive}']: "
                    )
                    val isActiveInput = readlnOrNull()
                    var isActive = false
                    if (isActiveInput == null) {
                        isActive = existing.isActive
                    } else {
                        isActive = isActiveInput == "si"
                    }

                    print("Nombre [Enter para mantener valor actual '${existing.name}']: ")
                    val name = readlnOrNull() ?: existing.name

                    print("Ingreso neto [Enter para mantener valor actual '${existing.netIncome}']: ")
                    val valueInput = readLine()
                    val value = valueInput?.toFloatOrNull() ?: existing.netIncome

                    val updated = SoccerTeam(id, name, date, value, isActive, existing.players)
                    teamService.updateSoccerTeam(updated)
                    println("\nJugador Actualizado!")
                } else {
                    println("\nJugador no encontrado!")
                }
            }

            5 -> {
                print("\nIngrese el ID del Equipo de Fútbol al que desea agregar un jugador: ")
                val teamIdToUpdate = readLine()?.toIntOrNull() ?: -1
                val existingTeam = teamService.getSoccerTeamById(teamIdToUpdate)

                if (existingTeam != null) {
                    println("\nEquipo de Fútbol actual:")
                    println(existingTeam)

                    print("\nIngrese el ID del jugador a agregar: ")
                    val playerId = readLine()?.toIntOrNull() ?: -1
                    val player = playerService.getPlayerById(playerId)
                    if (player != null) {
                        existingTeam.players.add(playerId)
                        teamService.updateSoccerTeam(existingTeam)
                        println("Jugador ${player.name} agregado al equipo")
                    } else {
                        println("ID de jugador inválido.")
                    }
                } else {
                    println("\nEquipo de Fútbol no encontrado")
                }
            }

            6 -> {
                print("\nIngrese el ID del Equipo del que desea eliminar un jugador: ")
                val teamIdToUpdate = readLine()?.toIntOrNull() ?: -1
                val existingTeam = teamService.getSoccerTeamById(teamIdToUpdate)

                if (existingTeam != null) {
                    println("\nEquipo actual:")
                    println(existingTeam)

                    print("\nIngrese el ID del jugador a eliminar: ")
                    val playerId = readLine()?.toIntOrNull()
                    if (playerId != null) {
                        if (existingTeam.players.find { it == playerId } != null) {
                            existingTeam.players.remove(playerId)
                            teamService.updateSoccerTeam(existingTeam)
                            println("Jugador eliminado del equipo")
                        } else {
                            println("ID de jugador no encontrado en el equipo de fútbol.")
                        }
                    } else {
                        println("ID de jugador inválido. No se eliminó del equipo de fútbol.")
                    }
                } else {
                    println("\nEquipo no encontrado")
                }
            }

            7 -> {
                print("\nIngrese el ID de equipo a borrar: ")
                val teamIdToDelete = readLine()?.toIntOrNull() ?: -1
                teamService.deleteSoccerTeam(teamIdToDelete)
                println("Equipo borrado")
            }

            8 -> {
                exit = true
            }

            else -> println("\nOpcion Invalida intente de nuevo.")
        }
    }
}

public fun playerCRUD() {
    var exit = false
    val playerService = PlayerService("players.csv")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    while (!exit) {
        println("\nJugadores de Fútbol")
        println("\nSelecione:")
        println("1. Anadir Jugador")
        println("2. Buscar jugador con ID")
        println("3. Mostrar todos los jugadores")
        println("4. Actualizar jugador")
        println("5. Borrar jugador")
        println("6. Salir")
        print("Ingrese opción: ")

        when (readLine()?.toIntOrNull() ?: 6) {
            1 -> {
                println("\nIngrese información del Jugador:")
                print("ID: ")
                val id = readLine()?.toIntOrNull() ?: -1
                print("Fecha de debut(yyyy-MM-dd) [En blanco para la fecha actual]: ")
                val debutDateString = readLine()
                val debutDate =
                    debutDateString?.let {
                        if (it.isNotBlank()) dateFormat.parse(it) else Date()
                    }
                        ?: Date()
                print("Esta lesionado (si/no): ")
                val isInjured = readLine() == "si"
                print("Nombre: ")
                val name = readLine() ?: ""
                print("Precio [$]: ")
                val value = readLine()?.toFloatOrNull() ?: 0.0f

                val newPlayer = Player(id, debutDate, isInjured, name, value)
                playerService.addPlayer(newPlayer)
                println("Jugador añadido!")
            }

            2 -> {
                print("\nIngrese ID de jugadr: ")
                val playerId = readLine()?.toIntOrNull() ?: -1
                val player = playerService.getPlayerById(playerId)
                if (player != null) {
                    println("\nJugador:\n $player")
                } else {
                    println("\nJugador no encontrado!")
                }
            }

            3 -> {
                val allPlayers = playerService.getAllPlayers()
                println("\n Jugadores:\n")
                allPlayers.forEach {
                    println("$it")
                }
            }

            4 -> {
                println("\nActualizar:")
                print("ID Jugador: ")
                val id = readLine()?.toIntOrNull() ?: -1
                val existingPlayer = playerService.getPlayerById(id)
                if (existingPlayer != null) {
                    print(
                        "Fecha debut (yyyy-MM-dd) [Enter para mantener valor actual '${dateFormat.format(existingPlayer.debutDate)}']: "
                    )
                    val debutDateString = readLine()
                    val debutDate =
                        debutDateString?.let {
                            if (it.isNotBlank()) dateFormat.parse(it)
                            else existingPlayer.debutDate
                        }
                            ?: existingPlayer.debutDate

                    print(
                        "Lesionado (si/no) [Enter para mantener valor actual '${existingPlayer.isInjured}']: "
                    )
                    val isInjuredInput = readLine()
                    val isInjured = isInjuredInput == "si"

                    print("Nombre [Enter para mantener valor actual '${existingPlayer.name}']: ")
                    val name = readLine() ?: existingPlayer.name

                    print("Precio [Enter para mantener valor actual '${existingPlayer.value}']: ")
                    val valueInput = readLine()
                    val value = valueInput?.toFloatOrNull() ?: existingPlayer.value

                    val updatedPlayer = Player(id, debutDate, isInjured, name, value)
                    playerService.updatePlayer(updatedPlayer)
                    println("\nJugador Actualizado!")
                } else {
                    println("\nJugador no encontrado!")
                }
            }

            5 -> {
                print("\nIngrese el ID de jugador a borrar: ")
                val playerIdToDelete = readLine()?.toIntOrNull() ?: -1
                playerService.deletePlayer(playerIdToDelete)
                println("Jugador borrado!")
            }

            6 -> {
                exit = true
            }

            else -> println("\nOpcion Invalida intente de nuevo.")
        }
    }
}
