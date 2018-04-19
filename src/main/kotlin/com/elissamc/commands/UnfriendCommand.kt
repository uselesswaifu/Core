package com.elissamc.commands

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import com.elissamc.chat.ChatManager
import com.elissamc.core.StorageEngine

class UnfriendCommand(name: String) : Command(name) {

    override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (sender.isOp()) {
                this.processOpCommand(sender, args)
            } else {
                this.processCommand(sender, args)
            }
        } else {
            sender.sendMessage("This command is only available for players!")
        }
        return true
    }

    private fun processCommand(sender: Player, args: Array<String>) {
        if (args.isNotEmpty()) {
            val playerName = args[0]
            var success = false

            for (p in Server.getInstance().onlinePlayers.values) {
                if (p.name.equals(playerName, ignoreCase = true)) {
                    if (StorageEngine.getProfile(sender.uniqueId).isFriend(
                                    p.uniqueId)) {
                        StorageEngine.getProfile(sender.uniqueId).removeFriend(
                                p.uniqueId)
                        sender.sendMessage(ChatManager.success("Player '" + p.name
                                + "' has been REMOVED from your friends!"))
                        success = true
                    } else {
                        sender.sendMessage(ChatManager.error("Player '" + p.name
                                + "' is not in your friends list!"))

                    }
                }
            }
            if (!success)
                sender.sendMessage(ChatManager.error("Player not found! Player must be online!"))
        } else {
            sender.sendMessage(ChatManager.error("You must provide player name!"))
        }
    }

    private fun processOpCommand(sender: Player, args: Array<String>) {
        //No op commands.
        this.processCommand(sender, args)
    }

}