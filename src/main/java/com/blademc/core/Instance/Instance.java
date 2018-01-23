package com.blademc.core.Instance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.level.particle.Particle;
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

public class  Instance implements Listener {

	BladeMC plugin;

	public Map<String, DummyBossBar> bossbar = new HashMap<>();

	public BladeCultist playerclass;

	public FormWindowSimple travelMenu;
	public FormWindowCustom cosmeticMenu;

	public FormWindowCustom serverMenu;

    public PartyCmd party;

	private Lobby lobby;

	public static String maps[] = { "Lobby", "Maps"};
	public static String servergames[] = { "Survival Games", "Skywars", "Murder Mystery", "BlockParty",
			"Draw My Thing" };

	public Instance(BladeMC plugin) {
		this.plugin = plugin;
		for (final String map : maps) {
			this.plugin.getServer().loadLevel(map);
			Level level = Server.getInstance().getLevelByName(map);
			level.setTime(0);
			level.stopTime();
			level.setRaining(false);
		}
	}

	public void load() {
		getServer().getPluginManager().registerEvents(lobby = new Lobby(this), getMain());
		serverMenu = new FormWindowCustom("Server Menu");
		serverMenu.addElement(new ElementLabel("Welcome to the BladeMC Cult!"));
		serverMenu.addElement(new ElementDropdown("Your Language", Arrays.asList("English", "Japanese")));

		travelMenu = new FormWindowSimple("Travel", "Navigate the Server");
		travelMenu.addButton(new ElementButton("Game Type Selector"));
		travelMenu.addButton(new ElementButton("Preferences"));
		travelMenu.addButton(new ElementButton("Profile Menu"));
		// for (final String game : servergames)
		// compassMenu.addButton(new ElementButton(game));

		cosmeticMenu = new FormWindowCustom("Cosmetic Menu");
		cosmeticMenu
				.addElement(new ElementDropdown("Cosmetic?", Arrays.asList("Styles", "Gadgets", "Pets", "Particles")));
		this.registerEvents();
		this.registerCommands();
	}

	public void registerEvents() {
		getServer().getPluginManager().registerEvents(new PlayerJoinEventBC(this), getMain());
		getServer().getPluginManager().registerEvents(new Cosmetics(this), getMain());
		getServer().getPluginManager().registerEvents(new FormResponseEvent(this), getMain());
		getServer().getPluginManager().registerEvents(new com.blademc.Server.ServerEvent(), getMain());
		getServer().getPluginManager().registerEvents(new PluginsCmd(), getMain());
		getServer().getPluginManager().registerEvents(new PartyChatEventBC(), getMain());

		getServer().getScheduler().scheduleRepeatingTask(getMain(), new UpdateCosmetic(this), 5, true);
		getServer().getScheduler().scheduleRepeatingTask(getMain(), new LobbyMessage(this), 60 * 20, true);
	}

    public void registerCommands() {
        getServer().getCommandMap().register("report", new ReportCmd(this));
        getServer().getCommandMap().register("nick", new NickCmd(this));
        getServer().getCommandMap().register("party",party = new PartyCmd(this));
    }



	public Server getServer() {
		return plugin.getServer();
	}

	public BladeMC getMain() {
		return plugin;
	}

	public Lobby getLobby() {
		return lobby;
	}

    public FloatingTextParticle getCrateParticle(int i) {
		return lobby.mainLobby.crateParticle[i];
    }
}
