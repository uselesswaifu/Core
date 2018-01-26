package com.blademc.core.Cosmetic;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.TextFormat;
import com.blademc.core.Instance.BladeCultist;
import com.blademc.core.utils.Lobby.Lobby;

import java.util.Random;

public class UpdateCosmetic extends NukkitRunnable {

	private final Random rand = new Random();

	@Override
	public void run() {
		for (final Player p : Server.getInstance().getOnlinePlayers().values()) {
			Integer n = ((BladeCultist) p).currentstyle.get(p.getName());
			if (n == null)
				n = -1;

			switch (n) {
			case 0:
				rainbowArmor(p);
				break;

			}
		}
	}

	private void rainbowArmor(Player p) {
		if (!Lobby.isInGame(p)) {
			final int r1 = rand.nextInt(250);
			final int g1 = rand.nextInt(250);
			final int b1 = rand.nextInt(250);

			final int r2 = rand.nextInt(250);
			final int g2 = rand.nextInt(250);
			final int b2 = rand.nextInt(250);

			final int r3 = rand.nextInt(250);
			final int g3 = rand.nextInt(250);
			final int b3 = rand.nextInt(250);

			final int r4 = rand.nextInt(250);
			final int g4 = rand.nextInt(250);
			final int b4 = rand.nextInt(250);
			ItemColorArmor colorizeditem = (ItemColorArmor) Item.get(Item.LEATHER_CAP, 0, 1);
			colorizeditem.setColor(r1, g1, b1);
			p.getInventory().setHelmet(colorizeditem);
			colorizeditem = (ItemColorArmor) Item.get(Item.LEATHER_TUNIC, 0, 1);
			colorizeditem.setColor(r2, g2, b2);
			p.getInventory().setChestplate(colorizeditem);
			colorizeditem = (ItemColorArmor) Item.get(Item.LEATHER_PANTS, 0, 1);
			colorizeditem.setColor(r3, g3, b3);
			p.getInventory().setLeggings(colorizeditem);
			colorizeditem = (ItemColorArmor) Item.get(Item.LEATHER_BOOTS, 0, 1);
			colorizeditem.setColor(r4, g4, b4);
			p.getInventory().setBoots(colorizeditem);

		}
	}

	public static void clearArmor(Player p) {
		p.getInventory().setHelmet(Item.get(Item.AIR));
		p.getInventory().setChestplate(Item.get(Item.AIR));
		p.getInventory().setLeggings(Item.get(Item.AIR));
		p.getInventory().setBoots(Item.get(Item.AIR));
	}

	public void sendGadget(Player player) {
		Integer n = ((BladeCultist) player).currentgadget.get(player.getName());
		if (n == null)
			n = -1;
		switch (n) {
		case 2:
			player.getInventory().setItem(0, Item.get(Item.TNT, 0, 4).setCustomName(TextFormat.RED + "Explosive TNT"));
			break;
		}
	}

	public static void removeGadget(Player player) {
		player.getInventory().setItem(0, Item.get(Item.AIR));

	}
}
