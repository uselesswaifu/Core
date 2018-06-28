package com.elissamc.chat

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerChatEvent
import me.lucko.luckperms.api.LuckPermsApi
import java.util.*
import cn.nukkit.utils.TextFormat

class ChatListener : Listener {

    init {
        val provider = Server.getInstance().serviceManager.getProvider(LuckPermsApi::class.java)
        if (provider != null) {
            api = provider.provider
        }
    }

    private fun contains(s: String) : Boolean {
        for(cf in ChatFormat.values()){
            if(cf.name == s)
                return true
        }
        return false
    }

    @EventHandler
    private fun onChat(event: PlayerChatEvent){
        var player: Player = event.player
        var group: String = Objects.requireNonNull(api.getUser(player.uniqueId)!!.primaryGroup.toUpperCase())
        if(!contains(group))
            group = "DEFAULT"
        var s = ChatFormat.valueOf(group).toString()
        s = s.replace("%name".toRegex(), player.name)
        s = s.replace("%msg".toRegex(), event.message)
        s = TextFormat.colorize(s)
        event.format = s
    }

    companion object {
        lateinit var api: LuckPermsApi
    }
}