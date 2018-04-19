package com.elissamc.actions;

import cn.nukkit.Player;
import cn.nukkit.Server;

public class CommandAction implements Action {
    private String command = "";

    /**
     * Creates a new command action. Command should <b> not contain</b> slash. <code>%player%</code> in command will be
     * replaced with name of player, that is this command executing for.
     *
     * @param command
     *            command of this action
     */
    public CommandAction(final String command) {
        this.command = command;
    }

    @Override
    public void execute(final Player player) {
        Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), this.command.replace("%player%", player.getName()));
    }
}