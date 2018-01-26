package com.blademc.core.utils.Lobby;

import cn.nukkit.Player;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerCreationEvent;
import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import com.blademc.core.API.PartySystem.PartySystem;
import com.blademc.core.BladeMC;
import com.blademc.core.Instance.BladeCultist;

import java.util.HashMap;
import java.util.Map;

public class Lobby implements Listener {

	public int time = 0;
	public static final Map<String, String> lobby = new HashMap<>();
	private final Map<String, LobbyClass> testLobby = new HashMap<>();
	public com.blademc.core.utils.Lobby.MainLobby mainLobby;

    public Lobby() {
		addLobby("main", new LobbyClass(new MainLobby(), BladeMC.plugin));
	}

	public void addLobby(String name, LobbyClass lobbys){
        testLobby.put(name, lobbys);
    }

    public static void message(Player player){
        player.sendMessage("Accessed from another module!");
    }

    public void setLobby(String name, Player player){
    	if(!PartySystem.IsInParty(player)) {
			if (lobby.containsKey(player.getName()))
				testLobby.get(lobby.get(player.getName())).removePlayer(player);
			this.lobby.put(player.getName(), name);
			testLobby.get(name).addPlayer(player);
			return;
		}

		if(PartySystem.isPartyLeader(player)){
    		for(Player member : PartySystem.getPartyMembers(player)){
				if (lobby.containsKey(member.getName()))
					testLobby.get(lobby.get(member.getName())).removePlayer(member);
				this.lobby.put(member.getName(), name);
				testLobby.get(name).addPlayer(member);
			}
			return;
		}
        player.sendMessage("You are not the party leader");

    }


	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (!lobby.containsKey(event.getPlayer().getName()))
			setLobby("main", event.getPlayer());
		else
			setLobby(lobby.get(event.getPlayer().getName()), event.getPlayer());
	}

	public void strikeLighting(Position pos) {
		final FullChunk chunk = pos.getLevel().getChunk((int) pos.getX() >> 4, (int) pos.getZ() >> 4);
		final CompoundTag nbt = new CompoundTag()
				.putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", pos.getX()))
						.add(new DoubleTag("", pos.getY())).add(new DoubleTag("", pos.getZ())))
				.putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0)).add(new DoubleTag("", 0))
						.add(new DoubleTag("", 0)))
				.putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0)).add(new FloatTag("", 0)));
		final EntityLightning lightning = new EntityLightning(chunk, nbt);
		lightning.setEffect(false);
		lightning.spawnToAll();
	}

	@EventHandler
	public void onPlayerCreationEvent(PlayerCreationEvent event) {
		event.setPlayerClass(BladeCultist.class);
	}

	@EventHandler
	public void onHunger(PlayerFoodLevelChangeEvent event) {
		event.setFoodLevel(20);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {

		if (!event.getFrom().equals(event.getTo()))
			if (event.getPlayer().y <= 0) {
				event.getPlayer().sendActionBar("&7You have been returned back to &oSpawn".replaceAll("&", "§"));
				//event.getPlayer().teleport(event.getPlayer().getLevel().getSafeSpawn());
			}
	}

	public static boolean isInGame(Player p) {
		// TODO Auto-generated method stub
		String ln = p.getLevel().getName();
		return !ln.equals("Lobby") && !ln.equals("Maps");
	}

	public void sendLobby(Player p) {
		setLobby(lobby.get(p.getName()), p);
	}
}
