package com.blademc.core.utils.Lobby;

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
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.entity.EntitySpawnEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.event.player.PlayerToggleFlightEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.transaction.data.UseItemOnEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemLeggingsDiamond;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FlameParticle;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.*;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.blademc.core.API.LevelSystem.LevelSystem;
import com.blademc.core.BladeMC;
import com.blademc.core.Instance.BladeCultist;

import java.io.File;
import java.util.*;

import static cn.nukkit.utils.TextFormat.*;

public class MainLobby extends LobbyClass implements Listener {

    private Vector3 pos = new Vector3(53.5, 97.5, -1.5);
	private FloatingTextParticle[] joinParticle = {
	        new FloatingTextParticle(pos.add(0, -0.1f), "", ""),
            new FloatingTextParticle(pos.add(0, -0.6f), "", ""),
            new FloatingTextParticle(pos.add(0, -0.9f), "", ""),
            new FloatingTextParticle(pos.add(0, -1.4f), "", ""),
            new FloatingTextParticle(pos.add(0, -1.7f), "", "")

	};
    public final FloatingTextParticle[] crateParticle = new FloatingTextParticle[]{
            new FloatingTextParticle(new Vector3(102.5, 5.8, 93.5), "", " "),
            new FloatingTextParticle(new Vector3(102.5, 5.5, 93.5), "", "No Boxes"),
            new FloatingTextParticle(new Vector3(102.5, 5.2, 93.5), "", " ")};

	private final Map<String, Player> members = new HashMap<>();

	public MainLobby(){
	    ArrayList<String> ftp = new ArrayList<>();
	    ftp.add("Welcome to " + BOLD + GOLD + "BladeMC Network");
	    ftp.add("Right click your Compass to");
	    ftp.add("open the server selector!");
	    ftp.add("" + BOLD + GOLD + "STORE " + RESET + "shop.blademc.pw");
        ftp.add("" + BOLD + GOLD + "WEBSITE " + RESET + "forums.blademc.pw");
	    ArrayList<String> string = LevelSystem.center(ftp, 0);
        int index = 0;
	    for(FloatingTextParticle particle : joinParticle){
            particle.setTitle(string.get(index));
	        index++;
        }
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
		player.teleport(new Location(69.5, 98, -0.5, 90, -1, Server.getInstance().getDefaultLevel()));

	}

	private void lobbyItems(Player player) {
		final Inventory inv = player.getInventory();
		inv.clearAll();
		inv.setItem(0, Item.get(Item.BOOK));
		inv.setItem(4, Item.get(Item.COMPASS));
		inv.setItem(8, Item.get(Item.DYE));

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
                Server.getInstance().getScheduler().scheduleDelayedTask(BladeMC.plugin, () -> {
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
			if (item.getCustomBlockData().contains("games")) {
				final String data = item.getCustomBlockData().getString("games");
                BladeMC.plugin.getInstance().getLobby().setLobby(data, player);
				event.setCancelled();
			}
	}

	private void lobbySettings(Player player) {
		((BladeCultist) player).fixNametag();
		AddEntityPacket pk = new AddEntityPacket();
		pk.entityRuntimeId = 1000;
		pk.entityUniqueId = 1000;
		pk.type = 71;
		pk.x = 51.5f;
		pk.y = 94.5f;
		pk.z = -1.5f;
		pk.speedX = 0;
		pk.speedY = 0;
		pk.speedZ = 0;
		pk.pitch = 0;
		pk.yaw = 0;
		player.dataPacket(pk);
		new com.blademc.core.player.task.ElderGuardianTask(BladeMC.plugin, player,
				new Location(69.5, 98, -0.5, 90, -1, Server.getInstance().getDefaultLevel()), TextFormat.YELLOW + "-=- BladeMC -=-", TextFormat.GRAY + "Games Server")
						.runTaskLater(BladeMC.plugin, 10);
	}

	private void lobbyParticles(Player player) {
		final int count = player.getServer().getOnlinePlayers().size();
		for(FloatingTextParticle particle : joinParticle) {
		    particle.setInvisible(false);
            player.getLevel().addParticle(particle, player);
        }
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

        createCreeper(new Location(41.5, 95.2, -16.5, 271, 2), player);

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
                TextFormat.GREEN + BOLD.toString() + "Josh the Creeper");
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

		File[] files = new File(BladeMC.dataFolder + "/skins/players/").listFiles();
        Random rand = new Random();
        File file = files[rand.nextInt(Objects.requireNonNull(files).length)];
        Skin skin = new Skin(file);
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
                BladeMC.plugin.getInstance().getLobby().setLobby("mm", player);

            }
			if (id == 104)
				BladeMC.plugin.getInstance().getLobby().setLobby("sw", player);

            if (id >= 100 && id <= 120)
                strikeLighting(player);
        }
    }



    private void strikeLighting(Position pos) {
		BladeMC.plugin.getInstance().getLobby().strikeLighting(pos);
    }

	@EventHandler
	public void onItemSpawn(EntitySpawnEvent event) {
		if (event.getEntity().getLevel().getName().equals("Lobby"))
			if (event.getEntity() instanceof EntityItem)
				event.getEntity().kill();
	}

	@EventHandler
	public void onTNT(EntityExplodeEvent event) {
		if (!event.getEntity().getLevel().getName().contains("Lobby"))
			return;
		final List<Block> blocks = new ArrayList<>();
		event.setBlockList(blocks);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
        if (!members.containsKey(event.getPlayer().getName()))
            return;
        if (!event.getPlayer().hasPermission("com.blademc.break")) {
            event.setCancelled();
            event.getPlayer().sendActionBar("&7Breaking blocks are not &cAllowed".replaceAll("&", "§"));
        }
    }

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if (!members.containsKey(event.getPlayer().getName()))
			return;
			if (event.getBlock().getId() != Block.TNT) {
				if (!event.getPlayer().hasPermission("com.blademc.place")) {
                    event.setCancelled();
                    event.getPlayer().sendActionBar("&7Placing blocks are not &cAllowed".replaceAll("&", "§"));
                }
			} else if (event.getBlock().getId() == Block.TNT) {
				event.getPlayer().getInventory().removeItem(event.getPlayer().getInventory().getItemInHand());
				((BlockTNT) event.getBlock()).prime();
				event.setCancelled();
				Server.getInstance().getScheduler().scheduleDelayedTask(BladeMC.plugin, () -> event.getPlayer().getInventory().addItem(event.getPlayer().getInventory().getItemInHand()), 300);
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
                //player.sendActionBar("&7You have Pushed &o".replaceAll("&", "§") + p.getName());


            }
		}
	}


	public void removePlayer(Player player) {
		members.remove(player.getName());
		for(FloatingTextParticle particle : joinParticle) {
            particle.setInvisible();
            player.getLevel().addParticle(particle);
        }
		final FloatingTextParticle[] particles = {
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
