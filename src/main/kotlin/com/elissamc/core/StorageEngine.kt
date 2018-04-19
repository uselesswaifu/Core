package com.elissamc.core

import cn.nukkit.Player
import cn.nukkit.level.Location
import cn.nukkit.utils.Config
import com.elissamc.area.AreaFlag
import com.elissamc.area.Lobby
import com.elissamc.area.ProtectedArea
import com.elissamc.games.Minigame
import jline.internal.Log
import java.io.File
import java.util.*

object StorageEngine {
    private val profiles = HashMap<UUID, PlayerProfile>()
    val minigames: MutableMap<String, Minigame> = HashMap()
    private val areas = HashMap<String, ProtectedArea>()
    private val aliases = HashMap<String, Class<*>>()
    private val lobbies = HashMap<String, Lobby>()
    private val gates = HashMap<String, TeleportGate>()
    private var initialized = false

    fun initialize() {
        if (!StorageEngine.initialized)
            StorageEngine.initialized = true
    }

    /**
     * Returns UUIDs of player's friends.
     *
     * @param player player
     * @return lsit of friends
     */
    fun getFriends(player: Player): List<UUID> {
        return StorageEngine.profiles[player.uniqueId]!!.getFriends()
    }

    /**
     * Returns UUIDs of player's foes.
     *
     * @param player
     * @return
     */
    fun getFoes(player: Player): List<UUID> {
        return StorageEngine.profiles[player.uniqueId]!!.getFoes()
    }

    /**
     * Returns map of areas.
     *
     * @return
     */
    fun getAreas(): Map<String, ProtectedArea> {
        return StorageEngine.areas
    }

    /**
     * Returns profile of specified player.
     *
     * @param player
     * @return
     */
    fun getProfile(player: UUID): PlayerProfile {
        return profiles[player]!!
    }

    /**
     * Returns minigame by its name.
     *
     * @param name name of minigame
     * @return minigame object
     */
    fun getMinigame(name: String): Minigame? {
        return StorageEngine.minigames[name]
    }

    /**
     * Registers minigame.
     *
     * @param minigame
     */
    fun addMinigame(minigame: Minigame) {
        StorageEngine.minigames[minigame.getName()] = minigame
    }

    internal fun getMinigames(): Map<String, Minigame> {
        return StorageEngine.minigames
    }


    fun addGate(name: String, gate: TeleportGate) {
        StorageEngine.gates[name] = gate
    }

    fun getGate(name: String): TeleportGate? {
        return StorageEngine.gates[name]
    }

    fun removeGate(name: String) {
        StorageEngine.gates.remove(name)
    }

    fun addLobby(lobby: Lobby) {
        StorageEngine.lobbies[lobby.name] = lobby
        StorageEngine.areas[lobby.name] = lobby
    }

    fun getLobby(lobbyName: String): Lobby {
        if (StorageEngine.lobbies.containsKey(lobbyName))
            return StorageEngine.lobbies.getValue(lobbyName)
        throw Exception()
    }

    /**
     * Saves player's profile to file.
     *
     * @param uniqueId
     */
    fun saveProfile(uniqueId: UUID) {
        Log.info("Saving profile for " + uniqueId.toString() + " to disk...")
        StorageEngine.profiles[uniqueId]!!.save(Paths.playerProfile(uniqueId))
    }

    /**
     * Loads player profile from disk or creates an empty one.
     *
     * @param uniqueId
     */
    fun loadProfile(uniqueId: UUID) {
        val f = File(Paths.playerProfile(uniqueId))
        if (f.exists()) {
            Log.info("Load profile for $uniqueId...")
            StorageEngine.profiles[uniqueId] = PlayerProfile.load(Paths.playerProfile(uniqueId))
        } else {
            Log.info("Creating new profile for " + uniqueId.toString())
            StorageEngine.profiles[uniqueId] = PlayerProfile(uniqueId)
        }
    }

    fun saveData() {
        Log.info("Saving data...")
        // Save lobbies.
        val yaml_lobbies = Config(Config.YAML)
        var i_lobbies = 0
        for (l in StorageEngine.lobbies.values) {
            yaml_lobbies.set("lobbies.lobby$i_lobbies.name", l.name)
            yaml_lobbies.set("lobbies.lobby$i_lobbies.checkinterval",
                    l.checkInterval)

            // Save global flags
            for (flag in AreaFlag.values())
                if (l.getGlobalFlag(flag) !== ProtectedArea.defaultFlags[flag])
                    yaml_lobbies.set(
                            "lobbies.lobby" + i_lobbies + ".gflags." + flag.toString(),
                            l.getGlobalFlag(flag))

            yaml_lobbies.set("lobbies.lobby$i_lobbies.thresholdY",
                    l.thresholdY)
            l.region.serialize(yaml_lobbies,
                    "lobbies.lobby$i_lobbies.region")
            i_lobbies++
        }
        yaml_lobbies.save(File(Paths.lobbiesPath()))
        Log.info("Saved $i_lobbies lobbies!")

        // Save gates
        val yaml_gates = Config(Config.YAML)
        var i_gates = 0
        for (key in StorageEngine.gates.keys) {
            val tg = StorageEngine.gates[key]
            yaml_gates.set("gates.gate$i_gates.name", key)
            if (tg != null) {
                yaml_gates.set("gates.gate$i_gates.action.type",
                        tg.action!!.javaClass.simpleName)
            }
            tg?.region?.serialize(yaml_gates, "gates.gate$i_gates.region")
            i_gates++
        }
        yaml_gates.save(File(Paths.gatesPath()))
        Log.info("Saved $i_gates gates!")
    }

    fun loadData() {
        Log.info("Loading data...")

    }

    fun gateEnter(player: Player, location: Location) {
        // Find the right gate
        for (gate in StorageEngine.gates.values)
            if (gate.region.intersects(location))
                gate.teleport(player)
    }

    fun saveProfiles() {
        for (profile in StorageEngine.profiles.values) {
            profile.save(Paths.playerProfile(profile.uniqueId))
        }
        Log.info("Saved Player Profiles!")
    }
}