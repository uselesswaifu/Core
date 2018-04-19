package com.elissamc.commands

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.utils.TextFormat
import com.elissamc.chat.ChatManager
import com.elissamc.core.Settings
import com.elissamc.core.StorageEngine

class SettingsCommand(name: String) : Command(name) {

    override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.size == 2) {
                try {
                    val setting = Settings.valueOf(args[0])
                    val value = java.lang.Boolean.parseBoolean(args[1])

                    StorageEngine.getProfile(sender.uniqueId).setSetting(
                            setting, value)

                } catch (ex: Exception) {
                    sender.sendMessage(ChatManager.error(ex.toString()))
                }

            } else {
                sender.sendMessage(ChatManager.error("/settings <setting> <false/true>"))
                var avaiable = (TextFormat.GOLD.toString() + "Avaiable settings: "
                        + TextFormat.YELLOW)
                for (s in Settings.values()) {
                    avaiable += s.toString() + ", "
                }
                sender.sendMessage(avaiable)
            }
        }
        return false
    }
}