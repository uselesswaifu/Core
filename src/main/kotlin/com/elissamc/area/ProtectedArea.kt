package com.elissamc.area

import com.elissamc.core.Region
import java.util.*

/** Used for handling protected areas. */
open class ProtectedArea
/**
 * Creates new area with specified name.
 *
 * @param name
 */
internal constructor(
        /**
         * Name of area.
         */
        /**
         * Returns area name.
         *
         * @return name of area
         */
        val name: String,
        /**
         * Area region
         */
        /**
         * Returns area region.
         *
         * @return region
         */
        val region: Region) {
    /**
     * Global area flags.
     */
    private val globalFlags = HashMap<AreaFlag, Boolean>()
    /**
     * Player area flags.
     */
    private val playerFlags = HashMap<UUID, HashMap<AreaFlag, Boolean>>()
    /**
     * Permission area flags.
     */
    private val permissionFlags = HashMap<String, HashMap<AreaFlag, Boolean>>()
    /**
     * Owner of area.
     */
    /**
     * Returns area owner.
     *
     * @return owner of area
     */
    val owner: AreaOwner? = null

    /**
     * Returns value of global flag. If not specified uses parent's flag (default).
     *
     * @param flag
     */
    fun getGlobalFlag(flag: AreaFlag): Boolean? {
        return if (this.globalFlags[flag] == null)
            if (ProtectedArea.defaultFlags[flag] == null)
                false
            else
                ProtectedArea.defaultFlags[flag]
        else
            this.globalFlags[flag]

    }

    /**
     * Returns value of player flag. If not specified uses parent's flag (global).
     *
     * @param flag
     * @param player
     */
    fun getPlayerFlag(flag: AreaFlag, player: UUID): Boolean? {
        return if (this.playerFlags.containsKey(player))
            if (this.playerFlags[player]?.get(flag) == null)
                if (this.globalFlags[flag] == null)
                    if (ProtectedArea.defaultFlags[flag] == null)
                        false
                    else
                        ProtectedArea.defaultFlags[flag]
                else
                    this.globalFlags[flag]
            else
                this.playerFlags[player]?.get(flag)
        else {
            if (this.globalFlags[flag] == null)
                if (ProtectedArea.defaultFlags[flag] == null)
                    false
                else
                    ProtectedArea.defaultFlags[flag]
            else
                this.globalFlags[flag]
        }

    }

    /**
     * Sets global flag.
     *
     * @param flag  flag to set
     * @param value value to set
     */
    fun setGlobalFlag(flag: AreaFlag, value: Boolean) {
        this.globalFlags[flag] = value
    }

    /**
     * Sets player flag.
     *
     * @param flag
     * @param value
     * @param player
     */
    fun setPlayerFlag(flag: AreaFlag, value: Boolean, player: UUID) {
        if (this.playerFlags.containsKey(player))
            this.playerFlags[player]?.set(flag, value)
        else {
            this.playerFlags[player] = HashMap()
            this.playerFlags[player]?.set(flag, value)
        }
    }

    companion object {
        /**
         * Map of default values for flags.
         */
        val defaultFlags: MutableMap<AreaFlag, Boolean> = HashMap()

        //Initialization of static values.
        init {
            ProtectedArea.defaultFlags[AreaFlag.BLOCK_BREAK] = false
            ProtectedArea.defaultFlags[AreaFlag.BLOCK_PLACE] = false
            ProtectedArea.defaultFlags[AreaFlag.PLAYER_GETDAMAGE] = false
            ProtectedArea.defaultFlags[AreaFlag.PLAYER_DODAMAGE] = false
            ProtectedArea.defaultFlags[AreaFlag.PLAYER_DROPITEM] = false
            ProtectedArea.defaultFlags[AreaFlag.PLAYER_STARVATION] = false
            ProtectedArea.defaultFlags[AreaFlag.AREA_CHAT_GOODBYE] = true
            ProtectedArea.defaultFlags[AreaFlag.AREA_CHAT_PERMISSIONDENIED] = true
            ProtectedArea.defaultFlags[AreaFlag.AREA_CHAT_WELCOME] = true
        }
    }
}