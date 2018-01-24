package com.blademc.core.Instance;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.Vector3fEntityData;
import cn.nukkit.event.entity.EntityVehicleEnterEvent;
import cn.nukkit.event.entity.EntityVehicleExitEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.DummyBossBar;
import cn.nukkit.utils.TextFormat;
import com.blademc.core.BladeMC;
import com.blademc.core.Cosmetic.MysteryBox;
import com.blademc.core.utils.Lobby.Lobby;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BladeCultist extends Player{

    public static final String DEFAULT = TextFormat.GRAY + "[Visitor]";
    public static final String VIP = TextFormat.GREEN + "[VIP]";
    public static final String VIPPLUS = TextFormat.GREEN + "[VIP" + TextFormat.GOLD + "+" + TextFormat.GREEN + "]";
    public static final String MVP = TextFormat.AQUA + "[MVP]";
    public static final String MVPPLUS = TextFormat.AQUA + "[MVP" + TextFormat.RED + "+" + TextFormat.AQUA + "]";
    public static final String MVPPLUSPLUS = TextFormat.GOLD + "[MVP" + TextFormat.RED + "++" + TextFormat.GOLD + "]";


    public static Instance instance;

	private static final String OWNERS[] = { "uselessnora" };
	private static final String MVPPLUSPLUSS[] = { "uselessnora", "Leafysyntqx" };
	private static final String ADMINS[] = { "EpicSteve33" };
	private static final String MODSS[] = { "hopblitz420" };
	public static Map<String, Integer> costring = new HashMap<>();

	public Map<String, Integer> currentstyle = new HashMap<>();
	public Map<String, Integer> currentgadget = new HashMap<>();
	private String fakePrefix = "";
	private String fakeName = "";

	public BladeCultist(SourceInterface interfaz, Long clientID, String ip, int port) {
		super(interfaz, clientID, ip, port);
	}

	public String checkRank() {
	    if(!(fakePrefix.equals("") && fakeName.equals(""))){
	        return fakePrefix + " " + fakeName;
        }
		String rank = "";
		if (Arrays.asList(OWNERS).contains(getName()))
			rank += TextFormat.RED + "[OWNER]";
		if (Arrays.asList(MVPPLUSPLUSS).contains(getName()))
			rank += " " + TextFormat.GOLD + "[MVP" + TextFormat.RED + "++" + TextFormat.GOLD + "]";

		if (Arrays.asList(ADMINS).contains(getName()))
			rank += " " + TextFormat.RED + "ADMIN";

		if (Arrays.asList(MODSS).contains(getName()))
			rank += " " + TextFormat.YELLOW + "MOD";
		if(rank.equals(""))
		rank += TextFormat.GRAY + "Member";
		return rank + " " + this.getName();

	}

	public void fixNametag(){
		this.setNameTagAlwaysVisible();
		if(!fakeName.equals(""))
		    this.setDisplayName(fakeName);
		else
		    this.setDisplayName(checkRank());
		this.setNameTag(checkRank());
	}

	public boolean hasPower(String power) {
		if(power.equals("BLOCK")){
            return Arrays.asList(OWNERS).contains(getName()) || Arrays.asList(ADMINS).contains(getName());
        }
		return false;
	}

	public DummyBossBar getBossbar(){
        return BladeMC.plugin.getInstance().bossbar.get(this.getName());
    }

    @Override
	public void spawnTo(Player player) {
	    if(!Lobby.lobby.containsKey(this.getName())){
	        super.spawnTo(player);
	        return;
        }
		if(Lobby.lobby.get(player.getName()).equals(Lobby.lobby.get(this.getName()))){
            super.spawnTo(player);
        }
	}
	@SuppressWarnings("unchecked")
	public void cosmeticForm(int type) {

		final Config config = new Config(new File(instance.getMain().getDataFolder() + "/players/"
				+ getName().toLowerCase().charAt(0) + "/" + getName().toLowerCase(), "player.yml"), Config.YAML);
		switch (type) {
		case 0: {
			if (config.get("styles") == null) {
				this.sendMessage(TextFormat.RED + "Error, No Entries Found");
				return;
			}
			final Map<String, Boolean> map = (Map<String, Boolean>) config.get("styles");
			final FormWindowSimple form = new FormWindowSimple("Styles", "Choose an Style to enable");
			form.addButton(new ElementButton("Disable Styles", new ElementButtonImageData("url",
					"https://media-elerium.cursecdn.com/avatars/49/250/636075445633231745.png")));
			for (final Map.Entry<String, Boolean> entry : map.entrySet()) {
				form.addButton(new ElementButton(MysteryBox.items[Integer.parseInt(entry.getKey())].getName()));
				costring.put(MysteryBox.items[Integer.parseInt(entry.getKey())].getName(),
						Integer.parseInt(entry.getKey()));
			}
			showFormWindow(form);
		}
			break;
		case 1: {
			if (config.get("gadgets") == null) {
				this.sendMessage(TextFormat.RED + "Error, No Entries Found");
				return;
			}
			final Map<String, Boolean> map = (Map<String, Boolean>) config.get("gadgets");
			final FormWindowSimple form = new FormWindowSimple("Gadgets", "Choose an Gadget to play with");
			form.addButton(new ElementButton("Disable Gadgets", new ElementButtonImageData("url",
					"https://media-elerium.cursecdn.com/avatars/49/250/636075445633231745.png")));

			for (final Map.Entry<String, Boolean> entry : map.entrySet()) {
				form.addButton(new ElementButton(MysteryBox.items[Integer.parseInt(entry.getKey())].getName()));
				costring.put(MysteryBox.items[Integer.parseInt(entry.getKey())].getName(),
						Integer.parseInt(entry.getKey()));
			}
			showFormWindow(form);
		}
			break;

		case 2: {
			if (config.get("pets") == null) {
				this.sendMessage(TextFormat.RED + "Error, No Entries Found");
				return;
			}
			final Map<String, Boolean> map = (Map<String, Boolean>) config.get("pets");
			final FormWindowSimple form = new FormWindowSimple("Pets", "Choose an Fluffy Animal");
			form.addButton(new ElementButton("Disable Pets", new ElementButtonImageData("url",
					"https://media-elerium.cursecdn.com/avatars/49/250/636075445633231745.png")));

			for (final Map.Entry<String, Boolean> entry : map.entrySet()) {
				form.addButton(new ElementButton(MysteryBox.items[Integer.parseInt(entry.getKey())].getName()));
				costring.put(MysteryBox.items[Integer.parseInt(entry.getKey())].getName(),
						Integer.parseInt(entry.getKey()));
			}
			showFormWindow(form);
		}
			break;

		case 3: {
			if (config.get("particles") == null) {
				this.sendMessage(TextFormat.RED + "Error, No Entries Found");
				return;
			}
			final Map<String, Boolean> map = (Map<String, Boolean>) config.get("particles");
			final FormWindowSimple form = new FormWindowSimple("Particles", "Choose a(n) Colorful Lightshow");
			form.addButton(new ElementButton("Disable Particles", new ElementButtonImageData("url",
					"https://media-elerium.cursecdn.com/avatars/49/250/636075445633231745.png")));

			for (final Map.Entry<String, Boolean> entry : map.entrySet()) {
				form.addButton(new ElementButton(MysteryBox.items[Integer.parseInt(entry.getKey())].getName()));
				costring.put(MysteryBox.items[Integer.parseInt(entry.getKey())].getName(),
						Integer.parseInt(entry.getKey()));
			}
			showFormWindow(form);

		}
			break;
		}
	}

	public void addCosmeticItem(Item item) {

		final Config config = new Config(new File(instance.getMain().getDataFolder() + "/players/"
				+ getName().toLowerCase().charAt(0) + "/" + getName().toLowerCase(), "player.yml"), Config.YAML);
		String type = "gay";
		if (item.hasCustomBlockData())
			type = item.getCustomBlockData().getString("type");

		if (!config.exists(type)) {
			final Map<String, Boolean> map = new HashMap<>();
			config.set(type, map);
			config.save();
		}
		@SuppressWarnings("unchecked")
		final Map<String, Boolean> map = (Map<String, Boolean>) config.get(type);
		map.put(Integer.toString(item.getCustomBlockData().getInt("identify")), true);
		config.set(type, map);
		config.save();

	}

	public Integer checkData(String data) {

		final Config config = new Config(new File(BladeMC.dataFolder + "/players/"
				+ getName().toLowerCase().charAt(0) + "/" + getName().toLowerCase(), "player.yml"), Config.YAML);
		if(config.exists(data))
		return config.getInt(data);
		return null;

	}

	@Override
	public boolean entityBaseTick(int tick){
		if (riding != null) {
			mount(riding);
		}
		super.entityBaseTick(tick);
		return true;
	}

	public void setData(String key, int result) {
		final Config config = new Config(new File(BladeMC.dataFolder + "/players/"
				+ getName().toLowerCase().charAt(0) + "/" + getName().toLowerCase(), "player.yml"), Config.YAML);
		config.set(key, result);
		config.save();
	}

	public void addCoins(int coins) {

		int coin = 0 + coins;
		final Config config = new Config(new File(BladeMC.dataFolder + "/players/"
				+ getName().toLowerCase().charAt(0) + "/" + getName().toLowerCase(), "player.yml"), Config.YAML);
		if (config.exists("coins"))
			coin = (int) config.get("coins") + coins;

		int xp = (int) (coins * 0.05);
		if (config.exists("xp"))
			xp = (int) config.get("xp") + xp;
		config.set("xp", xp);
		config.set("coins", coin);
		config.save();
	}

	public Integer checkLevel() {
		return checkData("xp");

	}

    public void setNick(String s, String a) {
	    fakePrefix = s;
	    fakeName = a;
    }

   public Boolean mount(Entity entity) {
       Objects.requireNonNull(entity, "The target of the mounting entity can't be null");
       this.PitchDelta = 0.0D;
       this.YawDelta = 0.0D;
       if (entity.riding != null) {
           EntityVehicleExitEvent ev = new EntityVehicleExitEvent(entity, null);
           server.getPluginManager().callEvent(ev);
           if (ev.isCancelled()) {
               return false;
           }
           SetEntityLinkPacket pk;

           pk = new SetEntityLinkPacket();
           pk.rider = getId(); //Weird Weird Weird
           pk.riding = entity.getId();
           pk.type = 3;
           Server.broadcastPacket(this.hasSpawned.values(), pk);

           if (entity instanceof Player) {
               pk = new SetEntityLinkPacket();
               pk.rider = getId();
               pk.riding = entity.getId();
               pk.type = 3;
               ((Player) entity).dataPacket(pk);
           }

           entity.riding = null;
           linkedEntity = null;
           entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, false);
           return true;
       }
       EntityVehicleEnterEvent ev = new EntityVehicleEnterEvent(entity, null);
       server.getPluginManager().callEvent(ev);
       if (ev.isCancelled()) {
           return false;
       }

       SetEntityLinkPacket pk;

       pk = new SetEntityLinkPacket();
       pk.rider = this.getId();
       pk.riding = entity.getId();
       pk.type = 2;
       Server.broadcastPacket(this.hasSpawned.values(), pk);

       if (entity instanceof Player) {
           pk = new SetEntityLinkPacket();
           pk.rider = this.getId();
           pk.riding = 0;
           pk.type = 2;
         //  ((Player) entity).dataPacket(pk);
       }

       entity.riding = entity;
       linkedEntity = this;

       entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, true);
	   entity.setDataProperty(new Vector3fEntityData(DATA_RIDER_SEAT_POSITION,
			   new Vector3f(0, getMountedYOffset(), 0)));
       return true;
       }



}
