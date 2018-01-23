package com.blademc.core.Instance.CMD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import com.blademc.core.Instance.Instance;

public class PartyCmd extends Command{

    private Instance instance;
    public Map<String, ArrayList<Player>> parties;
    private Map<String, ArrayList<String>> invites;
    public Map<String, Boolean> partychat;

    public PartyCmd(Instance instance) {
        super("party");
        this.parties = new HashMap<>();
        this.invites = new HashMap<>();
        this.partychat = new HashMap<>();
        this.instance = instance;
        setAliases(new String[]{"p", "pc"});
        setUsage("/p or /party");
    }

    @Override
    public boolean execute(CommandSender p, String alias, String[] args) {
        Player player = (Player) p;

        if (!(p instanceof Player))
            return true;

        if (alias.equals("pc")) {
            for (Map.Entry<String, ArrayList<Player>> entry : parties.entrySet()) {
                if (entry.getValue().contains(player)) {
                    if(partychat.containsKey(player.getName())){
                        partychat.put(player.getName(), !partychat.get(player.getName()));
                        String on = partychat.get(player.getName()) ? "on" : "off";
                        player.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + TextFormat.GRAY + "PartyChat has been set to " + TextFormat.YELLOW + on);
                        return true;
                    }
                    partychat.put(player.getName(), true);
                    String on = "on";
                    player.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + TextFormat.GRAY + "PartyChat has been set to " + TextFormat.YELLOW + on);
                    return true;

                   // for (Player member : parties.get(entry.getKey())) {
                   //     member.sendMessage(player.getName() + " " + args[0]);
                   // }
                }
            }
        }
        if (args.length > 0) {
            if (args[0].equals("create")) {
                player.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + TextFormat.GRAY + "Party has been created!\nInvite players using /p invite");
                parties.put(player.getName(), new ArrayList<>());
                parties.get(player.getName()).add(player);
                return true;
            }
            if (args[0].equals("invite")) {
                if (args.length > 1) {
                    if (parties.containsKey(player.getName())) {
                        Player invited = Server.getInstance().getPlayer(args[1]);
                        if (!parties.get(player.getName()).contains(invited)) {
                            for (Player member : parties.get(player.getName())) {
                                member.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + TextFormat.GRAY + invited.getDisplayName() + " has been invited to the party");
                            }
                            invited.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + TextFormat.GRAY + "You have been invited to a party!\nuse /p accept to join ");
                            invites.put(player.getName(), new ArrayList<>());
                            invites.get(player.getName()).add(invited.getName());
                            instance.getServer().getScheduler().scheduleDelayedTask(instance.getMain(), () -> {
                                if (invites.get(player.getName()).contains(invited.getName())) {
                                    invites.get(player.getName()).remove(invited.getName());
                                    invited.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + TextFormat.GRAY + "Party invitation has expired");
                                }
                            }, 30 * 30);
                            return true;


                        }
                    }
                }
            }
            if (args[0].equals("accept")) {
                for (Map.Entry<String, ArrayList<String>> entry : invites.entrySet()) {
                    if (entry.getValue().contains(player.getName())) {
                        invites.get(entry.getKey()).remove(player.getName());
                        parties.get(entry.getKey()).add(player);
                        for (Player member : parties.get(entry.getKey())) {
                            member.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + TextFormat.GRAY + player.getDisplayName() + " has joined the party");
                        }
                        return true;
                    }
                }
            }
            if (args[0].equals("deny")) {
                for (Map.Entry<String, ArrayList<String>> entry : invites.entrySet()) {
                    if (entry.getValue().contains(player.getName())) {
                        for (Player member : parties.get(entry.getKey())) {
                            member.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + TextFormat.GRAY + player.getDisplayName() + " has denied the party invite");
                        }
                        invites.get(entry.getKey()).remove(player.getName());
                        return true;
                    }
                }
            }
            if (args[0].equals("kick")) {
                if (args.length > 1) {
                    for (Map.Entry<String, ArrayList<Player>> entry : parties.entrySet()) {
                        if (entry.getValue().contains(player)) {
                            Player invited = Server.getInstance().getPlayer(args[1]);
                            if (parties.get(entry.getKey()).contains(invited)) {
                                for (Player member : parties.get(entry.getKey())) {
                                    member.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + TextFormat.GRAY + invited.getDisplayName() + " has been kicked from the party");
                                }
                                parties.get(entry.getKey()).remove(invited);
                                return true;
                            }
                        }
                    }
                }
            }
            if (args[0].equals("list")) {
                StringBuilder players = new StringBuilder();
                for (Map.Entry<String, ArrayList<Player>> entry : parties.entrySet()) {
                    if (entry.getValue().contains(player)) {
                        for (Player member : parties.get(entry.getKey())) {
                            players.append(TextFormat.BOLD).append(TextFormat.GREEN.toString()).append(member.getDisplayName()).append(TextFormat.RESET).append(TextFormat.GRAY).append(", ");
                        }
                    }
                    player.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "» " + TextFormat.RESET + TextFormat.GRAY + "Party List " + TextFormat.YELLOW + TextFormat.BOLD.toString() + "«" + TextFormat.RESET);
                    player.sendMessage(players.toString());
                    return true;
                }

            }
            player.sendMessage(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + TextFormat.RED + "An error has occurred");
            return false;
        }
        return false;
    }
}
