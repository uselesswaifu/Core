// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform.
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 *
 * This file is part of Pexel.
 *
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
package com.elissamc.core;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import com.elissamc.ElissaMC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used for regions.
 *
 * @author Mato Kormuth
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Region {
    /**
     * First vector.
     */
    @XmlElement(name = "vector1")
    protected final Vector3 v1;
    /**
     * Second vector.
     */
    @XmlElement(name = "vector2")
    protected final Vector3 v2;
    /**
     * World of region.
     */
    @XmlTransient
    protected final Level w;

    @XmlElement(name = "world")
    protected final String w_name;

    /**
     * Creates a new region from two locations.
     *
     * @param loc1 first location
     * @param loc2 second location
     */
    public Region(final Location loc1, final Location loc2) {
        this.v1 = loc1;
        this.v2 = loc2;
        this.w = loc1.getLevel();
        this.w_name = loc1.getLevel().getName();
    }

    /**
     * Creates a new region from two vectors and one world. Similar to {@link Region#Region(Location, Location)}
     *
     * @param v1 minimum point
     * @param v2 maximum point
     * @param w  world
     */
    public Region(final Vector3 v1, final Vector3 v2, final Level w) throws Exception {
        if(w == null)
            throw new Exception();
        this.v1 = v1;
        this.v2 = v2;
        this.w = w;
        this.w_name = w.getName();
    }

    /**
     * Creates a new region with center and size.
     *
     * @param center center
     * @param w      world
     * @param size   size
     */
    public Region(final Vector3 center, final Level w, final int size) throws Exception {
        if(w == null)
            throw new Exception();
        this.v1 =
                center.clone().add(size, size, size);
        this.v2 = center.clone().add(
                -size, -size, -size);
        this.w = w;
        this.w_name = w.getName();
    }

    /**
     * Creates a new region from WorldEdit selection.
     *
     * @param selection worldedit selection
     */
    public Region(final Region selection) {
//        selection = selection.getSelection()
        this.v1 = new Vector3(selection.getMinX(), selection.getMinY(), selection.getMinZ());
        this.v2 = new Vector3(selection.getMaxX(), selection.getMaxY(), selection.getMaxZ());
        this.w = selection.getWorld();
        this.w_name = w.getName();
    }

    /**
     * Returns whether the location intersect the region.
     *
     * @param loc location to check.
     * @return
     */
    public boolean intersects(final Location loc) {
        if (this.w.getName().equals(loc.getLevel().getName()))
            return this.intersects(loc.asBlockVector3());
        else
            return false;
    }

    /**
     * Returns whether the location intersects X and Z coordinate of this region.
     *
     * @param loc location to check
     * @return
     */
    public boolean intersectsXZ(final Location loc) {
        if (this.w.getName().equals(loc.getLevel().getName()))
            return this.intersectsXZ(loc.asBlockVector3());
        else
            return false;
    }

    /**
     * Returns whatever vector intersects the region.
     *
     * @param v vector to check
     * @return
     */
    public boolean intersects(final BlockVector3 v) {
        return Region.range(this.v1.getX(), this.v2.getX(), v.getX())
                && Region.range(this.v1.getY(), this.v2.getY(), v.getY())
                && Region.range(this.v1.getZ(), this.v2.getZ(), v.getZ());
    }

    public boolean intersectsXZ(final BlockVector3 v) {
        return Region.range(this.v1.getX(), this.v2.getX(), v.getX())
                && Region.range(this.v1.getZ(), this.v2.getZ(), v.getZ());
    }

    /**
     * Returns players in region.
     *
     * @return list of players
     */
    public List<Player> getPlayersXYZ() {
        List<Player> players = new ArrayList<Player>();
        for (Player player : Server.getInstance().getOnlinePlayers().values())
            if (this.intersects(player.getLocation()))
                players.add(player);
        return players;
    }

    public void serialize(final Config yaml, final String string) {
        yaml.set(string + ".v1.x", this.v1.getX());
        yaml.set(string + ".v1.y", this.v1.getY());
        yaml.set(string + ".v1.z", this.v1.getZ());

        yaml.set(string + ".v2.x", this.v2.getX());
        yaml.set(string + ".v2.y", this.v2.getY());
        yaml.set(string + ".v2.z", this.v2.getZ());

        yaml.set(string + ".world", this.w.getName());
    }

    private final static boolean range(final double min, final double max,
                                       final double value) {
        if (max > min)
            return (value <= max ? (value >= min ? true : false) : false);
        else
            return (value <= min ? (value >= max ? true : false) : false);
    }

    /**
     * Retruns first vector.
     *
     * @return vector
     */
    public Location getV1Loaction() {
        return new Location(this.v1.getX(), this.v1.getY(), this.v1.getZ(), this.w);
    }

    /**
     * Returns second vector.
     *
     * @return vector
     */
    public Location getV2Location() {
        return new Location(this.v2.getX(), this.v2.getY(), this.v2.getZ(), this.w);
    }

    @Override
    public String toString() {
        return "Region{x1:" + this.v1.getX() + ",y1:" + this.v1.getY()
                + ",z1:" + this.v1.getZ() + ",x2:" + this.v2.getX() + ",y2:"
                + this.v2.getY() + ",z2:" + this.v2.getZ() + ",world:"
                + this.w.getName() + "}";
    }

    /**
     * Returns players in XZ region.
     *
     * @return list of players.
     */
    public List<Player> getPlayersXZ() {
        List<Player> players = new ArrayList<Player>();
        for (Player player : Server.getInstance().getOnlinePlayers().values())
            if (this.intersectsXZ(player.getLocation()))
                players.add(player);
        return players;
    }

    public double getMaxX() {
        if (this.v1.getX() > this.v2.getX())
            return this.v1.getX();
        else
            return this.v2.getX();
    }

    public double getMaxY() {
        if (this.v1.getY() > this.v2.getY())
            return this.v1.getY();
        else
            return this.v2.getY();
    }

    public double getMaxZ() {
        if (this.v1.getZ() > this.v2.getZ())
            return this.v1.getZ();
        else
            return this.v2.getZ();
    }

    public double getMinX() {
        if (this.v1.getX() > this.v2.getX())
            return this.v2.getX();
        else
            return this.v1.getX();
    }

    public double getMinY() {
        if (this.v1.getY() > this.v2.getY())
            return this.v2.getY();
        else
            return this.v1.getY();
    }

    public double getMinZ() {
        if (this.v1.getZ() > this.v2.getZ())
            return this.v2.getZ();
        else
            return this.v1.getZ();
    }

    public Level getWorld() {
        return this.w;
    }

    /**
     * Returns random location from this region.
     *
     * @return random location in bounds of this region
     */
    public Location getRandomLocation() {
        int a = this.rnd((int) (this.getMaxX() - this.getMinX()));
        int b = this.rnd((int) (this.getMaxY() - this.getMinY()));
        int c = this.rnd((int) (this.getMaxZ() - this.getMinZ()));
        return new Location(this.getMinX() + a, this.getMinY() + b,
                this.getMinZ() + c, this.w);
    }

    private int rnd(final int max) {
        if (max == 0) {
            return 0;
        } else {
            return ElissaMC.core.getRandom().nextInt(max);
        }
    }

    /**
     * Returns list of blocks in this region. <b>Notice: can be slow on big regions.</b>
     *
     * @return list of region's blocks
     */
    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<Block>(500);
        int maxX = (int) this.getMaxX();
        int maxY = (int) this.getMaxY();
        int maxZ = (int) this.getMaxZ();
        for (int x = (int) this.getMinX(); x <= maxX; x++) {
            for (int y = (int) this.getMinY(); y <= maxY; y++) {
                for (int z = (int) this.getMinZ(); z <= maxZ; z++) {
                    blocks.add(this.w.getBlock(x, y, z));
                }
            }
        }
        return blocks;
    }

    /**
     * Creates region around specified location with specified size.
     *
     * @param center center
     * @param size   size
     * @return region
     */
    public static Region createAroundBox(final Location center, final int size) {
        try {
            return new Region(center.add(size, size, size),
                    center.subtract(size, size, size),
                    center.getLevel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     */
    public double getWidth() {
        return this.getMaxX() - this.getMinX();
    }

    public double getHeight() {
        return this.getMaxY() - this.getMinY();
    }

    public double getLength() {
        return this.getMaxZ() - this.getMinZ();
    }
}