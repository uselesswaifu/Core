package com.elissamc.player.event

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerChatEvent
import cn.nukkit.utils.TextFormat
import com.elissamc.ElissaMC

import java.util.Objects

class PartyChatEventBC : Listener {

    @EventHandler
    fun onChat(event: PlayerChatEvent) {
        //        String msg = TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + player.getDisplayName() + TextFormat.GRAY + " :" + event.getMessage();
        val player = event.player
    }
}
