package com.elissamc.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.plugin.PluginDisableEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.scheduler.NukkitRunnable;
import com.blademc.uselesswaifu.FloatingPassage;
import com.blademc.uselesswaifu.object.CraftParticle;
import com.elissamc.ElissaMC;
import com.elissamc.api.MenuSystem.menu.CosmeticMenu;
import com.elissamc.api.ParticleSystem.WeirdEffect;
import com.elissamc.task.ServerTutorial.ServerTutorial;

import java.io.File;

import static cn.nukkit.utils.TextFormat.*;

public class LobbyListener implements Listener {

    private EntityHuman entity;

    public LobbyListener() {
        Server.getInstance().getPluginManager().registerEvents(new CosmeticMenu(), ElissaMC.plugin);
        Skin skin = new Skin(new File(ElissaMC.dataFolder, "satan.png"));
        Location loc = new Location(-150.5, 83, 329.5, Server.getInstance().getDefaultLevel());
        final CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", loc.getX()))
                        .add(new DoubleTag("", loc.getY()))
                        .add(new DoubleTag("", loc.getZ())))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", (float) -159.3534393310547))
                        .add(new FloatTag("", (float) -4.282138347625732)))
                .putBoolean("npc", true);

        nbt.putCompound("Skin", new CompoundTag()
                .putBoolean("Transparent", false)
                .putByteArray("Data", skin.getData()).putString("ModelId", skin.getModel()));
        entity = (EntityHuman) Entity.createEntity("Human", loc.getLevel().getChunk(loc.getFloorX() >> 4, loc.getFloorZ() >> 4), nbt);

        CraftParticle satan = FloatingPassage.getHologramManager().createHologram("satan", loc.add(0, 0.5), true);
        satan.addLine(RED + "The Souls Collector");
        satan.addLine(WHITE + "Tutorial");
        new WeirdEffect(loc).runTaskTimer(ElissaMC.plugin, 0, 2);

        CraftParticle hologram = FloatingPassage.getHologramManager().createHologram("joinLobby", new Location(-145.5, 88.5, 311.5, Server.getInstance().getDefaultLevel()), true);
        hologram.addLine(WHITE + "Welcome to" + GOLD + " ElissaMC Network");
        hologram.addLine("" + GOLD + "%name%");
        hologram.addLine(" ");
        hologram.addLine(WHITE + "Right click your compass to");
        hologram.addLine(WHITE + "open the server selector!");
        hologram.addLine(" ");
        hologram.addLine("" + BOLD + GOLD + "STORE" + RESET + WHITE + " store.elissamc.com");
        hologram.addLine("" + BOLD + GOLD + "WEBSITE" + RESET + WHITE + " www.elissamc.com");
        hologram.addLine(GOLD + "%count%" + WHITE + " Members Online");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        Server.getInstance().getScheduler().scheduleDelayedTask(ElissaMC.plugin, new NukkitRunnable() {
            @Override
            public void run() {
                event.getPlayer().sendTitle("" + BOLD + WHITE + "ElissaMC", YELLOW + "Winner Winner Chicken Dinner", 20, 40, 20);
                event.getPlayer().sendActionBar(GOLD + "BETA STAGE v1.0");
            }
        }, 40);
        this.initPlayer(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onNPCEntityAttack(PlayerInteractEntityEvent event) {
        if (event.getEntity().namedTag.contains("npc"))
            if (event.getEntity().namedTag.getBoolean("npc")) {
                new ServerTutorial(event.getPlayer()).runTaskTimer(ElissaMC.plugin, 0, 20 * 5);
            }
    }

    @EventHandler
    public void onPlayerHunger(PlayerFoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        event.setCancelled();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("elissamc.build"))
            event.setCancelled();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("elissamc.break"))
            event.setCancelled();
    }

    @EventHandler
    public void onRightClickItem(PlayerInteractEvent event) {
        if (event.getAction() == PlayerInteractEvent.Action.PHYSICAL)
            return;
        if (!event.getItem().hasCustomBlockData())
            return;
        String s = event.getItem().getCustomBlockData().getString("lobby");
        event.setCancelled();
        if (s.equals("cosmetics"))
            CosmeticMenu.showMenu(event.getPlayer());
    }

    private void initPlayer(Player player) {
        player.setGamemode(Player.ADVENTURE);
        PlayerInventory inv = player.getInventory();
        inv.clearAll();
        inv.setItem(0, Item.get(Item.CHEST).setCustomName("" + BOLD + WHITE + "Cosmetics" + RESET + GRAY + " (Right Click)").setCustomBlockData(new CompoundTag().putString("lobby", "cosmetics")));
        inv.setItem(1, Item.get(Item.BOOK).setCustomName("" + BOLD + WHITE + "Store" + RESET + GRAY + " (Right Click)").setCustomBlockData(new CompoundTag().putString("lobby", "store")));
        inv.setItem(4, Item.get(Item.COMPASS).setCustomName("" + BOLD + WHITE + "Servers" + RESET + GRAY + " (Right Click)").setCustomBlockData(new CompoundTag().putString("lobby", "servers")));
        inv.setItem(7, Item.get(Item.DYE, 10).setCustomName("" + RED + "Players" + YELLOW + " » " + WHITE + "Visible" + GRAY + " (Right Click)").setCustomBlockData(new CompoundTag().putString("lobby", "players")));
        inv.setItem(8, Item.get(Item.BOOKSHELF).setCustomName("" + BOLD + WHITE + "Music" + RESET + GRAY + " (Right Click)").setCustomBlockData(new CompoundTag().putString("lobby", "music")));
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin() == ElissaMC.plugin) {
            Server.getInstance().broadcastMessage(RED + "Entity has been removed!");
            entity.level.removeEntity(entity);
        }
    }
}