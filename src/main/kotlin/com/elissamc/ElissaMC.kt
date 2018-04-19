package com.elissamc

import cn.nukkit.Server
import cn.nukkit.event.Listener
import cn.nukkit.level.Level
import cn.nukkit.plugin.PluginBase
import cn.nukkit.utils.TextFormat
import java.io.File

class ElissaMC : PluginBase(), Listener {

    override fun onEnable() {
        logger.info(TextFormat.RED.toString() + "ElissaMC has been enabled!")
        this.init()
    }

    fun init() {
        ElissaMC.plugin = this
        ElissaMC.folder = this.dataFolder
        ElissaMC.core = ElissaCore(this)
        server.pluginManager.registerEvents(core, this)
    }

    companion object {
        @JvmStatic
        fun getWorld(world: String): Level? {
            return Server.getInstance().getLevelByName(world)
        }

        lateinit var plugin: ElissaMC
        lateinit var folder: File
        lateinit var core: ElissaCore
    }

}
