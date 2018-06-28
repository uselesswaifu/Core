package com.elissamc.area

import cn.nukkit.Server
import cn.nukkit.level.Location
import cn.nukkit.potion.Effect
import cn.nukkit.scheduler.NukkitRunnable
import cn.nukkit.scheduler.TaskHandler
import com.elissamc.ElissaMC
import com.elissamc.core.Region
import com.elissamc.core.Updatable
import com.elissamc.core.UpdatedParts

/** Lobby is protected area with some special functions. */
class Lobby
/**
 * Creates new lobby object with specified name and region.
 *
 * @param name   name of lobby
 * @param region region of lobby
 */
(name: String, region: Region) : ProtectedArea(name, region), Updatable {

    /** Location of lobby spawn. */
    var spawn: Location? = null
    private var taskId: TaskHandler? = null
    /** How often should lobby check for players. */
    var checkInterval = 20 //40 ticks = 2 second.
    /** The minimal Y coordinate value, after the lobby will teleport players to its spawn. */
    var thresholdY = 50

    init {
        this.setGlobalFlag(AreaFlag.BLOCK_BREAK, false)
        this.setGlobalFlag(AreaFlag.BLOCK_PLACE, false)
        this.setGlobalFlag(AreaFlag.PLAYER_GETDAMAGE, false)
        //Weird call, isn't it?
        this.updateStart()
    }

    /** Updates players. Adds potion effects and teleports them if needed. */
    private fun updatePlayers() {
        for (player in this.region.playersXZ) {
            //Lobby potion effects.
            player.addEffect(Effect.getEffect(Effect.SPEED).setDuration(20 * 30).setAmplifier(2).setVisible(false))
            player.addEffect(Effect.getEffect(Effect.NIGHT_VISION).setDuration(20 * 30).setAmplifier(1).setVisible(false))

            //In-void lobby teleport.
            if (player.location.getY() < this.thresholdY)
                player.teleport(this.spawn)
        }
    }

    override fun updateStart() {
        UpdatedParts.registerPart(this)
        this.taskId = Server.getInstance().scheduler.scheduleRepeatingTask(ElissaMC.plugin, object : NukkitRunnable() {
            override fun run() {
                this@Lobby.updatePlayers()
            }
        }, this.checkInterval, true)
    }

    override fun updateStop() {
        taskId!!.cancel()
    }
}