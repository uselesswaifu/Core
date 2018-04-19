package com.elissamc.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import com.elissamc.core.Settings;
import com.elissamc.core.StorageEngine;

public class SettingsCommand extends Command {

    public SettingsCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(final CommandSender sender, final String alias, final String[] args) {
        if (sender instanceof Player) {
            if (args.length == 2) {
                try {
                    Settings setting = Settings.valueOf(args[0]);
                    Boolean value = Boolean.parseBoolean(args[1]);

                    StorageEngine.getProfile(((Player) sender).getUniqueId()).setSetting(
                            setting, value);

                } catch (Exception ex) {
                    sender.sendMessage(ex.toString());
                }
            } else {
                sender.sendMessage("/settings <setting> <false/true>");
                String avaiable = TextFormat.GOLD + "Avaiable settings: "
                        + TextFormat.YELLOW;
                for (Settings s : Settings.values()) {
                    avaiable += s.toString() + ", ";
                }
                sender.sendMessage(avaiable);
            }
        }
        return false;
    }
}