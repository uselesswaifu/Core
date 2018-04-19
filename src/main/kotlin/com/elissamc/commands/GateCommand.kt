package com.elissamc.commands


import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.utils.TextFormat
import com.boydti.fawe.`object`.FawePlayer
import com.elissamc.chat.ChatManager
import com.elissamc.core.StorageEngine

class GateCommand(name: String) : Command(name) {

    override fun execute(sender: CommandSender, alias: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (sender.isOp()) {
                this.processOpCommand(sender, args)
            } else {
                this.processCommand(sender)
            }
        } else {
            sender.sendMessage(ChatManager.error("This command is only avaiable for players!"))
        }
        return true
    }

    private fun processOpCommand(sender: Player, args: Array<String>) {
        if (args.size >= 2) {
            val action = args[0]
            val name = args[1]

            if (action.equals("create", ignoreCase = true)) {
                if (args.size == 4) {

                    if (this.checkSelection(sender)) {
                        if (StorageEngine.getGate(name) == null) {
                            sender.sendMessage(ChatManager.error("Can't configure gate!"))
                        } else {
                            sender.sendMessage(ChatManager.error("Gate with name '"
                                    + name + "' already exists!"))
                        }
                    }
                } else {
                    sender.sendMessage(ChatManager.error("Wrong use! Type /gate to help!"))
                }
            } else if (action.equals("modify", ignoreCase = true)) {
                if (args.size == 4) {

                    if (StorageEngine.getGate(name) != null) {
                        sender.sendMessage(ChatManager.error("Can't configure gate by command in this build."))
                    } else {
                        sender.sendMessage(ChatManager.error("Gate with name '" + name
                                + "' does not exists!"))
                    }
                } else {
                    sender.sendMessage(ChatManager.error("Wrong use! Type /gate to help!"))
                }
            } else if (action.equals("remove", ignoreCase = true)) {
                if (StorageEngine.getGate(name) != null) {
                    StorageEngine.removeGate(name)
                    sender.sendMessage(ChatManager.success("Gate '" + name
                            + "' has been removed!"))
                } else {
                    sender.sendMessage(ChatManager.error("Gate '" + name
                            + "' not found!"))
                }
            } else {
                sender.sendMessage(ChatManager.error("Invalid action!"))
            }
        } else {
            sender.sendMessage(TextFormat.RED.toString() + "/gate create <name> <actionType> <actionContent>")
            sender.sendMessage(TextFormat.RED.toString() + "/gate modify <name> <actionType> <actionContent>")
            sender.sendMessage(TextFormat.RED.toString() + "/gate remove <name>")
            sender.sendMessage(TextFormat.LIGHT_PURPLE.toString() + "--------------------------------------")
            sender.sendMessage(TextFormat.GREEN.toString() + "====== ACTION TYPES ======")
            sender.sendMessage(TextFormat.GOLD.toString() + "Type: commmand")
            sender.sendMessage(TextFormat.YELLOW.toString() + "Executes command as player. You can use %player% in content - it will be replaced with his name.")
            sender.sendMessage(TextFormat.BLUE.toString() + "Example: /gate create <name> command \"server staving\"")
            sender.sendMessage(TextFormat.GOLD.toString() + "Type: teleport")
            sender.sendMessage(TextFormat.YELLOW.toString() + "Teleports player to specified location. Use syntax 'x,y,z,yaw,pitch,world'")
            sender.sendMessage(TextFormat.BLUE.toString() + "Example: /gate create <name> teleport 15,85,41,0,90,world")
        }
    }

    private fun processCommand(sender: Player) {
        sender.sendMessage(ChatManager.error("Permission denied!"))
    }

    private fun checkSelection(sender: Player): Boolean {
        if (FawePlayer.wrap<Any>(sender).selection != null)
            return true
        else {
            sender.sendMessage(ChatManager.error("Make a WorldEdit selection first!"))
            return true
        }
    }
}