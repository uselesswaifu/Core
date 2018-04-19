package com.elissamc.commands

import java.util.Arrays

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.form.element.ElementDropdown
import cn.nukkit.form.element.ElementInput
import cn.nukkit.form.element.ElementLabel
import cn.nukkit.form.element.ElementToggle
import cn.nukkit.form.window.FormWindowCustom
import com.elissamc.ElissaCore

class NickCmd(internal var instance: ElissaCore) : Command("nick") {

    init {
        aliases = arrayOf("n")
        usage = "/n or /nick"
    }

    override fun execute(p: CommandSender, alias: String, args: Array<String>): Boolean {
        val player = p as Player
        val window = FormWindowCustom("Nick Menu")
        window.addElement(ElementLabel("Let's get you setup for your nickname! Please choose a name and rank"))
        window.addElement(ElementDropdown("What Rank are ya, aiming for?",
                Arrays.asList("Default", "VIP", "VIP+", "MVP", "MVP+", "MVP++")))
        window.addElement(ElementInput("Name:", player.name))
        window.addElement(ElementToggle("Random Skin?", false))
        player.showFormWindow(window)

        return true
    }
}
