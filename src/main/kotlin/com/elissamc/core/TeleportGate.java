package com.elissamc.core;

import cn.nukkit.Player;
import com.elissamc.actions.Action;

/**
 * Class representating teleport portal.
 *
 * @author Mato Kormuth
 */
public class TeleportGate {
    private final Region region;
    private Action action;

    /**
     * Creates new teleport gate in specified region, with specified action that will be executed when PlayerPortalEvent
     * is thrown for this gate.
     *
     * @param region region of gate
     * @param action action to be executed
     */
    public TeleportGate(final Region region, final Action action) {
        this.region = region;
        this.action = action;
    }

    /**
     * Retruns region of gate.
     *
     * @return Return's region
     */
    Region getRegion() {
        return this.region;
    }

    /**
     * Executes specified action on specified player.
     *
     * @param player player to execute action to
     */
    void teleport(final Player player) {
        this.action.execute(player);
    }

    /**
     * Returns action of this gate.
     *
     * @return action
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * Sets action to this teleport gate.
     *
     * @param action action
     */
    public void setAction(final Action action) {
        this.action = action;
    }
}