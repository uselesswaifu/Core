package com.elissamc.commands;


import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import com.boydti.fawe.object.FawePlayer;
import com.elissamc.chat.ChatManager;
import com.elissamc.core.StorageEngine;

public class GateCommand extends Command {

    public GateCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(final CommandSender sender, final String alias, final String[] args) {
            if (sender instanceof Player) {
                if (sender.isOp()) {
                    this.processOpCommand((Player) sender, args);
                }
                else {
                    this.processCommand((Player) sender, args);
                }
            }
            else {
                sender.sendMessage(ChatManager.error("This command is only avaiable for players!"));
            }
            return true;
    }

    private void processOpCommand(final Player sender, final String[] args) {
        if (args.length >= 2) {
            String action = args[0];
            String name = args[1];

            if (action.equalsIgnoreCase("create")) {
                if (args.length == 4) {

                    if (this.checkSelection(sender)) {
                        if (StorageEngine.getGate(name) == null) {
                            sender.sendMessage(ChatManager.error("Can't configure gate!"));
                        }
                        else {
                            sender.sendMessage(ChatManager.error("Gate with name '"
                                    + name + "' already exists!"));
                        }
                    }
                }
                else {
                    sender.sendMessage(ChatManager.error("Wrong use! Type /gate to help!"));
                }
            }
            else if (action.equalsIgnoreCase("modify")) {
                if (args.length == 4) {

                    if (StorageEngine.getGate(name) != null) {
                        sender.sendMessage(ChatManager.error("Can't configure gate by command in this build."));
                    }
                    else {
                        sender.sendMessage(ChatManager.error("Gate with name '" + name
                                + "' does not exists!"));
                    }
                }
                else {
                    sender.sendMessage(ChatManager.error("Wrong use! Type /gate to help!"));
                }
            }
            else if (action.equalsIgnoreCase("remove")) {
                if (StorageEngine.getGate(name) != null) {
                    StorageEngine.removeGate(name);
                    sender.sendMessage(ChatManager.success("Gate '" + name
                            + "' has been removed!"));
                }
                else {
                    sender.sendMessage(ChatManager.error("Gate '" + name
                            + "' not found!"));
                }
            }
            else {
                sender.sendMessage(ChatManager.error("Invalid action!"));
            }
        }
        else {
            sender.sendMessage(TextFormat.RED
                    + "/gate create <name> <actionType> <actionContent>");
            sender.sendMessage(TextFormat.RED
                    + "/gate modify <name> <actionType> <actionContent>");
            sender.sendMessage(TextFormat.RED + "/gate remove <name>");
            sender.sendMessage(TextFormat.LIGHT_PURPLE
                    + "--------------------------------------");
            sender.sendMessage(TextFormat.GREEN + "====== ACTION TYPES ======");
            sender.sendMessage(TextFormat.GOLD + "Type: commmand");
            sender.sendMessage(TextFormat.YELLOW
                    + "Executes command as player. You can use %player% in content - it will be replaced with his name.");
            sender.sendMessage(TextFormat.BLUE
                    + "Example: /gate create <name> command \"server staving\"");
            sender.sendMessage(TextFormat.GOLD + "Type: teleport");
            sender.sendMessage(TextFormat.YELLOW
                    + "Teleports player to specified location. Use syntax 'x,y,z,yaw,pitch,world'");
            sender.sendMessage(TextFormat.BLUE
                    + "Example: /gate create <name> teleport 15,85,41,0,90,world");
        }
    }

    private void processCommand(final Player sender, final String[] args) {
        sender.sendMessage(ChatManager.error("Permission denied!"));
    }

    private boolean checkSelection(final Player sender) {
        if (FawePlayer.wrap(sender).getSelection() != null)
            return true;
        else {
            sender.sendMessage(ChatManager.error("Make a WorldEdit selection first!"));
            return true;
        }
    }
}