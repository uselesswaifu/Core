package com.elissamc.games

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.Listener
import com.elissamc.core.StorageEngine

import java.util.HashMap
import java.util.UUID

class GameHandler : Listener {

    private val playerStatus = HashMap<UUID, String>()

    fun getGames(): Map<String, Minigame> {
        return StorageEngine.minigames
    }

    fun joinGame(player: Player, s: String): Boolean {
        if (getGames().containsKey(s)) {
            getGames()[s]?.addPlayer(player)
            playerStatus[player.uniqueId] = s
            return true
        }
        return false
    }

    fun addGame(minigame: Minigame) {
        StorageEngine.addMinigame(minigame)
    }

    fun removeGame(player: Player) {
        if (playerStatus.containsKey(player.uniqueId)) {
            getGames()[playerStatus[player.uniqueId]]!!.removePlayer(player)
            player.teleport(Server.getInstance().defaultLevel.safeSpawn)
        }
    }

    fun getGame(name: String): Minigame? {
        if(getGames().containsKey(name))
            return getGames()[name]
        return null
    }
}