package com.elissamc.stats

import cn.nukkit.Player
import cn.nukkit.utils.DummyBossBar
import cn.nukkit.utils.TextFormat

import java.util.ArrayList

import cn.nukkit.utils.TextFormat.*

object LevelSystem {

    private const val SPACE = " "


    fun LevelUpgradeMessage(player: Player) {
        player.sendTitle(GRAY.toString() + "LEVEL UP", "")
        val txt = ArrayList<String>()

        txt.add(SPACE)
        txt.add(GREEN.toString() + "-------------------------------------")
        txt.add("" + GREEN + OBFUSCATED + "A" + RESET + SPACE + GOLD + "LEVEL UP!" + SPACE + GREEN + OBFUSCATED + "A")
        txt.add(SPACE)
        txt.add(GRAY.toString() + "You are now" + SPACE + DARK_AQUA + "ElissaMC Level" + SPACE + GREEN + Integer.toString(0) + GRAY + "!")
        txt.add(SPACE)
        txt.add(YELLOW.toString() + "Check the cult box for your reward!")
        txt.add(GREEN.toString() + "-------------------------------------")
        txt.add(SPACE)
        for (msg in center(txt, 0)) {
            player.sendMessage(msg)
        }

    }

    fun DailyRewardMessage(player: Player) {
        val txt = ArrayList<String>()

        txt.add(SPACE)
        txt.add("" + GREEN + OBFUSCATED + "A" + RESET + SPACE + GOLD + "DAILY REWARD!" + SPACE + GREEN + OBFUSCATED + "A")
        txt.add(SPACE)
        txt.add(GRAY.toString() + "+" + SPACE + YELLOW + "125" + SPACE + "Coins")
        txt.add(GRAY.toString() + "+" + SPACE + YELLOW + "312" + SPACE + "Experience")
        txt.add(SPACE)
        txt.add(YELLOW.toString() + "Spend on Cosmetics and Upgrades!")
        txt.add(SPACE)
        for (msg in center(txt, 0)) {
            player.sendMessage(msg)
        }
        addExperience(player, 312)
    }

    private fun addExperience(player: Player, xp: Int) {
        val bar: DummyBossBar = DummyBossBar.Builder(player).text(DARK_GRAY.toString() + "+" + GREEN + Integer.toString(xp) + GRAY + " Experience").build()
        player.createBossBar(bar)
//        ElissaMC.plugin.server.scheduler.scheduleRepeatingTask(ElissaMC.plugin, BossbarTask(ElissaMC.plugin, bar), 1)
    }

    fun ExperienceBar(): Array<String> {
        val placeholder = StringBuilder(TextFormat.BOLD.toString() + "")
        val space = StringBuilder()
        for (i in 0..19) {
            placeholder.append(AQUA.toString() + "_")
            space.append("  ")
        }
        val lvl = AQUA.toString() + "Lvl. 3" + space.substring(0, space.length / 2) + "Lvl. 4"
        val bar = DARK_GRAY.toString() + "[" + placeholder + DARK_GRAY + "]"
        return arrayOf(lvl, bar)
    }

    private fun center(string: ArrayList<String>, l: Int): ArrayList<String> {
        var length: Int = l
        val left = ArrayList<String>()
        for (s in string) {
            if (TextFormat.clean(s).length > length)
                length = s.length
        }
        for (s in string) {
            if (TextFormat.clean(s).length > length) {
                left.add(s.substring(0, length))
                continue
            }
            if (TextFormat.clean(s).length == length) {
                left.add(s)
                continue
            }
            val leftPadding = (length - TextFormat.clean(s).length) / 2
            val leftBuilder = StringBuilder()
            var i = 0
            while (i < leftPadding) {
                leftBuilder.append(" ")
                ++i
            }
            left.add(leftBuilder.toString() + s)

        }
        return left
    }
}
