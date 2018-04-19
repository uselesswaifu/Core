package com.elissamc.core

import cn.nukkit.Server
import cn.nukkit.utils.Config
import com.google.common.collect.ImmutableList
import java.io.File
import java.util.*
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlID

open class PlayerProfile
/**
 * Creates player profile from UUID.
 *
 * @param uniqueId
 */
internal constructor(
        /** Player's UUID. */
        @field:XmlID
        @field:XmlAttribute
        internal val uniqueId: UUID) {

    /** Player's friends. */
    @XmlAttribute
    private val friends: MutableList<UUID> = ArrayList()

    /** Player's foes. */
    @XmlAttribute
    private val foes: MutableList<UUID> = ArrayList()

    /** Player's settings. */
    @XmlAttribute
    private val settings: MutableMap<Settings, Boolean> = HashMap()

    /** Spectating status. */
    @Transient
    var isSpectating = false

    /** Last known name of this player. */
    @XmlAttribute
    protected var lastKnownName = ""

    /** Amount of player's points (probably in-game currency or whatever). */
    @XmlAttribute
    var points = 0
        protected set

    /** Amount of warning's the player has */
    @XmlAttribute
    private var warnCount = 0

    /** Represents party, that player is currently in. If is player not in any party, it is null. */
    var party: Party? = null

    init {
        this.lastKnownName = uniqueId.toString()
        for (p in Server.getInstance().onlinePlayers.values) {
            if (p.uniqueId === uniqueId)
                this.lastKnownName = p.name
        }
    }

    /**
     * Adds friend.
     *
     * @param player
     */
    fun addFriend(player: UUID) {
        this.friends.add(player)
    }

    /**
     * Removes friend.
     *
     * @param player
     */
    fun removeFriend(player: UUID) {
        this.friends.remove(player)
    }

    fun addFoe(player: UUID) {
        this.foes.add(player)
    }

    fun removeFoe(player: UUID) {
        this.foes.remove(player)
    }

    /**
     * Return list of player's friends.
     *
     * @return
     */
    internal fun getFriends(): List<UUID> {
        return ImmutableList.copyOf(this.friends)
    }

    internal fun getFoes(): List<UUID> {
        return ImmutableList.copyOf(this.foes)
    }

    /**
     * Returns whatever is this player friend with the specified.
     *
     * @param uniqueId Friend's UUID.
     * @return Return boolean of whether friends or not.
     */
    fun isFriend(uniqueId: UUID): Boolean {
        return this.friends.contains(uniqueId)
    }

    fun isFoe(uniqueId: UUID): Boolean {
        return this.foes.contains(uniqueId)
    }

    /**
     * Returns setting value.
     *
     * @param setting Setting string.
     * @return Returns boolean.
     */
    internal fun getSetting(setting: Settings): Boolean {
        return (this.settings as Map<Settings, Boolean>).getOrDefault(setting, true)
    }

    /**
     * Set players settings.
     *
     * @param setting Setting string name.
     * @param value True or False value.
     */
    fun setSetting(setting: Settings, value: Boolean) {
        this.settings[setting] = value
    }

    /**
     * Saves player's profile to file.
     *
     * @param path path to save.
     */
    internal fun save(path: String) {
        val yaml = Config(path, Config.YAML)

        val friends = ArrayList<String>()
        this.friends.forEach { uuid -> friends.add(uuid.toString()) }
        val foes = ArrayList<String>()
        this.foes.forEach { uuid -> foes.add(uuid.toString()) }

        yaml.set("player.uuid", this.uniqueId.toString())
        yaml.set("player.points", this.points)
        yaml.set("player.warnCount", this.warnCount)
        yaml.set("player.lastKnownName", this.lastKnownName)
        yaml.set("player.friends", friends)
        yaml.set("player.foes", foes)

        yaml.save(File(path))
    }

    /**
     * Add specified amount of points.
     *
     * @param points Add points to player.
     */
    fun addPoints(points: Int) {
        this.points += points
    }

    companion object {

        /**
         * Loads player profile from specified path.
         *
         * @param path Path to player profile.
         */
        internal fun load(path: String): PlayerProfile {
            val yaml = Config(path, Config.YAML)
            val uuid = UUID.fromString(yaml.getString("player.uuid"))

            val profile = PlayerProfile(uuid)

            profile.points = yaml.getInt("player.points")
            profile.lastKnownName = yaml.getString("player.lastKnownName")

            val friends = yaml.getStringList("player.friends")
            val foes = yaml.getStringList("player.foes")

            for (obj in friends)
                profile.addFriend(UUID.fromString(obj.toString()))
            for (obj in foes)
                profile.addFoe(UUID.fromString(obj.toString()))

            return profile
        }
    }
}