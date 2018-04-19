package com.elissamc.core;

import cn.nukkit.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class used for party.
 *
 * @author Mato Kormuth
 *
 */
public class Party implements PlayerHolder {
    private final List<Player> players;
    private final Player       owner;

    /**
     * Creates new party.
     *
     * @param owner
     *            owner of the party
     */
    public Party(final Player owner) {
        this.players = new ArrayList<Player>();
        this.owner = owner;

        owner.sendMessage("Created new party!");
    }

    /**
     * Returns whether is specified player owner of the party.
     *
     * @param player
     *            specified player
     * @return true or false
     */
    public boolean isOwner(final Player player) {
        return this.owner == player;
    }

    /**
     * Adds player to party.
     *
     * @param player
     *            player to be added
     */
    public void addPlayer(final Player player) {
        this.players.add(player);
        player.sendMessage("You have joined "
                + this.owner.getDisplayName() + "'s party!");
    }

    @Override
    public Collection<Player> getPlayers() {
        return this.players;
    }

    @Override
    public int getPlayerCount() {
        return this.players.size();
    }

    public void removePlayer(final Player player) {
        this.players.remove(player);
        StorageEngine.getProfile(player.getUniqueId()).setParty(null);
    }

    @Override
    public boolean contains(final Player player) {
        return this.players.contains(player);
    }
}