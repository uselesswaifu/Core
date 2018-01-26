package com.blademc.core.Cosmetic;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.TextFormat;
import com.blademc.core.BladeMC;
import com.blademc.core.Instance.BladeCultist;

import java.util.Random;

public class MysteryBox extends NukkitRunnable {
	private final Player player;
	private static int time = 70;
	public static String legend = TextFormat.DARK_GRAY + "[" + TextFormat.RESET + "" + TextFormat.DARK_PURPLE + "LEGEND"
			+ " " + TextFormat.RESET;

	private static Item second = Item.get(Item.DIAMOND_LEGGINGS, 0, 1);
	private static Item third = Item.get(Item.DIAMOND_LEGGINGS, 0, 1);
	private final Random rand = new Random();
	public static Item items[] = {
			Item.get(Item.LEATHER_TUNIC, 0, 1).setCustomName("Rainbow Suit")
					.setCustomBlockData(new CompoundTag().putString("type", "styles").putInt("identify", 0)),
			Item.get(Item.SKULL, 13, 1).setCustomName("EnderDragon Suit")
					.setCustomBlockData(new CompoundTag().putString("type", "styles").putInt("identify", 1)),

			Item.get(Item.TNT, 0, 1).setCustomName("TNT")
					.setCustomBlockData(new CompoundTag().putString("type", "gadgets").putInt("identify", 2)),
			Item.get(Item.WOODEN_AXE, 0, 1).setCustomName("Generic Gadget")
					.setCustomBlockData(new CompoundTag().putString("type", "gadgets").putInt("identify", 3)),

			Item.get(Item.SPAWN_EGG, 3, 1).setCustomName("Dog")
					.setCustomBlockData(new CompoundTag().putString("type", "pets").putInt("identify", 4)),
			Item.get(Item.DRAGON_EGG, 0, 1).setCustomName("Drago")
					.setCustomBlockData(new CompoundTag().putString("type", "pets").putInt("identify", 5)),

			Item.get(Item.ELYTRA, 0, 1).setCustomName("Wings")
					.setCustomBlockData(new CompoundTag().putString("type", "particles").putInt("identify", 6)),
			Item.get(Item.FISHING_ROD, 0, 1).setCustomName("Generic Particle")
					.setCustomBlockData(new CompoundTag().putString("type", "particles").putInt("identify", 7)), };

	public MysteryBox(BladeMC instance, Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		time--;
		if (time >= 1) {
		    player.level.addSound(player, Sound.RANDOM_ORB);
			Item first = second;
			second = third;
			third = items[rand.nextInt(items.length)];
            for(int i=0; i<28; i++){
                player.getEnderChestInventory().setItem(i, Item.get(Item.STAINED_GLASS, rand.nextInt(15), 1));
            }
			player.getEnderChestInventory().setItem(12, first);
            player.getEnderChestInventory().setItem(13, second);
            player.getEnderChestInventory().setItem(14, third);
			BladeMC.plugin.getInstance().getCrateParticle(0).setTitle("&7".replaceAll("&", "§") + first.getName());
			BladeMC.plugin.getInstance().getCrateParticle(1).setTitle("&7>> &f".replaceAll("&", "§") + second.getName() + " &7<<".replaceAll("&", "§"));
			BladeMC.plugin.getInstance().getCrateParticle(2).setTitle("&7".replaceAll("&", "§") + third.getName());
			player.getLevel().addParticle(BladeMC.plugin.getInstance().getCrateParticle(0), player);
			player.getLevel().addParticle(BladeMC.plugin.getInstance().getCrateParticle(1), player);
			player.getLevel().addParticle(BladeMC.plugin.getInstance().getCrateParticle(2), player);
		}
		if (time == 0) {
			player.level.addSound(player, Sound.NOTE_SNARE);
		    player.getEnderChestInventory().close(player);
			player.sendMessage(TextFormat.GRAY + "" + second.getName() + " is the item you won!");
			spawnParticle();
			((BladeCultist) player).addCosmeticItem(second);

			BladeMC.plugin.getInstance().getCrateParticle(0).setTitle(" ");
			BladeMC.plugin.getInstance().getCrateParticle(1).setTitle(TextFormat.GRAY + "" + Integer.toString(((BladeCultist) player).checkData("keys")) + " Boxes");
			BladeMC.plugin.getInstance().getCrateParticle(2).setTitle(" ");
		}
		if (time <= -10) {
			Server.getInstance().getDefaultLevel()
					.addParticle(BladeMC.plugin.getInstance().getCrateParticle(0), player);
			Server.getInstance().getDefaultLevel()
					.addParticle(BladeMC.plugin.getInstance().getCrateParticle(1), player);
			Server.getInstance().getDefaultLevel()
					.addParticle(BladeMC.plugin.getInstance().getCrateParticle(2), player);
			//task.cancel();
			//cancel();
			time = 20;
		}
	}

	private void spawnParticle() {
		final Random rand = new Random();
		TaskHandler task = Server.getInstance().getScheduler().scheduleRepeatingTask(BladeMC.plugin, () -> {
			final Level level = player.getLevel();
			final double x = 34.5;
			final double y = 95;
			final double z = -20.5;
			final double radius = 0.5;
			for (double zy = 0; zy <= 7; zy += 0.2) {
				final double zx = radius * Math.cos(zy * 2);
				final double zz = radius * Math.sin(zy * 2);
				final int r = rand.nextInt(250);
				final int g = rand.nextInt(250);
				final int b = rand.nextInt(250);
				final DustParticle particle = new DustParticle(new Vector3(x + zx, y + zy / 3, z + zz), r, g, b);
				level.addParticle(particle);
			}

		}, 10);
		task.cancel();
	}

}
