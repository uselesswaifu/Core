package com.blademc.core.Instance;

import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.utils.DummyBossBar;
import com.blademc.core.BladeMC;
import com.blademc.core.Cosmetic.Cosmetics;
import com.blademc.core.Cosmetic.UpdateCosmetic;
import com.blademc.core.Instance.CMD.NickCmd;
import com.blademc.core.Instance.CMD.PartyCmd;
import com.blademc.core.Instance.CMD.PluginsCmd;
import com.blademc.core.Instance.CMD.ReportCmd;
import com.blademc.core.player.event.FormResponseEvent;
import com.blademc.core.player.event.PartyChatEventBC;
import com.blademc.core.player.event.PlayerJoinEventBC;
import com.blademc.core.utils.Lobby.Lobby;
import com.blademc.core.utils.Lobby.LobbyMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class  Instance implements Listener {

    private BladeMC mommy;
	public static Map<String, DummyBossBar> bossbar = new HashMap<>();

	public static FormWindowSimple travelMenu;
	public static FormWindowCustom cosmeticMenu;
	public static FormWindowCustom serverMenu;

    public PartyCmd party;

	private Lobby lobby;

	private static String maps[] = { "Lobby", "Maps"};

	public Instance(BladeMC mommy) {
	    this.mommy = mommy;
		for (final String map : maps) {
			Server.getInstance().loadLevel(map);
			Level level = Server.getInstance().getLevelByName(map);
			level.setRainTime(9999);
			level.setRaining(false);
			level.setThundering(false);
			level.stopTime();
		}
	}

	public void load() {
        mommy.getServer().getPluginManager().registerEvents(lobby = new Lobby(), BladeMC.plugin);
		this.registerEvents();
		this.registerForms();
		this.registerCommands();
	}

    private void registerForms() {
        serverMenu = new FormWindowCustom("Server Menu");
        serverMenu.addElement(new ElementLabel("Welcome to the BladeMC Cult!"));
        serverMenu.addElement(new ElementDropdown("Your Language", Arrays.asList("English", "Japanese")));

        travelMenu = new FormWindowSimple("Travel", "Navigate the Server");
        travelMenu.addButton(new ElementButton("Game Type Selector"));
        travelMenu.addButton(new ElementButton("Preferences"));
        travelMenu.addButton(new ElementButton("Profile Menu"));

        cosmeticMenu = new FormWindowCustom("Cosmetic Menu");
        cosmeticMenu.addElement(new ElementDropdown("Cosmetic?", Arrays.asList("Styles", "Gadgets", "Pets", "Particles")));
    }

    private void registerEvents() {
        mommy.getServer().getPluginManager().registerEvents(new PlayerJoinEventBC(this), BladeMC.plugin);
        mommy.getServer().getPluginManager().registerEvents(new Cosmetics(), BladeMC.plugin);
        mommy.getServer().getPluginManager().registerEvents(new FormResponseEvent(), BladeMC.plugin);
        mommy.getServer().getPluginManager().registerEvents(new PluginsCmd(), BladeMC.plugin);
        mommy.getServer().getPluginManager().registerEvents(new PartyChatEventBC(), BladeMC.plugin);

        mommy.getServer().getScheduler().scheduleRepeatingTask(BladeMC.plugin, new UpdateCosmetic(), 5, true);
        mommy.getServer().getScheduler().scheduleRepeatingTask(BladeMC.plugin, new LobbyMessage(), 60 * 20, true);
	}

    private void registerCommands() {
        mommy.getServer().getCommandMap().register("report", new ReportCmd(this));
        mommy.getServer().getCommandMap().register("nick", new NickCmd(this));
        mommy.getServer().getCommandMap().register("party",party = new PartyCmd());
    }


	public Lobby getLobby() {
		return lobby;
	}

    public FloatingTextParticle getCrateParticle(int i) {
		return lobby.mainLobby.crateParticle[i];
    }
}
