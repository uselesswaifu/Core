package com.elissamc.core

import cn.nukkit.Player
import com.elissamc.chat.ChatFormat

import java.util.ArrayList

/** Class used for party. */
class Party
/**
 * Creates new party.
 *
 * @param owner
 * owner of the party
 */
(private val owner: Player) : PlayerHolder {
    override val players: MutableList<Player>

    override val playerCount: Int
        get() = this.players.size

    init {
        this.players = ArrayList()

        owner.sendMessage(ChatFormat.success("Created new party!"))
    }

    /**
     * Returns whether is specified player owner of the party.
     *
     * @param player
     * specified player
     * @return true or false
     */
    fun isOwner(player: Player): Boolean {
        return this.owner === player
    }

    /**
     * Adds player to party.
     *
     * @param player
     * player to be added
     */
    fun addPlayer(player: Player) {
        this.players.add(player)
        player.sendMessage(ChatFormat.success("You have joined " + this.owner.displayName + "'s party!"))
    }

    fun removePlayer(player: Player) {
        this.players.remove(player)
        StorageEngine.getProfile(player.uniqueId).party = null
    }

    override fun contains(player: Player): Boolean {
        return this.players.contains(player)
    }
}