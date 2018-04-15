package com.elissamc.instance.CMD

import java.util.Arrays
import java.util.HashMap

import cn.nukkit.Player
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import cn.nukkit.command.data.CommandParameter
import cn.nukkit.form.element.ElementButton
import cn.nukkit.form.element.ElementDropdown
import cn.nukkit.form.element.ElementInput
import cn.nukkit.form.window.FormWindowCustom
import cn.nukkit.form.window.FormWindowSimple
import com.elissamc.instance.Instance

class ReportCmd(internal var instance: Instance) : Command("report") {

    init {
        aliases = arrayOf("r")
        usage = "/r or /r <player Name>"
        setCommandParameters(object : HashMap<String, Array<CommandParameter>>() {
            /**
             *
             */
            private val serialVersionUID = 1L

            init {
                put("1arg", arrayOf(CommandParameter("player Name", CommandParameter.ARG_TYPE_PLAYER, false)))
            }
        })
    }

    override fun execute(player: CommandSender, alias: String, args: Array<String>): Boolean {
        if (args.size < 1) {
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
