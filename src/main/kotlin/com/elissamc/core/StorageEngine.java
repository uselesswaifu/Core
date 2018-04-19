package com.elissamc.core;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import com.elissamc.area.AreaFlag;
import com.elissamc.area.Lobby;
import com.elissamc.area.ProtectedArea;
import com.elissamc.games.Minigame;
import jline.internal.Log;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StorageEngine {
    private static final Map<UUID, PlayerProfile> profiles = new HashMap<>();
    public static final Map<String, Minigame> minigames = new HashMap<>();
    private static final Map<String, ProtectedArea> areas = new HashMap<>();
    private static final Map<String, Class<?>> aliases = new HashMap<String, Class<?>>();
    private static final Map<String, Lobby> lobbies = new HashMap<String, Lobby>();
    private static final Map<String, TeleportGate> gates = new HashMap<String, TeleportGate>();
    private static boolean initialized = false;

    public static void initialize() {
        if (!StorageEngine.initialized)
            StorageEngine.initialized = true;
    }

    /**
     * Returns UUIDs of player's friends.
     *
     * @param player player
     * @return lsit of friends
     */
    public static List<UUID> getFriends(final Player player) {
        return StorageEngine.profiles.get(player.getUniqueId()).getFriends();
    }

    /**
     * Returns UUIDs of player's foes.
     *
     * @param player
     * @return
     */
    public static List<UUID> getFoes(final Player player) {
        return StorageEngine.profiles.get(player.getUniqueId()).getFoes();
    }

    /**
     * Returns map of areas.
     *
     * @return
     */
    public static Map<String, ProtectedArea> getAreas() {
        return StorageEngine.areas;
    }

    /**
     * Returns profile of specified player.
     *
     * @param player
     * @return
     */
    public static PlayerProfile getProfile(final UUID player) {
        return profiles.get(player);
    }

    /**
     * Returns minigame by its name.
     *
     * @param name name of minigame
     * @return minigame object
     */
    public static Minigame getMinigame(final String name) {
        return StorageEngine.minigames.get(name);
    }

    /**
     * Registers minigame.
     *
     * @param minigame
     */
    public static void addMinigame(final Minigame minigame) {
        StorageEngine.minigames.put(minigame.getName(), minigame);
    }

    protected static Map<String, Minigame> getMinigames() {
        return StorageEngine.minigames;
    }


    public static void addGate(final String name, final TeleportGate gate) {
        StorageEngine.gates.put(name, gate);
    }

    public static TeleportGate getGate(final String name) {
        return StorageEngine.gates.get(name);
    }

    public static void removeGate(final String name) {
        StorageEngine.gates.remove(name);
    }

    @SuppressWarnings("rawtypes")
    public static void registerArenaAlias(final Class arenaClass, final String alias) {
        StorageEngine.aliases.put(alias, arenaClass);
    }

    @SuppressWarnings("rawtypes")
    public static Class getByAlias(final String alias) {
        return StorageEngine.aliases.get(alias);
    }

    public static Map<String, Class<?>> getAliases() {
        return StorageEngine.aliases;
    }

    public static void addLobby(final Lobby lobby) {
        StorageEngine.lobbies.put(lobby.getName(), lobby);
        StorageEngine.areas.put(lobby.getName(), lobby);
    }

    public static Lobby getLobby(final String lobbyName) {
        return StorageEngine.lobbies.get(lobbyName);
    }

    /**
     * Saves player's profile to file.
     *
     * @param uniqueId
     */
    public static void saveProfile(final UUID uniqueId) {
        Log.info("Saving profile for " + uniqueId.toString() + " to disk...");
        StorageEngine.profiles.get(uniqueId).save(Paths.playerProfile(uniqueId));
    }

    /**
     * Loads player profile from disk or creates an empty one.
     *
     * @param uniqueId
     */
    public static void loadProfile(final UUID uniqueId) {
        File f = new File(Paths.playerProfile(uniqueId));
        if (f.exists()) {
            Log.info("Load profile for " + uniqueId + "...");
            StorageEngine.profiles.put(uniqueId,
                    PlayerProfile.load(Paths.playerProfile(uniqueId)));
        } else {
            Log.info("Creating new profile for " + uniqueId.toString());
            StorageEngine.profiles.put(uniqueId, new PlayerProfile(uniqueId));
        }
    }

    public static void saveData() {
        Log.info("Saving data...");
        // Save lobbies.
        Config yaml_lobbies = new Config(Config.YAML);
        int i_lobbies = 0;
        for (Lobby l : StorageEngine.lobbies.values()) {
            yaml_lobbies.set("lobbies.lobby" + i_lobbies + ".name", l.getName());
            yaml_lobbies.set("lobbies.lobby" + i_lobbies + ".checkinterval",
                    l.getCheckInterval());

            // Save global flags
            for (AreaFlag flag : AreaFlag.values())
                if (l.getGlobalFlag(flag) != ProtectedArea.defaultFlags.get(flag))
                    yaml_lobbies.set(
                            "lobbies.lobby" + i_lobbies + ".gflags." + flag.toString(),
                            l.getGlobalFlag(flag));

            yaml_lobbies.set("lobbies.lobby" + i_lobbies + ".thresholdY",
                    l.getThresholdY());
            l.getRegion().serialize(yaml_lobbies,
                    "lobbies.lobby" + i_lobbies + ".region");
            i_lobbies++;
        }
        yaml_lobbies.save(new File(Paths.lobbiesPath()));
        Log.info("Saved " + i_lobbies + " lobbies!");

        // Save gates
        Config yaml_gates = new Config(Config.YAML);
        int i_gates = 0;
        for (String key : StorageEngine.gates.keySet()) {
            TeleportGate tg = StorageEngine.gates.get(key);
            yaml_gates.set("gates.gate" + i_gates + ".name", key);
            yaml_gates.set("gates.gate" + i_gates + ".action.type",
                    tg.getAction().getClass().getSimpleName());
            if (tg.getRegion() != null)
                tg.getRegion().serialize(yaml_gates, "gates.gate" + i_gates + ".region");
            i_gates++;
        }
        yaml_gates.save(new File(Paths.gatesPath()));
        Log.info("Saved " + i_gates + " gates!");
    }

    public static void loadData() {
        Log.info("Loading data...");

    }

    public static void gateEnter(final Player player, final Location location) {
        // Find the right gate
        for (TeleportGate gate : StorageEngine.gates.values())
            if (gate.getRegion().intersects(location))
                gate.teleport(player);
    }

    public static void saveProfiles() {
        for (PlayerProfile profile : StorageEngine.profiles.values()) {
            profile.save(Paths.playerProfile(profile.getUniqueId()));
        }
        Log.info("Saved Player Profiles!");
    }
}