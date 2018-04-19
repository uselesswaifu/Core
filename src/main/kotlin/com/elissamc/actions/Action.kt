package com.elissamc.actions

import cn.nukkit.Player

/**
 * Class that represents actions.
 */
interface Action {
    /**
     * Called when action should be executed on the player.
     *
     * @param player
     * executor
     */
    fun execute(player: Player)
}