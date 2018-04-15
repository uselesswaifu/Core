package com.elissamc.instance.CMD

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerCommandPreprocessEvent
import cn.nukkit.utils.TextFormat

class PluginsCmd : Listener {

    @EventHandler
    fun onPluginsCmd(event: PlayerCommandPreprocessEvent) {

        if (event.message == "/pl" || event.message == "/plugins") {
            event.setCancelled()
            event.player
                    .sendMessage(TextFormat.GRAY.toString() + "Plugins (5): " + TextFormat.GOLD + "You" + TextFormat.GRAY + ", "
                            + TextFormat.GOLD + "Can't" + TextFormat.GRAY + ", " + TextFormat.GOLD + "See"
                            + TextFormat.GRAY + ", " + TextFormat.GOLD + "Our" + TextFormat.GRAY + ", "
                            + TextFormat.GOLD + "Plugins " + TextFormat.GRAY + "v" + TextFormat.GOLD + "2.0")
        }

    }

}
