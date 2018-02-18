package com.elissamc.games;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameHandler implements Listener {

    private final Map<String, GameParent> games = new HashMap<>();
    private final Map<UUID, String> playerStatus = new HashMap<>();

    public boolean joinGame(Player player, String s) {
        if (games.containsKey(s)) {
            games.get(s).addPlayer(player);
            playerStatus.put(player.getUniqueId(), s);
            return true;
        }
        return false;
    }

    public void addGame(String name, GameParent lobbys) {
        games.put(name, lobbys);
    }

    public void removeGame(Player player) {
        if (playerStatus.containsKey(player.getUniqueId())) {
            games.get(playerStatus.get(player.getUniqueId())).removePlayer(player);
            player.teleport(Server.getInstance().getDefaultLevel().getSafeSpawn());
        }
    }
}