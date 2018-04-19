package com.elissamc.core

import cn.nukkit.Player
import com.elissamc.actions.Action

/**
 * Class representating teleport portal.
 *
 */
class TeleportGate
/**
 * Creates new teleport gate in specified region, with specified action that will be executed when PlayerPortalEvent
 * is thrown for this gate.
 *
 * @param region region of gate
 * @param action action to be executed
 */
(
        /**
         * Retruns region of gate.
         *
         * @return Return's region
         */
        internal val region: Region,
        /**
         * Returns action of this gate.
         *
         * @return action
         */
        var action: Action?) {

    /**
     * Executes specified action on specified player.
     *
     * @param player player to execute action to
     */
    internal fun teleport(player: Player) {
        this.action!!.execute(player)
    }
}