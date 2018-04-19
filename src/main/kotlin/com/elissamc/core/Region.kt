package com.elissamc.core

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.Block
import cn.nukkit.level.Level
import cn.nukkit.level.Location
import cn.nukkit.math.BlockVector3
import cn.nukkit.math.Vector3
import cn.nukkit.utils.Config
import com.elissamc.ElissaMC

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlTransient
import java.util.ArrayList

/** Class used for regions. */
@XmlAccessorType(XmlAccessType.FIELD)
class Region {
    /**
     * First vector.
     */
    @XmlElement(name = "vector1")
    protected val v1: Vector3
    /**
     * Second vector.
     */
    @XmlElement(name = "vector2")
    protected val v2: Vector3
    /**
     * World of region.
     */
    @XmlTransient
    val world: Level

    @XmlElement(name = "world")
    protected val w_name: String

    /**
     * Returns players in region.
     *
     * @return list of players
     */
    val playersXYZ: List<Player>
        get() {
            val players = ArrayList<Player>()
            for (player in Server.getInstance().onlinePlayers.values)
                if (this.intersects(player.location))
                    players.add(player)
            return players
        }

    /**
     * Retruns first vector.
     *
     * @return vector
     */
    val v1Loaction: Location
        get() = Location(this.v1.getX(), this.v1.getY(), this.v1.getZ(), this.world)

    /**
     * Returns second vector.
     *
     * @return vector
     */
    val v2Location: Location
        get() = Location(this.v2.getX(), this.v2.getY(), this.v2.getZ(), this.world)

    /**
     * Returns players in XZ region.
     *
     * @return list of players.
     */
    val playersXZ: List<Player>
        get() {
            val players = ArrayList<Player>()
            for (player in Server.getInstance().onlinePlayers.values)
                if (this.intersectsXZ(player.location))
                    players.add(player)
            return players
        }

    val maxX: Double
        get() = if (this.v1.getX() > this.v2.getX())
            this.v1.getX()
        else
            this.v2.getX()

    val maxY: Double
        get() = if (this.v1.getY() > this.v2.getY())
            this.v1.getY()
        else
            this.v2.getY()

    val maxZ: Double
        get() = if (this.v1.getZ() > this.v2.getZ())
            this.v1.getZ()
        else
            this.v2.getZ()

    val minX: Double
        get() = if (this.v1.getX() > this.v2.getX())
            this.v2.getX()
        else
            this.v1.getX()

    val minY: Double
        get() = if (this.v1.getY() > this.v2.getY())
            this.v2.getY()
        else
            this.v1.getY()

    val minZ: Double
        get() = if (this.v1.getZ() > this.v2.getZ())
            this.v2.getZ()
        else
            this.v1.getZ()

    /**
     * Returns random location from this region.
     *
     * @return random location in bounds of this region
     */
    val randomLocation: Location
        get() {
            val a = this.rnd((this.maxX - this.minX).toInt())
            val b = this.rnd((this.maxY - this.minY).toInt())
            val c = this.rnd((this.maxZ - this.minZ).toInt())
            return Location(this.minX + a, this.minY + b,
                    this.minZ + c, this.world)
        }

    /**
     * Returns list of blocks in this region. **Notice: can be slow on big regions.**
     *
     * @return list of region's blocks
     */
    val blocks: List<Block>
        get() {
            val blocks = ArrayList<Block>(500)
            val maxX = this.maxX.toInt()
            val maxY = this.maxY.toInt()
            val maxZ = this.maxZ.toInt()
            for (x in this.minX.toInt()..maxX) {
                for (y in this.minY.toInt()..maxY) {
                    for (z in this.minZ.toInt()..maxZ) {
                        blocks.add(this.world.getBlock(x, y, z))
                    }
                }
            }
            return blocks
        }

    /**
     * @return
     */
    val width: Double
        get() = this.maxX - this.minX

    val height: Double
        get() = this.maxY - this.minY

    val length: Double
        get() = this.maxZ - this.minZ

    /**
     * Creates a new region from two locations.
     *
     * @param loc1 first location
     * @param loc2 second location
     */
    constructor(loc1: Location, loc2: Location) {
        this.v1 = loc1
        this.v2 = loc2
        this.world = loc1.getLevel()
        this.w_name = loc1.getLevel().name
    }

    /**
     * Creates a new region from two vectors and one world. Similar to [Region()]
     *
     * @param v1 minimum point
     * @param v2 maximum point
     * @param w  world
     */
    @Throws(Exception::class)
    constructor(v1: Vector3, v2: Vector3, w: Level?) {
        if (w == null)
            throw Exception()
        this.v1 = v1
        this.v2 = v2
        this.world = w
        this.w_name = w.name
    }

    /**
     * Creates a new region with center and size.
     *
     * @param center center
     * @param w      world
     * @param size   size
     */
    @Throws(Exception::class)
    constructor(center: Vector3, w: Level?, size: Int) {
        if (w == null)
            throw Exception()
        this.v1 = center.clone().add(size.toDouble(), size.toDouble(), size.toDouble())
        this.v2 = center.clone().add(
                (-size).toDouble(), (-size).toDouble(), (-size).toDouble())
        this.world = w
        this.w_name = w.name
    }

    /**
     * Creates a new region from WorldEdit selection.
     *
     * @param selection worldedit selection
     */
    constructor(selection: Region) {
        this.v1 = Vector3(selection.minX, selection.minY, selection.minZ)
        this.v2 = Vector3(selection.maxX, selection.maxY, selection.maxZ)
        this.world = selection.world
        this.w_name = world.name
    }

    /**
     * Returns whether the location intersect the region.
     *
     * @param loc location to check.
     * @return
     */
    fun intersects(loc: Location): Boolean {
        return if (this.world.name == loc.getLevel().name)
            this.intersects(loc.asBlockVector3())
        else
            false
    }

    /**
     * Returns whether the location intersects X and Z coordinate of this region.
     *
     * @param loc location to check
     * @return
     */
    fun intersectsXZ(loc: Location): Boolean {
        return if (this.world.name == loc.getLevel().name)
            this.intersectsXZ(loc.asBlockVector3())
        else
            false
    }

    /**
     * Returns whatever vector intersects the region.
     *
     * @param v vector to check
     * @return
     */
    fun intersects(v: BlockVector3): Boolean {
        return (Region.range(this.v1.getX(), this.v2.getX(), v.getX().toDouble())
                && Region.range(this.v1.getY(), this.v2.getY(), v.getY().toDouble())
                && Region.range(this.v1.getZ(), this.v2.getZ(), v.getZ().toDouble()))
    }

    fun intersectsXZ(v: BlockVector3): Boolean {
        return Region.range(this.v1.getX(), this.v2.getX(), v.getX().toDouble()) && Region.range(this.v1.getZ(), this.v2.getZ(), v.getZ().toDouble())
    }

    fun serialize(yaml: Config, string: String) {
        yaml.set("$string.v1.x", this.v1.getX())
        yaml.set("$string.v1.y", this.v1.getY())
        yaml.set("$string.v1.z", this.v1.getZ())

        yaml.set("$string.v2.x", this.v2.getX())
        yaml.set("$string.v2.y", this.v2.getY())
        yaml.set("$string.v2.z", this.v2.getZ())

        yaml.set("$string.world", this.world.name)
    }

    override fun toString(): String {
        return ("Region{x1:" + this.v1.getX() + ",y1:" + this.v1.getY()
                + ",z1:" + this.v1.getZ() + ",x2:" + this.v2.getX() + ",y2:"
                + this.v2.getY() + ",z2:" + this.v2.getZ() + ",world:"
                + this.world.name + "}")
    }

    private fun rnd(max: Int): Int {
        return if (max == 0) {
            0
        } else {
            ElissaMC.core.random.nextInt(max)
        }
    }

    companion object {

        private fun range(min: Double, max: Double,
                          value: Double): Boolean {
            return if (max > min)
                if (value <= max) if (value >= min) true else false else false
            else
                if (value <= min) if (value >= max) true else false else false
        }

        /**
         * Creates region around specified location with specified size.
         *
         * @param center center
         * @param size   size
         * @return region
         */
        fun createAroundBox(center: Location, size: Int): Region? {
            try {
                return Region(center.add(size.toDouble(), size.toDouble(), size.toDouble()),
                        center.subtract(size.toDouble(), size.toDouble(), size.toDouble()),
                        center.getLevel())
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    }
}