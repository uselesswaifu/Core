package com.elissamc.actions


import cn.nukkit.Player
import cn.nukkit.level.Location


/**
 * Inventory action that teleports player to specified location.
 *
 */
class TeleportAction
/**
 * Creates a new action.
 *
 * @param location
 */
(
        /**
         * Target location.
         */
        private val location: Location) : Action {

    override fun execute(player: Player) {
        // Just teleport player to target location.
        player.teleport(this.location)
    }
}