package com.blademc.core.player.event;

import cn.nukkit.Player;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.DummyBossBar;
import cn.nukkit.utils.TextFormat;
import com.blademc.core.BladeMC;
import com.blademc.core.Instance.BladeCultist;
import com.blademc.core.Instance.Instance;
import com.blademc.core.PictureLogin.ImageMessage;
import com.blademc.core.PictureLogin.imgmsg.Testing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerJoinEventBC implements Listener {

	public static File serverCape;

	public PlayerJoinEventBC(Instance instance) {
		serverCape = new File(BladeMC.dataFolder + "/images/" + "cape.png");
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage("");
	}

	@EventHandler
	public void onPreJoin(PlayerPreLoginEvent event) {
		final Skin skin = new Skin(event.getPlayer().getSkin().getData());
		skin.setCape(skin.new Cape(serverCape, skin.getModel()));
		event.getPlayer().setSkin(skin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		saveSkin(player);
		player.addServerSettings(Instance.serverMenu);
		PlayerSettings(player);
		((BladeCultist) player).addCoins(0);
		((BladeCultist) player).setData("keys", 8);
		event.setJoinMessage(
				TextFormat.BLUE + "Lobby> " + TextFormat.YELLOW + player.getName() + TextFormat.GRAY + " has connected to the " + TextFormat.YELLOW + "Lobby-1");

		try {
			BufferedImage img = ImageIO.read(new File(BladeMC.dataFolder + "/skins/players/", player.getName() + ".png")).getSubimage(8,8,8,8);


			new ImageMessage(toBufferedImage(img.getScaledInstance(64, 64, 1)), 8, Testing.getChar()).appendCenteredText("", TextFormat.YELLOW + "Welcome to BladeMC!", "", TextFormat.BLUE + "Purchase things @ shop.blademc.pw", "", "", TextFormat.YELLOW + TextFormat.BOLD.toString() + "Have Fun!", "").sendToPlayer(player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage toBufferedImage(Image image) {
		BufferedImage buffer = new BufferedImage(
				image.getWidth(null),
				image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB
		);
		Graphics2D g = buffer.createGraphics();

		g.drawImage(image, null, null);
		return buffer;
	}

	public void PlayerSettings(Player player) {
		Instance.bossbar.put(player.getName(), new DummyBossBar.Builder(player)
				.text("&7-&8=&7- &7You're playing on &6&lBlade&3MC &7-&8=&7- &r"
						.replaceAll("&", "§"))
				.length(100).color(Color.GREEN).build());
		player.createBossBar(((BladeCultist) player).getBossbar());
	}

	public void saveSkin(Player player) {
		final byte[] byteArray = player.getSkin().getData();
		final BufferedImage finalSkin = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		final int entireByteArray = byteArray.length;
		int start = 0;
		int x = 0;
		int y = 0;
		while (entireByteArray > start) {
			if (x == 64) {
				x = 0;
				y++;
			}
			final int r = byteArray[start] & 0xFF;
			final int g = byteArray[start + 1] & 0xFF;
			final int b = byteArray[start + 2] & 0xFF;
			final int a = byteArray[start + 3] & 0xFF;

			finalSkin.setRGB(x, y, new Color(r, g, b, a).getRGB());
			start = start + 4;
			x++;

		}
		try {
			ImageIO.write(finalSkin, "png",
					new File(BladeMC.dataFolder + "/skins/players/", player.getName() + ".png"));
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}

}
