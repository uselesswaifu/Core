package com.elissamc

import cn.nukkit.Server
import cn.nukkit.event.Listener
import cn.nukkit.level.Location
import cn.nukkit.permission.Permission
import com.elissamc.chat.ChatListener
import com.elissamc.commands.*
import com.elissamc.core.StorageEngine
import com.elissamc.games.GameHandler
import com.elissamc.listener.LobbyListener
import com.elissamc.parkour.ParkourSystem
import jline.internal.Log
import java.io.File
import java.util.*


class ElissaCore(private val mommy: ElissaMC) : Listener {
    lateinit var gameHandler: GameHandler
//    private lateinit var eventProcessor: EventProcessor
    var random: Random = Random()

    init {
        for (map in maps) try {
            Server.getInstance().loadLevel(map)
            val level = Server.getInstance().getLevelByName(map)
            level.rainTime = 9999
            level.isRaining = false
            level.isThundering = false
            level.stopTime()
        }
        catch (error: IllegalStateException){
            Server.getInstance().logger.error("Level called: $map could not be found!")
        }
        this.load()
//        HardCoded.main()
    }

    private fun load() {
        this.registerEvents()
        this.registerCommands()
        this.createDirectoryStructure()
        StorageEngine.initialize()
        StorageEngine.loadData()
        gameHandler = GameHandler()
//        eventProcessor = EventProcessor()
    }

    private fun registerEvents() {
        mommy.server.pluginManager.registerEvents(PluginsCmd(), ElissaMC.plugin)
        mommy.server.pluginManager.registerEvents(LobbyListener(), ElissaMC.plugin)
        mommy.server.pluginManager.registerEvents(ParkourSystem(), ElissaMC.plugin)
        mommy.server.pluginManager.registerEvents(ChatListener(), ElissaMC.plugin)
    }

    private fun registerCommands() {
        mommy.server.commandMap.register("report", ReportCmd())
        mommy.server.commandMap.register("nick", NickCmd())

        mommy.server.commandMap.register("friend", FriendCommand("friend"))
        mommy.server.commandMap.register("party", PartyCommand("party"))
        mommy.server.commandMap.register("settings", SettingsCommand("settings"))
        mommy.server.commandMap.register("unfriend", UnfriendCommand("unfriend"))
    }

    private fun createDirectoryStructure() {
        var created = false
        val path = ElissaMC.folder.absolutePath
        created = created or File("$path/cache").mkdirs()
        created = created or File("$path/records").mkdirs()
        created = created or File("$path/profiles").mkdirs()
        created = created or File("$path/clips").mkdirs()
        if (created)
            Log.info("Directory structure expanded!")
    }

    fun getLobbyLocation(): Location {
        return ElissaMC.getWorld("Lobby")!!.safeSpawn!!.location
    }

    companion object {

        private val maps = arrayOf("world")
    }
}
