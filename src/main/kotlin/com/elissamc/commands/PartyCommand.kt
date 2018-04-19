package com.elissamc.commands

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import com.elissamc.chat.ChatManager
import com.elissamc.core.Party
import com.elissamc.core.StorageEngine

class PartyCommand(name: String) : Command(name) {

    override fun execute(sender: CommandSender,
                         alias: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (args.size < 1)
            return main(player)
        val cmd = args[0]
        if (cmd.equals("create", ignoreCase = true))
            return create(player)
        if (cmd.equals("invite", ignoreCase = true))
            return add(player, args[1])
        if (cmd.equals("kick", ignoreCase = true))
            return kick(player, args[1])
        return if (cmd.equals("leave", ignoreCase = true)) leave(player) else false

    }

    fun main(sender: Player): Boolean {
        val party: Party? = StorageEngine.getProfile(sender.uniqueId).party
        if (party == null) {
            sender.sendMessage(ChatManager.error("You are not in party!"))
        } else {
            var players = ""
            for (player in party.players)
                players += player.name + ", "
            players = players.substring(0, players.length - 2)

            sender.sendMessage(ChatManager.success("You are in party with: $players"))
            sender.sendMessage(ChatManager.success("Type /party leave to leave party."))
        }
        return true
    }

    fun create(sender: Player): Boolean {
        val party = StorageEngine.getProfile(sender.uniqueId).party
        if (party != null) {
            StorageEngine.getProfile(sender.uniqueId).party = Party(sender)
            party.addPlayer(sender)
        } else {
            sender.sendMessage(ChatManager.error("You have to leave your current party first! Type /party leave!"))
        }
        return true
    }

    fun add(sender: Player, playerName: String): Boolean {
        if (StorageEngine.getProfile(sender.uniqueId).party != null) {
            if (StorageEngine.getProfile(sender.uniqueId).party!!.isOwner(sender)) {
                val player = this.findPlayer(playerName)
                if (player != null) {
                    if (StorageEngine.getProfile(player.uniqueId).party == null) {
                        sender.sendMessage(ChatManager.success("Adding player "
                                + player.displayName + " to party!"))
                        StorageEngine.getProfile(sender.uniqueId).party!!.addPlayer(
                                player)
                        StorageEngine.getProfile(player.uniqueId).party = StorageEngine.getProfile(sender.uniqueId).party
                    } else {
                        sender.sendMessage(ChatManager.error("Specified player is in another party!"))
                    }
                } else {
                    sender.sendMessage(ChatManager.error("Player not found!"))
                }
            } else {
                sender.sendMessage(ChatManager.error("Only party owner can invite players."))
            }
        } else {
            this.create(sender)
            this.add(sender, playerName)
        }
        return true
    }

    fun kick(sender: Player, playerName: String): Boolean {
        if (StorageEngine.getProfile(sender.uniqueId).party != null) {
            val p = StorageEngine.getProfile(sender.uniqueId).party
            if (p!!.isOwner(sender)) {
                val player = this.findPlayer(playerName)
                if (player != null) {
                    if (p.contains(player)) {
                        player.sendMessage(ChatManager.success("You have been kicked from the party!"))
                        sender.sendMessage(ChatManager.success("Player "
                                + player.name + " has been kicked!"))
                        p.removePlayer(player)
                    } else {
                        sender.sendMessage(ChatManager.error("Player not found in this party!"))
                    }
                } else {
                    sender.sendMessage(ChatManager.error("Player not found!"))
                }
            } else {
                sender.sendMessage(ChatManager.error("You are not owner of this party!"))
            }
        } else {
            sender.sendMessage(ChatManager.error("You are not in party!"))
        }
        return true
    }

    fun leave(sender: Player): Boolean {
        if (StorageEngine.getProfile(sender.uniqueId).party != null) {
            val party = StorageEngine.getProfile(sender.uniqueId).party
            party!!.removePlayer(sender)
            sender.sendMessage(ChatManager.success("You have left party!"))
        } else {
            sender.sendMessage(ChatManager.error("You are not in party!"))
        }
        return true
    }

    private fun findPlayer(name: String): Player? {
        for (p in Server.getInstance().onlinePlayers.values)
            if (p.name.equals(name, ignoreCase = true))
                return p
        return null
    }
}