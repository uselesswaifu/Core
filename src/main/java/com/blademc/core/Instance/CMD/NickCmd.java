package com.blademc.core.Instance.CMD;

import java.util.Arrays;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import com.blademc.core.Instance.Instance;

public class NickCmd extends Command {

    Instance instance;

    public NickCmd(Instance instance) {
        super("nick");
        this.instance = instance;
        setAliases(new String[]{"n"});
        setUsage("/n or /nick");
    }

    @Override
    public boolean execute(CommandSender p, String alias, String[] args) {
        Player player = (Player) p;
        final FormWindowCustom window = new FormWindowCustom("Nick Menu");
        window.addElement(new ElementLabel("Let's get you setup for your nickname! Please choose a name and rank"));
        window.addElement(new ElementDropdown("What Rank are ya, aiming for?",
                Arrays.asList("Default", "VIP", "VIP+", "MVP", "MVP+", "MVP++")));
        window.addElement(new ElementInput("Name:", player.getName()));
        window.addElement(new ElementToggle("Random Skin?", false));
        player.showFormWindow(window);

        return true;
    }
}
