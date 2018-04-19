package com.elissamc.core;

import cn.nukkit.Player;

import java.util.Collection;

/**
 * Specifies class that hold collection of players.
 *
 * @author Mato Kormuth
 *
 */
public interface PlayerHolder {

    public Collection<Player> getPlayers();

    public int getPlayerCount();

    public boolean contains(Player player);
}