package com.blademc.core.player.event;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerSettingsRespondedEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.lang.BaseLang;
import cn.nukkit.network.protocol.ServerSettingsResponsePacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.blademc.core.Cosmetic.MysteryBox;
import com.blademc.core.Cosmetic.UpdateCosmetic;
import com.blademc.core.Instance.BladeCultist;
import com.blademc.core.Instance.Instance;

import javax.xml.soap.Text;

public class FormResponseEvent implements Listener {

	private final Instance instance;

	public FormResponseEvent(Instance instance) {
		this.instance = instance;
	}


	@EventHandler
    public void respond(PlayerSettingsRespondedEvent event){
	    Player player = event.getPlayer();
        int gaykid = ((FormWindowCustom) event.getWindow()).getResponse().getDropdownResponse(1).getElementID();
        switch(gaykid){
            case 0:{
               player.sendMessage("You language has been changed to: English");
            }
            case 1:{
                player.sendMessage("You language has been changed to: Japanese");
            }
        }
        TextPacket pk = new TextPacket();
        pk.type = TextPacket.TYPE_TRANSLATION;
        pk.isLocalized = true;
        pk.message = "Hello gay people";
        pk.source = "Gay";
        player.dataPacket(pk);
    }
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onRespond(PlayerFormRespondedEvent event) {
		final Player player = event.getPlayer();
		final FormWindow window = event.getWindow();
		if (event.getResponse() == null)
			return;
		if (window instanceof FormWindowCustom) {

			final String title = ((FormWindowCustom) event.getWindow()).getTitle();
			if (event.wasClosed() == false)
				switch (title) {

					case "Nick Menu":
                        int rankid = ((FormWindowCustom) event.getWindow()).getResponse().getDropdownResponse(1).getElementID();
						String rank = "";
						switch(rankid){
							case 0:
								rank += BladeCultist.DEFAULT;
								break;
							case 1:
								rank += BladeCultist.VIP;
								break;
							case 2:
								rank += BladeCultist.VIPPLUS;
								break;
							case 3:
								rank += BladeCultist.MVP;
								break;
							case 4:
								rank += BladeCultist.MVPPLUS;
								break;
							case 5:
								rank+= BladeCultist.MVPPLUSPLUS;
								break;
						}
						final String username = ((FormWindowCustom) event.getWindow()).getResponse().getInputResponse(2);
						if(username.equals("")) {
                            player.sendMessage(TextFormat.GOLD + "Invalid " + TextFormat.GRAY + "username has been supplied!");
                            Server.getInstance().dispatchCommand(player, "nick");
                            return;
                        }
						player.sendMessage(TextFormat.GRAY + "You have been " + TextFormat.GOLD + "Nicked " + TextFormat.GRAY + "as " + TextFormat.GOLD + rank + " " + username);
                        ((BladeCultist) player).setNick(rank, username);
						if((((FormWindowCustom) event.getWindow()).getResponse().getToggleResponse(3))){
                            player.sendMessage(TextFormat.GOLD + "Skin " + TextFormat.GRAY + "has been set to a random player's skin ");
                        }

						break;
				case "Cosmetic Menu": {
                    final int id = ((FormWindowCustom) event.getWindow()).getResponse().getDropdownResponse(0).getElementID();
                    ((BladeCultist) player).cosmeticForm(id);
                    break;
                }
				case "Report Menu":
                    final int id = ((FormWindowCustom) event.getWindow()).getResponse().getDropdownResponse(0).getElementID();
					String message = TextFormat.RED + "Error, Check with an admin";
					switch (id) {
					case 0:
						message = "Chat Reporting";
						break;
					case 1:
						message = "Hacking";
						break;
					}
					final Config config = new Config(new File(instance.getMain().getDataFolder(), "reports.yml"),
							Config.YAML);
					final String name = ((FormWindowCustom) event.getWindow()).getResponse().getInputResponse(1);
					player.sendMessage("Reported " + name + " for " + message);
					Map<String, Map<String, String>> map = new HashMap<>();
					if (config.exists("reports." + name))
						map = (Map<String, Map<String, String>>) config.get("reports." + name);
					final DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d. HH:mm:ss");
					final Date date = new Date();
					final Map<String, String> data = new HashMap<>();
					data.put("User", name);
					data.put("Date", dateFormat.format(date));
					data.put("Reporter", player.getName());
					data.put("Reason", message);
					data.put("Result", "waiting");
					map.put(UUID.randomUUID().toString(), data);
					config.set("reports." + name, map);
					config.save();
					event.getWindow().setResponse(null);

				}
		}
		if (window instanceof FormWindowSimple) {
			final int id = ((FormResponseSimple) event.getResponse()).getClickedButtonId();
			final String title = ((FormWindowSimple) event.getWindow()).getTitle();
			if (event.wasClosed() == false)
				switch (title) {
				case "Reports":
					switch (id) {
					case 0:
						NewReports(player);
						break;
					case 1:
						BannedReports(player);
						break;
					case 2:
						DeletedReports(player);
						break;

					}
					break;
				case "Styles":
					if (((FormResponseSimple) event.getResponse()).getClickedButtonId() == 0) {
						((BladeCultist) player).currentstyle.put(player.getName(), -1);
						player.sendMessage(
								TextFormat.BLUE + "Lobby> " + TextFormat.GRAY + "styles have been disabled.");
						UpdateCosmetic.clearArmor(player);
						return;
					}
					final int style = BladeCultist.costring
							.get(((FormResponseSimple) event.getResponse()).getClickedButton().getText());
					((BladeCultist) player).currentstyle.put(player.getName(), style);

					player.sendMessage(TextFormat.BLUE + "Lobby> " + TextFormat.YELLOW + ""
							+ MysteryBox.items[style].getName() + TextFormat.GRAY + " has been enabled.");
					break;

				case "Gadgets":
					if (((FormResponseSimple) event.getResponse()).getClickedButtonId() == 0) {
						((BladeCultist) player).currentgadget.put(player.getName(), -1);
						player.sendMessage(TextFormat.BLUE + "Lobby> " + TextFormat.GRAY + "gadget have been removed.");
						UpdateCosmetic.removeGadget(player);
						return;
					}
					final int gadget = BladeCultist.costring
							.get(((FormResponseSimple) event.getResponse()).getClickedButton().getText());
					((BladeCultist) player).currentgadget.put(player.getName(), gadget);
					player.sendMessage(TextFormat.BLUE + "Lobby> " + TextFormat.YELLOW + ""
							+ MysteryBox.items[gadget].getName() + TextFormat.GRAY + " has been added to inventory.");

					break;
				case "Travel":
					switch (id) {
					case 0:
						gameTypeSelector(player);
						break;
					case 1:
						preferences(player);
						break;
					case 2:
						profileMenu(player);
						break;
					case 3:
						instance.getServer().dispatchCommand(player, "games bp");
						event.getWindow().setResponse(null);
						break;
					}
					break;
				case "Game Type Selector":
					switch (id) {
					case 0:
						instance.getLobby().setLobby("main", player);
						break;
					case 1:
						instance.getLobby().setLobby("sw", player);
						break;
						case 2:
                            instance.getLobby().setLobby("sw", player);
                            break;


					}

				}
		}
	}

	private void preferences(Player player) {
		final FormWindowSimple form = new FormWindowSimple("Preferences", "");
		form.setContent(TextFormat.YELLOW.toString() + TextFormat.BOLD + "Preferences Menu coming soon!");
		player.showFormWindow(form);

	}

	private void gameTypeSelector(Player player) {
		final FormWindowSimple form = new FormWindowSimple("Game Type Selector", "");
		form.setContent("Travel to places!");
		form.addButton(new ElementButton("Back to Lobby", new ElementButtonImageData("url",
				"https://lh3.googleusercontent.com/oOr8F3Ikurl5y_IxKfsz3OQHxIX0mUAtSKtoFdQ1lpWL41px82NX7v7kSsidUFCSt6s06T3_lMJJM0MASosu=s400")));
		form.addButton(new ElementButton("Skywars"));
		form.addButton(new ElementButton("Bedwars"));
		player.showFormWindow(form);

	}

	private void profileMenu(Player player) {
		final FormWindowSimple form = new FormWindowSimple("Profile", "");
		form.setContent(TextFormat.YELLOW.toString() + TextFormat.BOLD + "Profile Menu coming soon!");
		player.showFormWindow(form);
	}

	@SuppressWarnings("unchecked")
	public void NewReports(Player player) {
		final Config config = new Config(new File(instance.getMain().getDataFolder(), "reports.yml"), Config.YAML);
		final FormWindowSimple window = new FormWindowSimple("New", null);
		window.setContent("View player reports");
		window.addButton(new ElementButton("Back", new ElementButtonImageData("url",
				"https://lh3.googleusercontent.com/oOr8F3Ikurl5y_IxKfsz3OQHxIX0mUAtSKtoFdQ1lpWL41px82NX7v7kSsidUFCSt6s06T3_lMJJM0MASosu=s400")));

		// final String name = player.getName();
		Map<String, Map<String, String>> map = new HashMap<>();
		Map<String, Map<String, Map<String, String>>> upper = new HashMap<>();
		upper = (Map<String, Map<String, Map<String, String>>>) config.get("reports");
		if (!config.exists("reports"))
			return;

		for (final Entry<String, Map<String, Map<String, String>>> up : upper.entrySet()) {
			player.sendMessage(up.toString());
			map = (Map<String, Map<String, String>>) config.get("reports." + up.getValue());
			for (final Entry<String, Map<String, String>> stuff : map.entrySet())
				if (stuff.getValue().get("Result").equals("waiting"))
					window.addButton(new ElementButton(
							stuff.getValue().get("User") + "\n" + TextFormat.RED + stuff.getValue().get("Reason")));
		}
		player.showFormWindow(window);
	}

	@SuppressWarnings("unchecked")
	public void BannedReports(Player player) {
		final Config config = new Config(new File(instance.getMain().getDataFolder(), "reports.yml"), Config.YAML);
		final FormWindowSimple window = new FormWindowSimple("Banned", null);
		window.setContent("View player reports");
		window.addButton(new ElementButton("Back", new ElementButtonImageData("url",
				"https://lh3.googleusercontent.com/oOr8F3Ikurl5y_IxKfsz3OQHxIX0mUAtSKtoFdQ1lpWL41px82NX7v7kSsidUFCSt6s06T3_lMJJM0MASosu=s400")));

		final String name = player.getName();
		Map<String, Map<String, String>> map = new HashMap<>();
		if (config.exists("reports." + name))
			map = (Map<String, Map<String, String>>) config.get("reports." + name);
		for (final Entry<String, Map<String, String>> stuff : map.entrySet())
			if (stuff.getValue().get("Result").equals("banned"))
				window.addButton(new ElementButton(
						stuff.getValue().get("User") + "\n" + TextFormat.RED + stuff.getValue().get("Reason")));
		player.showFormWindow(window);
	}

	@SuppressWarnings("unchecked")
	public void DeletedReports(Player player) {
		final Config config = new Config(new File(instance.getMain().getDataFolder(), "reports.yml"), Config.YAML);
		final FormWindowSimple window = new FormWindowSimple("Deleted", null);
		window.setContent("View player reports");
		window.addButton(new ElementButton("Back", new ElementButtonImageData("url",
				"https://lh3.googleusercontent.com/oOr8F3Ikurl5y_IxKfsz3OQHxIX0mUAtSKtoFdQ1lpWL41px82NX7v7kSsidUFCSt6s06T3_lMJJM0MASosu=s400")));
		final String name = player.getName();
		Map<String, Map<String, String>> map = new HashMap<>();
		if (config.exists("reports." + name))
			map = (Map<String, Map<String, String>>) config.get("reports." + name);
		for (final Entry<String, Map<String, String>> stuff : map.entrySet())
			if (stuff.getValue().get("Result").equals("deleted"))
				window.addButton(new ElementButton(
						stuff.getValue().get("User") + "\n" + TextFormat.RED + stuff.getValue().get("Reason")));
		player.showFormWindow(window);
	}

}
