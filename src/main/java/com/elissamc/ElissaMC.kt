package com.elissamc

import cn.nukkit.event.Listener
import cn.nukkit.plugin.PluginBase
import cn.nukkit.utils.TextFormat
import com.elissamc.instance.Instance

import java.io.File

class ElissaMC : PluginBase(), Listener {
    lateinit var instance: Instance

    override fun onEnable() {
        logger.info(TextFormat.RED.toString() + "ElissaMC has been enabled!")
        plugin = this
        folder = this.dataFolder
        instance = Instance(this)
        server.pluginManager.registerEvents(instance, this)
        instance.load()
    }

    companion object {
        lateinit var folder: File
        lateinit var plugin: ElissaMC
    }

}
