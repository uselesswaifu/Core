package com.blademc.core.Cosmetic;

import cn.nukkit.event.EventHandler;
import com.blademc.core.Instance.BladeCultist;
import com.blademc.core.Instance.Instance;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;

public class Cosmetics implements Listener {

	Instance instance;

	public Cosmetics(Instance instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onCrateOpen(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (event.getBlock().getId() != 130)
			return;
		player.getEnderChestInventory().setItem(0, Item.get(Item.IRON_SWORD));
		//event.setCancelled();
		final int keys = ((BladeCultist) player).checkData("keys");
		if (keys > 0) {
			((BladeCultist) player).setData("keys", keys - 1);
			instance.getServer().getScheduler().scheduleRepeatingTask(
					new MysteryBox(instance.getMain(), event.getPlayer()), 6);
		}

	}

}
