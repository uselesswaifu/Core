package com.elissamc.commands

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import com.elissamc.chat.ChatFormat
import com.elissamc.core.StorageEngine

class FriendCommand(name: String) : Command(name) {

    override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (sender.isOp()) {
                this.processOpCommand(sender, args)
            } else {
                this.processCommand(sender, args)
            }
        } else {
            sender.sendMessage(ChatFormat.error("This command is only available for players!"))
        }
        return true
    }

    private fun processCommand(sender: Player, args: Array<String>) {
        if (args.isNotEmpty()) {
            val playerName = args[0]
            var success = false
            for (p in Server.getInstance().onlinePlayers.values) {
                if (p.name.equals(playerName, ignoreCase = true)) {
                    StorageEngine.getProfile(sender.uniqueId).addFriend(
                            p.uniqueId)
                    sender.sendMessage(ChatFormat.success("Player '" + p.name
                            + "' has been ADDED to your friends!"))
                    p.sendMessage(ChatFormat.success("Player '" + sender.name
                            + "' added you to his/her friends! Add him too! /friend "
                            + sender.name))

                    success = true
                }
            }
            if (!success)
                sender.sendMessage(ChatFormat.error("Player not found! Player must be online!"))
        } else {
            sender.sendMessage(ChatFormat.error("You must provide player name!"))
        }
    }

    private fun processOpCommand(sender: Player, args: Array<String>) {
        //No op commands.
        this.processCommand(sender, args)
    }
}