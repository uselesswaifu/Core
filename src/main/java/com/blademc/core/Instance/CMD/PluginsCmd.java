package com.blademc.core.Instance.CMD;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.utils.TextFormat;

public class PluginsCmd implements Listener {

	@EventHandler
	public void onPluginsCmd(PlayerCommandPreprocessEvent event) {

		if (event.getMessage().equals("/pl") || event.getMessage().equals("/plugins")) {
			event.setCancelled();
			event.getPlayer()
					.sendMessage(TextFormat.GRAY + "Plugins (5): " + TextFormat.GOLD + "You" + TextFormat.GRAY + ", "
							+ TextFormat.GOLD + "Can't" + TextFormat.GRAY + ", " + TextFormat.GOLD + "See"
							+ TextFormat.GRAY + ", " + TextFormat.GOLD + "Our" + TextFormat.GRAY + ", "
							+ TextFormat.GOLD + "Plugins " + TextFormat.GRAY + "v" + TextFormat.GOLD + "2.0");
		}

	}

}
