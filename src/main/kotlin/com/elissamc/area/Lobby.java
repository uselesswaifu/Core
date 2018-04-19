// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform.
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 *
 * This file is part of Pexel.
 *
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
package com.elissamc.area;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Location;
import cn.nukkit.potion.Effect;
import cn.nukkit.potion.Potion;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.scheduler.TaskHandler;
import com.elissamc.ElissaMC;
import com.elissamc.core.Region;
import com.elissamc.core.Updatable;
import com.elissamc.core.UpdatedParts;

/**
 * Lobby is protected area with some special functions.
 *
 * @author Mato Kormuth
 *
 */
public class Lobby extends ProtectedArea implements Updatable {
    /**
     * Creates new lobby object with specified name and region.
     *
     * @param name
     *            name of lobby
     * @param region
     *            region of lobby
     */
    public Lobby(final String name, final Region region) {
        super(name, region);
        this.setGlobalFlag(AreaFlag.BLOCK_BREAK, false);
        this.setGlobalFlag(AreaFlag.BLOCK_PLACE, false);
        this.setGlobalFlag(AreaFlag.PLAYER_GETDAMAGE, false);
        //Wierd call, isn't it?
        this.updateStart();
    }

    /**
     * Location of lobby spawn.
     */
    private Location lobbySpawn;
    private TaskHandler taskId        = null;
    /**
     * How often should lobby check for players.
     */
    private int     checkInterval = 20; //40 ticks = 2 second.
    /**
     * The minimal Y coordinate value, after the lobby will teleport players to its spawn.
     */
    private int      thresholdY    = 50;

    /**
     * Returns lobby spawn.
     *
     * @return spawn
     */
    public Location getSpawn() {
        return this.lobbySpawn;
    }

    /**
     * Sets lobby spawn.
     *
     * @param location
     */
    public void setSpawn(final Location location) {
        this.lobbySpawn = location;
    }

    /**
     * Updates players. Adds potion effects and teleports them if needed.
     */
    private void updatePlayers() {
        for (Player player : this.getRegion().getPlayersXZ()) {
            //Lobby potion enhantsments.
            player.addEffect(Effect.getEffect(Effect.SPEED).setDuration(20 * 30).setAmplifier(2).setVisible(false));
            player.addEffect(Effect.getEffect(Effect.NIGHT_VISION).setDuration(20 * 30).setAmplifier(1).setVisible(false));

            //In-void lobby teleport.
            if (player.getLocation().getY() < this.thresholdY)
                player.teleport(this.lobbySpawn);
        }
    }

    @Override
    public void updateStart() {
        UpdatedParts.registerPart(this);
        this.taskId = Server.getInstance().getScheduler().scheduleRepeatingTask(ElissaMC.plugin, new NukkitRunnable() {
            @Override
            public void run() {
                Lobby.this.updatePlayers();
            }
        }, this.checkInterval, true);
    }

    @Override
    public void updateStop() {
        taskId.cancel();
    }

    /**
     * @return the thresholdY
     */
    public int getThresholdY() {
        return this.thresholdY;
    }

    /**
     * @return the checkInterval
     */
    public long getCheckInterval() {
        return this.checkInterval;
    }

    /**
     * @param checkInterval
     *            the checkInterval to set
     */
    public void setCheckInterval(final int checkInterval) {
        this.checkInterval = checkInterval;
    }

    /**
     * @param thresholdY
     *            the thresholdY to set
     */
    public void setThresholdY(final int thresholdY) {
        this.thresholdY = thresholdY;
    }
}