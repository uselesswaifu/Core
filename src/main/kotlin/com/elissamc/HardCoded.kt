package com.elissamc

import cn.nukkit.Server
import cn.nukkit.math.Vector3
import com.elissamc.area.Lobby
import com.elissamc.core.Region
import com.elissamc.core.StorageEngine

object HardCoded {

    fun main() {

        //Initialize main gates
        initGates()

        //Initialize lobbies
        initLobbies()
    }

    private fun initLobbies() {
        try {
            StorageEngine.addLobby(Lobby("hub", Region(Vector3(-93.0, 34.0, -137.0),
                    Vector3(226.0, 155.0, 208.0), Server.getInstance().getLevelByName("Lobby"))))
        } catch (e: Exception) {
            return
        }

        StorageEngine.getLobby("hub").spawn = Server.getInstance().getLevelByName("Lobby").safeSpawn.location
        StorageEngine.getLobby("hub").thresholdY = 20
    }

    private fun initGates() {}
}