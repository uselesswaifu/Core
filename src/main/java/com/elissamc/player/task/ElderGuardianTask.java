package com.elissamc.player.task;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.scheduler.NukkitRunnable;
import com.elissamc.ElissaMC;

public class ElderGuardianTask extends NukkitRunnable {
	private final Player player;
	private final Position location;
	private final String title;
	private final String subtitle;

	public ElderGuardianTask(ElissaMC instance, Player player, Position location, String title, String subtitle) {
		this.player = player;
		this.location = location;
		this.title = title;
		this.subtitle = subtitle;
	}

	@Override
	public void run() {
		final LevelEventPacket pk = new LevelEventPacket();
		pk.evid = LevelEventPacket.EVENT_GUARDIAN_CURSE;
		pk.data = 0;
		pk.x = (float) player.x;
		pk.y = (float) player.y;
		pk.z = (float) player.z;

		player.dataPacket(pk);
		player.setSubtitle(subtitle);
		player.sendTitle(title);
		player.teleport(location);

	}

}
