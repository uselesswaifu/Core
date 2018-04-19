package com.elissamc.core;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import com.google.common.collect.ImmutableList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import java.io.File;
import java.util.*;

public class PlayerProfile {
    /**
     * Player's UUID.
     */
    @XmlID
    @XmlAttribute
    protected final UUID uuid;
    /**
     * Player's friends.
     */
    @XmlAttribute
    protected final List<UUID> friends = new ArrayList<UUID>();
    /**
     * Player's foes.
     */
    @XmlAttribute
    protected final List<UUID> foes = new ArrayList<UUID>();
    /**
     * Player's settings.
     */
    @XmlAttribute
    protected final Map<Settings, Boolean> settings = new HashMap<Settings, Boolean>();

    /**
     * Spectating status.
     */
    protected transient boolean spectating = false;

    /**
     * Last known name of this player.
     */
    @XmlAttribute
    protected String lastKnownName = "";
    /**
     * Amount of player's points (probably in-game currency or whatever).
     */
    @XmlAttribute
    protected int points = 0;
    @XmlAttribute
    protected int warnCount = 0;

    /**
     * Represents party, that player is currently in. If is player not in any party, it is null.
     */
    protected Party party = new Party(null);

    /**
     * Creates player profile from UUID.
     *
     * @param player
     */
    PlayerProfile(final UUID player) {
        this.uuid = player;
        for(Player p : Server.getInstance().getOnlinePlayers().values()){
            if(p.getUniqueId() == player)
                this.lastKnownName = p.getName();
        }
    }

    /**
     * Adds friend.
     *
     * @param player
     */
    public void addFriend(final UUID player) {
        this.friends.add(player);
    }

    /**
     * Removes friend.
     *
     * @param player
     */
    public void removeFriend(final UUID player) {
        this.friends.remove(player);
    }

    public void addFoe(final UUID player) {
        this.foes.add(player);
    }

    public void removeFoe(final UUID player) {
        this.foes.remove(player);
    }

    /**
     * Returns UUID of profile.
     *
     * @return
     */
    UUID getUniqueId() {
        return this.uuid;
    }

    /**
     * Return list of player's friends.
     *
     * @return
     */
    List<UUID> getFriends() {
        return ImmutableList.copyOf(this.friends);
    }

    List<UUID> getFoes() {
        return ImmutableList.copyOf(this.foes);
    }

    /**
     * Returns whatever is this player friend with the specified.
     *
     * @param uniqueId Friend's UUID.
     * @return Return boolean of whether friends or not.
     */
    public boolean isFriend(final UUID uniqueId) {
        return this.friends.contains(uniqueId);
    }

    public boolean isFoe(final UUID uniqueId) {
        return this.foes.contains(uniqueId);
    }

    /**
     * Returns setting value.
     *
     * @param setting Setting string.
     * @return Returns boolean.
     */
    boolean getSetting(final Settings setting) {
        return this.settings.getOrDefault(setting, true);
    }

    /**
     * Set players settings.
     *
     * @param setting Setting string name.
     * @param value True or False value.
     */
    public void setSetting(final Settings setting, final boolean value) {
        this.settings.put(setting, value);
    }

    /**
     * Return's whether is player spectating.
     *
     * @return
     */
    public boolean isSpectating() {
        return this.spectating;
    }

    /**
     * Sets player's spectating state.
     *
     * @param spectating Set player as spectating.
     */
    public void setSpectating(final boolean spectating) {
        this.spectating = spectating;
    }

    /**
     * Saves player's profile to file.
     *
     * @param path path to save.
     */
    void save(final String path) {
        Config yaml = new Config(path, Config.YAML);

        yaml.set("player.uuid", this.uuid.toString());
        yaml.set("player.points", this.points);
        yaml.set("player.warnCount", this.warnCount);
        yaml.set("player.lastKnownName", this.lastKnownName);
        yaml.set("player.friends", this.friends);
        yaml.set("player.foes", this.foes);

        yaml.save(new File(path));
    }

    /**
     * Loads player profile from specified path.
     *
     * @param path Path to player profile.
     */
    static PlayerProfile load(final String path) {
        Config yaml = new Config(path, Config.YAML);
        UUID uuid = UUID.fromString(yaml.getString("player.uuid"));

        PlayerProfile profile = new PlayerProfile(uuid);

        profile.points = yaml.getInt("player.points");
        profile.lastKnownName = yaml.getString("player.lastKnownName");

        List<?> friends = yaml.getList("player.friends");
        List<?> foes = yaml.getList("player.foes");

        for (Object obj : friends)
            profile.addFriend(UUID.fromString(obj.toString()));
        for (Object obj : foes)
            profile.addFoe(UUID.fromString(obj.toString()));

        return profile;
    }

    /**
     * Return the number of points.
     *
     * @return Returns player's # of points.
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Add specified amount of points.
     *
     * @param points Add points to player.
     */
    public void addPoints(final int points) {
        this.points += points;
    }

    public Party getParty() {
        return this.party;
    }

    public void setParty(final Party party) {
        this.party = party;
    }
}