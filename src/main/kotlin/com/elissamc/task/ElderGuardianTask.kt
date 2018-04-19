package com.elissamc.task

import cn.nukkit.Player
import cn.nukkit.level.Position
import cn.nukkit.network.protocol.LevelEventPacket
import cn.nukkit.scheduler.NukkitRunnable

class ElderGuardianTask(private val player: Player, private val location: Position, private val title: String, private val subtitle: String) : NukkitRunnable() {

    override fun run() {
        val pk = LevelEventPacket()
        pk.evid = LevelEventPacket.EVENT_GUARDIAN_CURSE
        pk.data = 0
        pk.x = player.x.toFloat()
        pk.y = player.y.toFloat()
        pk.z = player.z.toFloat()

        player.dataPacket(pk)
        player.setSubtitle(subtitle)
        player.sendTitle(title)
        player.teleport(location)

    }

}
