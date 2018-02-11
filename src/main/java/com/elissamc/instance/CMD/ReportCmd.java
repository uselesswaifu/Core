package com.elissamc.instance.CMD;

import java.util.Arrays;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import com.elissamc.instance.Instance;

public class ReportCmd extends Command {

	Instance instance;

	public ReportCmd(Instance instance) {
		super("report");
		this.instance = instance;
		setAliases(new String[] { "r" });
		setUsage("/r or /r <player Name>");
		setCommandParameters(new HashMap<String, CommandParameter[]>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;
			{
				put("1arg", new CommandParameter[] {
						new CommandParameter("player Name", CommandParameter.ARG_TYPE_PLAYER, false), });
			}
		});
	}

	@Override
	public boolean execute(CommandSender player, String alias, String[] args) {
		if (args.length < 1) {
			final FormWindowCustom window = new FormWindowCustom("Report Menu");
			window.addElement(new ElementDropdown("Reporting for?",
					Arrays.asList("Inappropriate Chat", "Hacking", "Teaming", "Bug Abuse", "Spamming")));
			window.addElement(new ElementInput("Name:", "steve"));
			((Player) player).showFormWindow(window);
			return true;

		} else {
			if (args[0].equals("view"))
				if (player.isOp()) {
					reportsView((Player) player);
					return false;
				}
			final FormWindowCustom window = new FormWindowCustom("Report Menu");
			window.addElement(new ElementDropdown("Reporting for?",
					Arrays.asList("Inappropriate Chat", "Hacking", "Teaming", "Bug Abuse", "Spamming")));
			window.addElement(new ElementInput("Name:", args[0], args[0]));
			((Player) player).showFormWindow(window);
			return true;
		}
	}

	private void reportsView(Player player) {

		final FormWindowSimple window = new FormWindowSimple("Reports", "View player reports");
		window.addButton(new ElementButton("New Reports (20)"));
		window.addButton(new ElementButton("Banned Reports (200)"));
		window.addButton(new ElementButton("Deleted Reports (2)"));
		player.showFormWindow(window);
	}

}
