package com.elissamc.api.ParkourSystem

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.Block
import cn.nukkit.block.BlockPressurePlateBase
import cn.nukkit.command.CommandSender
import cn.nukkit.command.defaults.VanillaCommand
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerInteractEvent
import cn.nukkit.level.Level
import cn.nukkit.level.Location
import cn.nukkit.utils.Config
import cn.nukkit.utils.TextFormat
import com.elissamc.ElissaMC

import java.io.File
import java.util.HashMap
import java.util.UUID
import java.util.concurrent.TimeUnit

import cn.nukkit.utils.TextFormat.*

class ParkourSystem : VanillaCommand("parkour"), Listener {

    private val config = Config(File(ElissaMC.folder.toString() + "/ParkourSystem/", "Timer.yml"), Config.YAML)
    private val parkour = HashMap<UUID, Int>()
    private val timer = HashMap<UUID, Long>()
    private val checkpoints = HashMap<Int, Location>()
    private val l = Server.getInstance().defaultLevel
    private val start = Location(-305.0, 53.0, 312.0, 90.12568, -2.6629138, l)
    private val end = Location(-320.0, 125.0, 314.0, l)

    init {
        Server.getInstance().commandMap.register("parkour", this)
        checkpoints[1] = Location(-311.0, 62.0, 304.0, -266.4483, -2.736549, l)
        checkpoints[2] = Location(-313.0, 70.0, 328.0, -235.58936, -14.071013, l)
        checkpoints[3] = Location(-318.0, 82.0, 309.0, -110.93039, 15.221949, l)
        checkpoints[4] = Location(-320.0, 89.0, 311.0, -359.941, 23.097181, l)
        checkpoints[5] = Location(-316.0, 96.0, 312.0, -358.83707, 7.493875, l)
        checkpoints[6] = Location(-320.0, 108.0, 310.0, -0.75057983, -1.7061664, l)
        checkpoints[7] = Location(-320.0, 125.0, 314.0, -3.8417358, 7.4202847, l)
    }

    override fun execute(commandSender: CommandSender, s: String, args: Array<String>): Boolean {
        if (commandSender !is Player)
            return false
        if (args.size > 0) {
            if (args[0] == "start") {
                commandSender.sendActionBar("Parkour Challenge Started")
                commandSender.teleport(start)
            }

            if (args[0] == "reset") {
                commandSender.sendActionBar("Parkour Challenge Restarted!")
                commandSender.teleport(start)
            }

            if (args[0] == "checkpoint") {
                if (parkour.containsKey(commandSender.uniqueId)) {
                    val pos = checkpoints[parkour[commandSender.uniqueId]]
                    if (pos != null) {
                        commandSender.sendActionBar("Sent to Checkpoint!")
                        commandSender.teleport(pos)
                    }
                }
            }
        }
        return false
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun onPlayerInteractPressurePlate(event: PlayerInteractEvent) {
        if (event.action == PlayerInteractEvent.Action.PHYSICAL) {
            val player = event.player
            val block = event.block
            if (block is BlockPressurePlateBase) {
                if (block.getX() == start.getX() && block.getY() == start.getY() && block.getZ() == start.getZ())
                    addToParkour(player)
                else if (block.getX() == end.getX() && block.getY() == end.getY() && block.getZ() == end.getZ())
                    endParkour(player)
                else
                    checkCheckpoint(player, block)
            }
        }
    }

    private fun endParkour(player: Player) { // TODO: GIVE SOULS ON BEATING SCORE
        if (!parkour.containsKey(player.uniqueId)) {
            player.sendMessage(TextFormat.colorize("&a&lThis is the finish line for the parkour! Get to the start line and climb back up here!"))
            return
        }

        val millis = System.currentTimeMillis() - timer[player.uniqueId]!!
        val time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))

        if (!config.exists(player.uniqueId.toString())) {
            player.sendMessage("" + GREEN + BOLD + "Congratulations on completing the parkour! You finised in " + YELLOW + time + GREEN + "!")
            player.sendMessage("" + GREEN + BOLD + "Try again to get an even better record!")
            config.set(player.uniqueId.toString(), millis)
            config.save()
        }

        if (millis > config.get(player.uniqueId.toString()) as Long) {
            val oldtim = config.get(player.uniqueId.toString()) as Long
            val old = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(oldtim),
                    TimeUnit.MILLISECONDS.toSeconds(oldtim) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(oldtim)))
            player.sendMessage(TextFormat.colorize("&a&lYour time &e&l%time &a&ldid not beat your previous record of &e&l%old&a&l!\n".replace("%time".toRegex(), time).replace("%old".toRegex(), old) + "&a&lTry again to beat your old record!"))
        }

        if (millis < config.get(player.uniqueId.toString()) as Long) {
            player.sendMessage(TextFormat.colorize("&a&lThat's a new record of &e&l%time&a&l! Try again to get an even\n".replace("%time".toRegex(), time) + "better record!"))
        }
        config.set(player.uniqueId.toString(), millis)
        config.save()
        parkour.remove(player.uniqueId)
        timer.remove(player.uniqueId)
    }

    private fun checkCheckpoint(player: Player, block: Block) {
        val x = block.getX().toInt()
        val y = block.getY().toInt()
        val z = block.getZ().toInt()
        var check: MutableMap.MutableEntry<Int, Location>? = null
        if (!timer.containsKey(player.uniqueId))
            return
        for (checkpoint in checkpoints.entries) {
            if (checkpoint.value.getX() == x.toDouble() && checkpoint.value.getY() == y.toDouble() && checkpoint.value.getZ() == z.toDouble()) {
                check = checkpoint
            }
        }
        if (check != null) {
            if (!parkour.containsKey(player.uniqueId) || parkour[player.uniqueId] != check!!.key) {
                player.sendMessage("" + BOLD + GREEN + "You reached " + YELLOW + "Checkpoint #" + Integer.toString(check!!.key) + GREEN + ". You can type" + YELLOW + " /parkour checkpoint" + GREEN + " to get back to this place")
                parkour[player.uniqueId] = check!!.key
            }
        }
    }

    private fun addToParkour(player: Player) {
        if (!parkour.containsKey(player.uniqueId))
            player.sendMessage("" + GREEN + BOLD + "Parkour challenge started! Use" + YELLOW + " /parkour reset" + GREEN + " to restart!")
        else
            player.sendMessage("" + GREEN + BOLD + "Reset your timer to 00:00! Get to the finish line!")
        parkour[player.uniqueId] = 0
        timer[player.uniqueId] = System.currentTimeMillis()
    }
}
