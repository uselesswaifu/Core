package com.elissamc.games

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.Listener

import java.util.HashMap
import java.util.UUID

class GameHandler : Listener {

    val games = HashMap<String, GameParent>()
    private val playerStatus = HashMap<UUID, String>()

    fun getGames(): Map<String, GameParent> {
        return games
    }

    fun joinGame(player: Player, s: String): Boolean {
        if (games.containsKey(s)) {
            games[s]!!.addPlayer(player)
            playerStatus[player.uniqueId] = s
            return true
        }
        return false
    }

    fun addGame(name: String, lobbys: GameParent) {
        games[name] = lobbys
    }

    fun removeGame(player: Player) {
        if (playerStatus.containsKey(player.uniqueId)) {
            games[playerStatus[player.uniqueId]]!!.removePlayer(player)
            player.teleport(Server.getInstance().defaultLevel.safeSpawn)
        }
    }
}