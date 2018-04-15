package com.elissamc.api.ChatSystem

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerChatEvent
import cn.nukkit.plugin.service.RegisteredServiceProvider
import cn.nukkit.utils.TextFormat
import me.lucko.luckperms.api.LuckPermsApi
import me.lucko.luckperms.api.User

import java.util.Objects

class ChatListener : Listener {

    private var api: LuckPermsApi? = null

    init {
        val provider = Server.getInstance().serviceManager.getProvider(LuckPermsApi::class.java)
        if (provider != null) {
            api = provider.provider

        }
    }

    private operator fun contains(s: String): Boolean {
        for (cf in ChatFormat.values()) {
            if (cf.name == s)
                return true
        }
        return false
    }

    @EventHandler
    fun onChat(event: PlayerChatEvent) {
        val player = event.player
        var group = Objects.requireNonNull<User>(api!!.getUser(player.uniqueId)).getPrimaryGroup().toUpperCase()
        if (!this.contains(group))
            group = "DEFAULT"
        var s = ChatFormat.valueOf(group).toString()
        s = s.replace("%name".toRegex(), player.name)
        s = s.replace("%msg".toRegex(), event.message)
        s = TextFormat.colorize(s)
        event.format = s

    }
}
