// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform.
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 *
 * This file is part of Pexel.
 *
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
package com.elissamc.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import com.elissamc.chat.ChatManager;
import com.elissamc.core.Party;
import com.elissamc.core.StorageEngine;

@CommandHandler(name = "party", aliases = { "partyy", "partyyy" }, description = "Command used to deal with parties")
public class PartyCommand extends Command {
    public PartyCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(final CommandSender sender,
                             final String alias, final String[] args) {
        Player player = (Player) sender;
        if(args.length < 1)
            main(player);
        String cmd = args[0];
        if(cmd.equalsIgnoreCase("create"))
            create(player);
        if(cmd.equalsIgnoreCase("invite"))
            add(player, args[1]);
        if(cmd.equalsIgnoreCase("kick"))
            kick(player, args[1]);
        if(cmd.equalsIgnoreCase("leave"))
            leave(player);

        return false;
    }

    @SubCommand
    public void main(final Player sender) {
        if (StorageEngine.getProfile(sender.getUniqueId()).getParty() == null) {
            sender.sendMessage("You are not in party!");
        }
        else {
            Party p = StorageEngine.getProfile(sender.getUniqueId()).getParty();
            String players = "";
            for (Player player : p.getPlayers())
                players += player.getName() + ", ";
            players = players.substring(0, players.length() - 2);

            sender.sendMessage("You are in party with: " + players);
            sender.sendMessage("Type /party leave to leave party.");
        }
    }

    @SubCommand(name = "create", description = "Creates a new party")
    public void create(final Player sender) {
        if (StorageEngine.getProfile(sender.getUniqueId()).getParty() == null) {
            StorageEngine.getProfile(sender.getUniqueId()).setParty(new Party(sender));
            StorageEngine.getProfile(sender.getUniqueId()).getParty().addPlayer(sender);
        }
        else {
            sender.sendMessage("You have to create party first! Type /party create!");
        }
    }

    @SubCommand(name = "invite", description = "Invites player to your party")
    public void add(final Player sender, final String playerName) {
        if (StorageEngine.getProfile(sender.getUniqueId()).getParty() != null) {
            if (StorageEngine.getProfile(sender.getUniqueId()).getParty().isOwner(sender)) {
                Player player = this.findPlayer(playerName);
                if (player != null) {
                    if (StorageEngine.getProfile(player.getUniqueId()).getParty() == null) {
                        sender.sendMessage("Adding player "
                                + player.getDisplayName() + " to party!");
                        StorageEngine.getProfile(sender.getUniqueId()).getParty().addPlayer(
                                player);
                        StorageEngine.getProfile(player.getUniqueId()).setParty(
                                StorageEngine.getProfile(sender.getUniqueId()).getParty());
                    }
                    else {
                        sender.sendMessage("Specified player is in another party!");
                    }
                }
                else {
                    sender.sendMessage("Player not found!");
                }
            }
            else {
                sender.sendMessage("Only party owner can invite players.");
            }
        }
        else {
            this.create(sender);
            this.add(sender, playerName);
        }
    }

    @SubCommand(name = "kick", description = "Kicks player from your party")
    public void kick(final Player sender, final String playerName) {
        if (StorageEngine.getProfile(sender.getUniqueId()).getParty() != null) {
            Party p = StorageEngine.getProfile(sender.getUniqueId()).getParty();
            if (p.isOwner(sender)) {
                Player player = this.findPlayer(playerName);
                if (player != null) {
                    if (p.contains(player)) {
                        player.sendMessage(ChatManager.success("You have been kicked from the party!"));
                        sender.sendMessage(ChatManager.success("Player "
                                + player.getName() + " has been kicked!"));
                        p.removePlayer(player);
                    }
                    else {
                        sender.sendMessage(ChatManager.error("Player not found in this party!"));
                    }
                }
                else {
                    sender.sendMessage(ChatManager.error("Player not found!"));
                }
            }
            else {
                sender.sendMessage(ChatManager.error("You are not owner of this party!"));
            }
        }
        else {
            sender.sendMessage(ChatManager.error("You are not in party!"));
        }
    }

    @SubCommand(name = "leave", description = "Leaves current party")
    public void leave(final Player sender) {
        if (StorageEngine.getProfile(sender.getUniqueId()).getParty() != null) {
            Party party = StorageEngine.getProfile(sender.getUniqueId()).getParty();
            party.removePlayer(sender);
            sender.sendMessage(ChatManager.success("You have left party!"));
        }
        else {
            sender.sendMessage(ChatManager.error("You are not in party!"));
        }
    }

    private Player findPlayer(final String name) {
        for (Player p : Server.getInstance().getOnlinePlayers().values())
            if (p.getName().equalsIgnoreCase(name))
                return p;
        return null;
    }
}