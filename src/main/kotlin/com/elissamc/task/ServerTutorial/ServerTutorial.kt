package com.elissamc.task.ServerTutorial

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.level.Level
import cn.nukkit.level.Location
import cn.nukkit.scheduler.NukkitRunnable

import cn.nukkit.utils.TextFormat.*

class ServerTutorial(private val player: Player) : NukkitRunnable() {

    private var stage = 0
    private val l = Server.getInstance().defaultLevel
    private val previousLoc: Location
    private val locs = arrayOf(Location(-160.3890975496729, 100.0, 302.65173069276443, 307.15384, 26.55564, l), Location(-228.46697606052106, 88.0, 288.4036739560498, 310.9807, 33.4011, l), Location(-273.4095471915437, 84.0, 288.4437772466859, 57.75351, 12.304578, l), Location(-154.69999998807907, 106.0, 346.3194764605599, 208.09845, 26.041174, l), Location(-144.3900863763124, 93.0, 294.30000001192093, 341.02112, 16.031563, l), Location(-180.69999998807907, 103.0, 320.69999998807907, 245.1742, 9.333818, l))

    init {
        this.previousLoc = player.location
        player.setImmobile()
    }

    /**
     * When an object implementing interface `Runnable` is used
     * to create a thread, starting the thread causes the object's
     * `run` method to be called in that separately executing
     * thread.
     *
     *
     * The general contract of the method `run` is that it may
     * take any action whatsoever.
     *
     * @see Thread.run
     */
    override fun run() {
        when (stage) {
            0 -> {
                player.sendTitle(YELLOW.toString() + "Welcome", GRAY.toString() + "You just started the tutorial!", 5, 80, 5)
                player.teleport(locs[stage])
            }
            1 -> {
                player.sendTitle(GOLD.toString() + "Choosing the Server!", WHITE.toString() + "Use the compass in your inventory to choose a gametype", 20, 80, 20)
                player.teleport(locs[stage])
            }
            2 -> {
                player.sendTitle(GOLD.toString() + "Minigames", WHITE.toString() + "The lobby contains many fun minigames and secrets!", 20, 80, 20)
                player.teleport(locs[stage])
            }
            3 -> {
                player.sendTitle(GOLD.toString() + "Souls", WHITE.toString() + "Souls are a global currency working on all gametypes!", 5, 80, 5)
                player.teleport(locs[stage])
            }
            4 -> {
                player.sendTitle(GOLD.toString() + "Gadgets!", WHITE.toString() + "Gadgets are a fun way to spend your time!", 5, 40, 20)
                player.teleport(locs[stage])
            }
            5 -> {
                player.sendTitle(GOLD.toString() + "Mystery Crates!", WHITE.toString() + "Pay 10 souls to open a Mystery Crate!", 5, 40, 20)
                player.teleport(locs[stage])
            }
            6 -> {
                player.sendTitle(GOLD.toString() + "Tutorial finished!", WHITE.toString() + "Thank you for taking your time!", 5, 40, 20)
                player.teleport(this.previousLoc)
            }
            else -> {
                player.isImmobile = false
                this.cancel()
            }
        }
        stage++
    }
}
