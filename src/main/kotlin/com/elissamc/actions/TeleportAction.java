package com.elissamc.actions;


import cn.nukkit.Player;
import cn.nukkit.level.Location;


/**
 * Inventory action that teleports player to specified location.
 *
 */
public class TeleportAction implements Action {
    /**
     * Target location.
     */
    private final Location location;

    /**
     * Creates a new action.
     *
     * @param location
     */
    public TeleportAction(final Location location) {
        this.location = location;
    }

    @Override
    public void execute(final Player player) {
        // Just teleport player to target location.
        player.teleport(this.location);
    }
}