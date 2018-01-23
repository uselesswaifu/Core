package com.blademc.core.utils.Lobby;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockTNT;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.*;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.transaction.data.UseItemOnEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBookWritten;
import cn.nukkit.item.ItemLeggingsDiamond;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FlameParticle;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.*;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.blademc.core.API.LevelSystem.LevelSystem;
import com.blademc.core.BladeMC;
import com.blademc.core.Instance.BladeCultist;
import com.blademc.core.Instance.Instance;
import com.blademc.core.player.event.PlayerJoinEventBC;

import javax.imageio.ImageIO;

public class MainLobby extends LobbyClass implements Listener {

	private FloatingTextParticle joinParticle = new FloatingTextParticle(new Vector3(122.5, 6.5, 95.5), "", " ");
    public final FloatingTextParticle[] crateParticle = new FloatingTextParticle[]{
            new FloatingTextParticle(new Vector3(102.5, 5.8, 93.5), "", " "),
            new FloatingTextParticle(new Vector3(102.5, 5.5, 93.5), "", "No Boxes"),
            new FloatingTextParticle(new Vector3(102.5, 5.2, 93.5), "", " ")};

    public Instance instance;
	private final Map<String, Player> members = new HashMap<>();

    MainLobby(Instance instance) {
		this.instance = instance;
	}

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();
        final int level = ((BladeCultist) player).checkLevel();
        event.setFormat(TextFormat.GRAY + "" + level + " " + ((BladeCultist) player).checkRank()
                + " " + TextFormat.DARK_GRAY + "" + ">>" + TextFormat.RESET
                + " " + TextFormat.GRAY + message);
        event.setRecipients(new HashSet<>(player.level.getPlayers().values()));

    }

	public void addPlayer(Player player) {
		members.put(player.getName(), player);
		initialJoin(player);
	}

	private void initialJoin(Player player) {
		lobbyParticles(player);
		lobbySettings(player);
		lobbyEntities(player);
		lobbyItems(player);
		player.setMovementSpeed(0.25f);
		player.teleport(new Location(128, 4, 127.5, 180, -1, Server.getInstance().getDefaultLevel()));

	}

	private void lobbyItems(Player player) {
		final Inventory inv = player.getInventory();
		inv.clearAll();
		inv.setItem(1,
				Item.get(Item.COMPASS)
						.setCustomName(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Travel " + TextFormat.RESET + TextFormat.GRAY + "(Right Click)")
						.setLore(TextFormat.GRAY + "Click me to open the Cult travel, and navigate the network!")
						.setCustomBlockData(new CompoundTag().putString("MainLobby", "travel")));

        BufferedImage image = null;
        try {
            image = ImageIO.read(new URL(
                    "https://upload.wikimedia.org/wikipedia/en/2/24/Lenna.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Graphics g = Objects.requireNonNull(image).getGraphics();
        g.setFont(g.getFont().deriveFont(80f));
        g.drawString(player.getName(), 1, 300);
        g.dispose();

        try {
			BufferedImage img = ImageIO.read(new File(instance.getMain().getDataFolder() + "/skins/players/", player.getName() + ".png")).getSubimage(8,8,8,8);
            //ImageIO.write(image, "png", new File(this.instance.getMain().getDataFolder() + "/images/" + "test.png"));
			img = PlayerJoinEventBC.toBufferedImage(img.getScaledInstance(64, 64, 1));
					ImageIO.write(img, "png", new File(this.instance.getMain().getDataFolder() + "/images/" + "test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        inv.setItem(4,
				new ItemBookWritten());

		inv.setItem(7,
				Item.get(Item.ENDER_CHEST)
						.setCustomName(TextFormat.YELLOW + TextFormat.BOLD.toString() + "Cosmetic Menu " + TextFormat.RESET + TextFormat.GRAY + "(Right Click)")
						.setLore(TextFormat.GRAY + "Click to open the cosmetic menu.")
						.setCustomBlockData(new CompoundTag().putString("MainLobby", "cosmetic")));

	}

	@EventHandler
	public void onFly(PlayerToggleFlightEvent event){
    	if(event.isFlying() && members.containsKey(event.getPlayer().getName()) && event.getPlayer().getGamemode() == 0){
    	    Player player = event.getPlayer();
				Vector3 directionVector = event.getPlayer().getDirectionVector();
				player.setMotion(directionVector.multiply(2));
            player.getAdventureSettings().set(AdventureSettings.Type.ALLOW_FLIGHT, false);
            player.getAdventureSettings().update();
            event.setCancelled();
			}
		}

		@EventHandler
		public void onToggleSprint(PlayerToggleSprintEvent event){
			if(members.containsKey(event.getPlayer().getName()))
    		event.getPlayer().setMovementSpeed(0.25f);
		}

	@EventHandler
    public void onKickFly(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if(members.containsKey(event.getPlayer().getName()))
        if (event.getReason().equals(PlayerKickEvent.Reason.FLYING_DISABLED.toString())) {
            player.getAdventureSettings().set(AdventureSettings.Type.ALLOW_FLIGHT, false);
            player.getAdventureSettings().update();
            event.setCancelled();
        }
    }

	@EventHandler
	public void onJump(DataPacketReceiveEvent event){
    	if(members.containsKey(event.getPlayer().getName()))
		if(event.getPacket() instanceof PlayerActionPacket){
			PlayerActionPacket packet = (PlayerActionPacket) event.getPacket();
			Player player = event.getPlayer();
			if(packet.action == PlayerActionPacket.ACTION_JUMP && event.getPlayer().gamemode == 0){
                player.getAdventureSettings().set(AdventureSettings.Type.ALLOW_FLIGHT, true);
                player.getAdventureSettings().update();
                this.instance.getServer().getScheduler().scheduleDelayedTask(this.instance.getMain(), () -> {
                    player.getAdventureSettings().set(AdventureSettings.Type.ALLOW_FLIGHT, false);
                    player.getAdventureSettings().update();
                }, 30);
			}
		}
	}

	@EventHandler
	public void useItem(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final Item item = event.getItem();
		if (item.hasCustomBlockData())
			if (item.getCustomBlockData().contains("MainLobby")) {
				final String data = item.getCustomBlockData().getString("MainLobby");
				if (data.equals("travel"))
					Compass(player);
				if (data.equals("cosmetic"))
					Cosmetic(player);
				event.setCancelled();
			}
	}

	private void Cosmetic(Player player) {
		player.showFormWindow(instance.cosmeticMenu);

	}

	private void Compass(Player player) {
		player.showFormWindow(instance.travelMenu);

	}

	private void lobbySettings(Player player) {
		((BladeCultist) player).fixNametag();
		AddEntityPacket pk = new AddEntityPacket();
		pk.entityRuntimeId = 1000;
		pk.entityUniqueId = 1000;
		pk.type = 71;
		pk.x = 57.5f;
		pk.y = 23.5f;
		pk.z = 56.5f;
		pk.speedX = 0;
		pk.speedY = 0;
		pk.speedZ = 0;
		pk.pitch = 0;
		pk.yaw = 0;
		player.dataPacket(pk);
		new com.blademc.core.player.task.ElderGuardianTask(instance, player,
				new Location(128, 4, 127.5, 180, -1, Server.getInstance().getDefaultLevel()), TextFormat.YELLOW + "-=- BladeMC -=-", TextFormat.GRAY + "Games Server")
						.runTaskLater(instance.getMain(), 10);
	}

	private void lobbyParticles(Player player) {
		final int count = player.getServer().getOnlinePlayers().size();
		joinParticle.setText(
				"          &7play.blademc.pw&r\n        &eBladePlexus &7&lBETA&r\n             &7blademc.pw&r\n\n         &7You're in &eLobby-1&r\n &7There are &e"
						.replaceAll("&", "§") + count
						+ " &7players online&r\n\n         &7Hit an &eNPC &7to play\n             &7Stay &e&lSANE"
								.replaceAll("&", "§"));
		player.getLevel().addParticle(joinParticle, player);
		player.getLevel().addParticle(crateParticle[0]);
		crateParticle[1].setTitle(TextFormat.GRAY + Integer.toString(((BladeCultist) player).checkData("keys")) + " Boxes");
		player.getLevel().addParticle(crateParticle[1], player);
		player.getLevel().addParticle(crateParticle[2], player);
	}

	private void lobbyEntities(Player player) {

        //createWitherSkeleton(99, new Location(50.5, 25, 61.5, 269, 22), player, "Duels v1.0");
        //createWitherSkeleton(100, new Location(50.5, 25, 51.5, 269, 22), player, "Cops & Crims");
		createWitherSkeleton(101, new Location(124.5, 4, 76.5, 0, 1), player, "Murder Mystery");
        createWitherSkeleton(102, new Location(131.5, 4, 76.5, 0, 1), player, "Bedwars");

        //createWitherSkeleton(103, new Location(53.5, 24, 65.5, 269, 22), player, "Survival Games");
        createWitherSkeleton(104, new Location(122.5, 4, 77.5, 0, 1), player, "Skywars");

        //createWitherSkeleton(119, new Location(61.5, 23.5, 69.5, 269, 22), player, "Draw My Thing");
        //createWitherSkeleton(120, new Location(61.5, 23.5, 43.5, 269, 22), player, "BlockParty");

        createCreeper(new Location(133.5, 4, 95.5, 90, 1), player);

	}

    private void createCreeper(Location pos, Player player) {
        final AddEntityPacket pk = new AddEntityPacket();
        pk.entityUniqueId = (long) 200;
        pk.entityRuntimeId = (long) 200;
        pk.type = EntityCreeper.NETWORK_ID;
        pk.x = (float) pos.x;
        pk.y = (float) pos.y;
        pk.z = (float) pos.z;
        pk.yaw = (float) pos.yaw;
        pk.pitch = (float) pos.pitch;
        pk.metadata = new EntityMetadata(){
            {
                this.putLong(Entity.DATA_FLAGS,
                        1 << Entity.DATA_FLAG_POWERED
                                ^ 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG
                );
                this.putBoolean(Entity.DATA_FLAG_POWERED, true);
                this.putLong(Entity.DATA_LEAD_HOLDER_EID, -1);
            }
        };
        player.dataPacket(pk);

        final FloatingTextParticle particle1 = new FloatingTextParticle(new Vector3(pk.x, pk.y + 3, pk.z), "",
                TextFormat.AQUA + "Daily Rewards Available");

        final FloatingTextParticle particle2 = new FloatingTextParticle(new Vector3(pk.x, pk.y + 2.5, pk.z), "",
                TextFormat.GREEN + TextFormat.BOLD.toString() + "Josh the Creeper");
        player.getLevel().addParticle(particle1, player);
        player.getLevel().addParticle(particle2, player);

    }

	private void createWitherSkeleton(long eid, Location pos, Player player, String name) {
        final AddPlayerPacket pk = new AddPlayerPacket();
        UUID uuid = UUID.randomUUID();
	    pk.uuid = uuid;
        pk.username = "";
        pk.entityRuntimeId = eid;
        pk.entityUniqueId = eid;
        pk.x = (float) pos.x;
        pk.y = (float) pos.y;
        pk.z = (float) pos.z;
        pk.yaw = (float) pos.getYaw();
        pk.pitch = (float) pos.getPitch();
        pk.item = Item.get(0);
		player.dataPacket(pk);

		final FloatingTextParticle particle1 = new FloatingTextParticle(new Vector3(pk.x, pk.y + 3.5, pk.z), "",
				TextFormat.YELLOW + "CLICK TO PLAY");

		final FloatingTextParticle particle2 = new FloatingTextParticle(new Vector3(pk.x, pk.y + 3, pk.z), "",
				TextFormat.AQUA + name);
		final FloatingTextParticle particle3 = new FloatingTextParticle(new Vector3(pk.x, pk.y + 2.5, pk.z), "",
				TextFormat.GREEN + "NEW!!");
		player.getLevel().addParticle(particle1, player);
		player.getLevel().addParticle(particle2, player);
		player.getLevel().addParticle(particle3, player);

		File[] files = new File(instance.getMain().getDataFolder() + "/skins/players/").listFiles();
        Random rand = new Random();
        File file = files[rand.nextInt(Objects.requireNonNull(files).length)];

        Skin skin = new Skin(file);
        //skin.setCape(skin.new Cape(PlayerJoinEventBC.serverCape, skin.getModel()));

        PlayerListPacket listPk = new PlayerListPacket();
        listPk.type = PlayerListPacket.TYPE_ADD;
        listPk.entries = new PlayerListPacket.Entry[]{
                new PlayerListPacket.Entry(uuid, eid, name, skin)
        };
        player.dataPacket(listPk);

		final MobArmorEquipmentPacket packet = new MobArmorEquipmentPacket();
		ItemLeggingsDiamond item = new ItemLeggingsDiamond();
		item.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL));
		packet.eid = eid;
		packet.slots[0] = Item.get(Item.AIR);
		packet.slots[1] = Item.get(Item.AIR);
		packet.slots[2] = Item.get(Item.AIR);
		packet.slots[3] = Item.get(Item.AIR);
		player.dataPacket(packet);

		MobEquipmentPacket pack = new MobEquipmentPacket();
		pack.eid = eid;
		pack.item = Item.get(Item.WRITTEN_BOOK);
		pack.inventorySlot = 0;
		pack.hotbarSlot = 0;
		//player.dataPacket(pack);

        EntityEventPacket gay = new EntityEventPacket();
        gay.eid = eid;
        gay.event = EntityEventPacket.UNKNOWN1;
        player.dataPacket(gay);

    }

    @EventHandler
    private void onHitWitherSkeleton(DataPacketReceiveEvent event) {
        if(!members.containsKey(event.getPlayer().getName()))
            return;
        if (event.getPacket() instanceof InventoryTransactionPacket) {
            final InventoryTransactionPacket pk = (InventoryTransactionPacket) event.getPacket();
            if (pk.transactionType != InventoryTransactionPacket.TYPE_USE_ITEM_ON_ENTITY)
                return;
            final Player player = event.getPlayer();
            final UseItemOnEntityData useItemOnEntityData = (UseItemOnEntityData) pk.transactionData;
            final long id = useItemOnEntityData.entityRuntimeId;
            if(id == 200){
                Config config = new Config(new File(BladeMC.dataFolder + "/Settings/DailyAwards", "rewards.yml"), Config.JSON);

                if(config.exists(player.getName())) {
                    if (System.currentTimeMillis() - (double) config.get(player.getName()) >= 60000) {
						LevelSystem.DailyRewardMessage(player);
						//LevelSystem.LevelUpgradeMessage(player);
                        int strands = 8;
                        int particles = 80;
                        int radius = 10;
                        int curve = 10;
                        int rotation = (int) Math.PI / 4;
                        Location location = player.getLocation();
                        for (int i = 1; i <= strands; ++i) {
                            for (int j = 1; j <= particles; ++j) {
                                float ratio = (float) j / particles;
                                double angle = curve * ratio * 2 * Math.PI / strands + (2 * Math.PI * i / strands) + rotation;
                                double x = Math.cos(angle) * ratio * radius;
                                double z = Math.sin(angle) * ratio * radius;
                                location.getLevel().addParticle(new FlameParticle(location.add(x, 0, z)));
                                location.subtract(x, 0, z);
                            }
                        }
                                config.set(player.getName(), System.currentTimeMillis());
                        config.save();
                    }
                    else{
                        player.sendMessage("You have already redeemed the daily reward");
                    }

                }

                if(!config.exists(player.getName())){
                    player.sendMessage("You have redeem daily reward");
					LevelSystem.LevelUpgradeMessage(player);
                    config.set(player.getName(), System.currentTimeMillis());
                    config.save();
                }

            }
            if (id == 101) {
                instance.getLobby().setLobby("mm", player);

            }
			if (id == 104)
				instance.getLobby().setLobby("sw", player);

            if (id >= 100 && id <= 120)
                strikeLighting(player);
        }
    }



    private void strikeLighting(Position pos) {
        instance.getLobby().strikeLighting(pos);
    }

	@EventHandler
	public void onItemSpawn(EntitySpawnEvent event) {
		if (event.getEntity().getLevel().getName().equals("Lobby"))
			if (event.getEntity() instanceof EntityItem)
				event.getEntity().kill();
	}

	@EventHandler
	public void onTNT(EntityExplodeEvent event) {
		if (!members.containsKey(event.getEntity().getName()))
			return;
		final List<Block> blocks = new ArrayList<>();
		event.setBlockList(blocks);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
        if (!members.containsKey(event.getPlayer().getName()))
            return;
        if (((BladeCultist) event.getPlayer()).hasPower("BREAK")) {
            event.setCancelled();
            event.getPlayer().sendActionBar("&7Breaking blocks are not &cAllowed".replaceAll("&", "§"));
        }
    }

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (!members.containsKey(event.getPlayer().getName()))
			return;
			if (event.getBlock().getId() != Block.TNT) {
			    if(((BladeCultist) event.getPlayer()).hasPower("BREAK")) {
                    event.setCancelled();
                    event.getPlayer().sendActionBar("&7Placing blocks are not &cAllowed".replaceAll("&", "§"));
                }
			} else if (event.getBlock().getId() == Block.TNT) {
				event.getPlayer().getInventory().removeItem(event.getPlayer().getInventory().getItemInHand());
				((BlockTNT) event.getBlock()).prime();
				event.setCancelled();
				instance.getServer().getScheduler().scheduleDelayedTask(instance.getMain(), () -> event.getPlayer().getInventory().addItem(event.getPlayer().getInventory().getItemInHand()), 120);
			}

	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {

		if (members.containsKey(event.getEntity().getName())) {
			event.setCancelled();
			if (event instanceof EntityDamageByEntityEvent) {
                final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player))
                    return;
                Player player = (Player) e.getDamager();
                Player p = (Player) e.getEntity();
                player.sendActionBar("&7You have Pushed &o".replaceAll("&", "§") + p.getName());


            }
		}
	}


	public void removePlayer(Player player) {
		members.remove(player.getName());
		player.setMovementSpeed(0.1f);
		final FloatingTextParticle[] particles = { joinParticle,
                crateParticle[0], crateParticle[1],
                crateParticle[2] };
		for (final FloatingTextParticle particle : particles) {
			particle.setText(" ");
			player.getLevel().addParticle(particle, player);
		}
        final long[] eids = { 100};
        for (final long eid : eids) {
            final RemoveEntityPacket pk = new RemoveEntityPacket();
            pk.eid = eid;
            player.dataPacket(pk);
        }
	}
}
