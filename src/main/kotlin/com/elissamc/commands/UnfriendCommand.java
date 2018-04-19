package com.elissamc.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import com.elissamc.core.StorageEngine;

public class UnfriendCommand extends Command {


    public UnfriendCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(final CommandSender sender, final String alias, final String[] args) {
        if (sender instanceof Player) {
            if (sender.isOp()) {
                this.processOpCommand((Player) sender, args);
            } else {
                this.processCommand((Player) sender, args);
            }
        } else {
            sender.sendMessage("This command is only avaiable for players!");
        }
        return true;
    }

    private void processCommand(final Player sender, final String[] args) {
        if (args.length >= 1) {
            String playerName = args[0];
            boolean success = false;

            for (Player p : Server.getInstance().getOnlinePlayers().values()) {
                if (p.getName().equalsIgnoreCase(playerName)) {
                    if (StorageEngine.getProfile(sender.getUniqueId()).isFriend(
                            p.getUniqueId())) {
                        StorageEngine.getProfile(sender.getUniqueId()).removeFriend(
                                p.getUniqueId());
                        sender.sendMessage("Player '" + p.getName()
                                + "' has been REMOVED from your friends!");
                        success = true;
                    } else {
                        sender.sendMessage("Player '" + p.getName()
                                + "' is not in your friends list!");

                    }
                }
            }
            if (!success)
                sender.sendMessage("Player not found! Player must be online!");
        } else {
            sender.sendMessage("You must provide player name!");
        }
    }

    private void processOpCommand(final Player sender, final String[] args) {
        //No op commands.
        this.processCommand(sender, args);
    }

}