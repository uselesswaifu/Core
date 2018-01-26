package com.blademc.core.Cosmetic;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import com.blademc.core.BladeMC;
import com.blademc.core.Instance.BladeCultist;

public class Cosmetics implements Listener {

	@EventHandler
	public void onCrateOpen(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (event.getBlock().getId() != Block.ENDER_CHEST)
			return;
		event.setCancelled();
		final int keys = ((BladeCultist) player).checkData("keys");
		if (keys > 0) {
			((BladeCultist) player).setData("keys", keys - 1);
			new MysteryBox(BladeMC.plugin, event.getPlayer()).runTaskTimer(BladeMC.plugin, 0, 6);
		}

	}

}
