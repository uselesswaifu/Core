package com.elissamc.commands

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.form.element.ElementButton
import cn.nukkit.form.element.ElementDropdown
import cn.nukkit.form.element.ElementInput
import cn.nukkit.form.window.FormWindowCustom
import cn.nukkit.form.window.FormWindowSimple
import java.util.*

class ReportCmd : Command("report") {

    init {
        aliases = arrayOf("r")
        usage = "/r or /r <player Name>"
    }

    override fun execute(player: CommandSender, alias: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            val window = FormWindowCustom("Report Menu")
            window.addElement(ElementDropdown("Reporting for?",
                    Arrays.asList("Inappropriate Chat", "Hacking", "Teaming", "Bug Abuse", "Spamming")))
            window.addElement(ElementInput("Name:", "steve"))
            (player as Player).showFormWindow(window)
            return true

        } else {
            if (args[0] == "view")
                if (player.isOp) {
                    reportsView(player as Player)
                    return false
                }
            val window = FormWindowCustom("Report Menu")
            window.addElement(ElementDropdown("Reporting for?",
                    Arrays.asList("Inappropriate Chat", "Hacking", "Teaming", "Bug Abuse", "Spamming")))
            window.addElement(ElementInput("Name:", args[0], args[0]))
            (player as Player).showFormWindow(window)
            return true
        }
    }

    private fun reportsView(player: Player) {

        val window = FormWindowSimple("Reports", "View player reports")
        window.addButton(ElementButton("New Reports (20)"))
        window.addButton(ElementButton("Banned Reports (200)"))
        window.addButton(ElementButton("Deleted Reports (2)"))
        player.showFormWindow(window)
    }

}
