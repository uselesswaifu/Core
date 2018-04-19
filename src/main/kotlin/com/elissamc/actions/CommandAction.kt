package com.elissamc.actions

import cn.nukkit.Player
import cn.nukkit.Server

class CommandAction
/**
 * Creates a new command action. Command should ** not contain** slash. `%player%` in command will be
 * replaced with name of player, that is this command executing for.
 *
 * @param command
 * command of this action
 */
(command: String) : Action {
    private var command = ""

    init {
        this.command = command
    }

    override fun execute(player: Player) {
        Server.getInstance().dispatchCommand(Server.getInstance().consoleSender, this.command.replace("%player%", player.name))
    }
}