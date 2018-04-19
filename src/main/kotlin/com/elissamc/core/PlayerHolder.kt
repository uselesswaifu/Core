package com.elissamc.core

import cn.nukkit.Player

/** Specifies class that hold collection of players. */
interface PlayerHolder {

    val players: Collection<Player>

    val playerCount: Int

    operator fun contains(player: Player): Boolean
}