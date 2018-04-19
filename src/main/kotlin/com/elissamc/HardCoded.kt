package com.elissamc

import cn.nukkit.Server
import cn.nukkit.level.Location
import cn.nukkit.math.Vector3
import com.elissamc.actions.CommandAction
import com.elissamc.actions.TeleportAction
import com.elissamc.area.AreaFlag
import com.elissamc.area.Lobby
import com.elissamc.core.Region
import com.elissamc.core.StorageEngine
import com.elissamc.core.TeleportGate
import java.util.*

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

        //dobrakmato - block interactions
        StorageEngine.getLobby("hub").setPlayerFlag(AreaFlag.BLOCK_BREAK, true,
                UUID.fromString("966ad920-d45e-3fe5-8956-bf7a7a877ab4"))
        StorageEngine.getLobby("hub").setPlayerFlag(AreaFlag.BLOCK_PLACE, true,
                UUID.fromString("966ad920-d45e-3fe5-8956-bf7a7a877ab4"))
    }

    private fun initGates() {
        try {
            StorageEngine.addGate("Lminigame", TeleportGate(Region(Vector3(7.0, 50.0,
                    280.0), Vector3(13.0, 55.0, 282.0), ElissaMC.getWorld("world")),
                    TeleportAction(Location(1972.5, 147.5,
                            2492.5, ElissaMC.getWorld("world")))))

            //Initialize gates
            StorageEngine.addGate("mg_colorwar", TeleportGate(
                    Region(Vector3(1976.0, 147.0, 2532.0), Vector3(1972.0, 153.0, 2534.0),
                            ElissaMC.getWorld("world")), CommandAction("pcmd cwtest")))

            StorageEngine.addGate("mg_tnttag", TeleportGate(Region(Vector3(1962.0,
                    147.0, 2532.0), Vector3(1967.0, 153.0, 2534.0), ElissaMC.getWorld("world")),
                    CommandAction("pcmd tnttest")))
        } catch (e: Exception) {
            Server.getInstance().logger.error("Could not initialize Gates!")
        }

    }
}