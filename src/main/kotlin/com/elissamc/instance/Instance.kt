package com.elissamc.instance

import cn.nukkit.Server
import cn.nukkit.event.Listener
import cn.nukkit.level.Level
import cn.nukkit.permission.Permission
import com.elissamc.ElissaMC
import com.elissamc.api.ChatSystem.ChatListener
import com.elissamc.api.ParkourSystem.ParkourSystem
import com.elissamc.games.GameHandler
import com.elissamc.instance.CMD.NickCmd
import com.elissamc.instance.CMD.PluginsCmd
import com.elissamc.instance.CMD.ReportCmd
import com.elissamc.listener.LobbyListener
import com.elissamc.player.event.PartyChatEventBC

class Instance(private val mommy: ElissaMC) : Listener {
    lateinit var gameHandler: GameHandler
        private set

    init {
        for (map in maps) {
            try {
                Server.getInstance().loadLevel(map)
                val level = Server.getInstance().getLevelByName(map)
                level.rainTime = 9999
                level.isRaining = false
                level.isThundering = false
                level.stopTime()
            }

            catch (error: IllegalStateException){
                Server.getInstance().logger.error("Level titled $map could not be found!")
            }
        }
    }

    fun load() {
        this.registerEvents()
        this.registerCommands()
        this.registerPerms()
        gameHandler = GameHandler()
    }

    private fun registerPerms() {
        this.mommy.server.pluginManager.addPermission(Permission("elissamc.build", null, Permission.DEFAULT_FALSE))
        this.mommy.server.pluginManager.addPermission(Permission("elissamc.break", null, Permission.DEFAULT_FALSE))
        this.mommy.server.pluginManager.addPermission(Permission("elissamc.parkour", null, Permission.DEFAULT_TRUE))
    }

    private fun registerEvents() {
        mommy.server.pluginManager.registerEvents(PluginsCmd(), ElissaMC.plugin)
        mommy.server.pluginManager.registerEvents(PartyChatEventBC(), ElissaMC.plugin)
        mommy.server.pluginManager.registerEvents(LobbyListener(), ElissaMC.plugin)
        mommy.server.pluginManager.registerEvents(ParkourSystem(), ElissaMC.plugin)
        mommy.server.pluginManager.registerEvents(ChatListener(), ElissaMC.plugin)
    }

    private fun registerCommands() {
        mommy.server.commandMap.register("report", ReportCmd(this))
        mommy.server.commandMap.register("nick", NickCmd(this))
    }

    companion object {

        private val maps = arrayOf("world")
    }
}
