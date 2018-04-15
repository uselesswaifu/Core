package com.elissamc.games

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.Listener
import cn.nukkit.plugin.Plugin
import cn.nukkit.scheduler.PluginTask
import com.elissamc.ElissaMC

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class GameParent(private val game: Any, plugin: Any) {

    init {
        Server.getInstance().pluginManager.registerEvents(game as Listener, plugin as Plugin)
        Server.getInstance().scheduler.scheduleRepeatingTask(ElissaMC.plugin, game as PluginTask<ElissaMC>, 20)
    }


    fun addPlayer(player: Player) {
        try {
            val method = game.javaClass.getMethod("addPlayer", Player::class.java)
            try {
                method.invoke(game, player)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }

    }

    fun removePlayer(player: Player) {
        try {
            val method = game.javaClass.getMethod("removePlayer", Player::class.java)
            try {
                method.invoke(game, player)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }

    }
}