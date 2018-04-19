package com.elissamc;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.item.EntityMinecartAbstract;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityPortalEnterEvent;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.event.plugin.PluginDisableEvent;
import cn.nukkit.level.Location;
import com.elissamc.area.AreaFlag;
import com.elissamc.area.Areas;
import com.elissamc.area.ProtectedArea;
import com.elissamc.core.StorageEngine;
import com.elissamc.menu.InventoryMenu;

public class EventProcessor implements Listener {
    public EventProcessor() {
        Server.getInstance().getPluginManager().registerEvents(this, ElissaMC.plugin);
    }

    @EventHandler
    private void onVehicleDestroyed(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof EntityMinecartAbstract) {
            ProtectedArea area = Areas.findArea(event.getEntity().getLocation());
            if (area != null) {
                if (event.getDamager() instanceof Player) {
                    if (!((Player) event.getDamager()).isOp()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    private void onGrow(final BlockGrowEvent event) {
        ProtectedArea area = Areas.findArea(event.getBlock().getLocation());
        if (area != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockBreak(final BlockBreakEvent event) {
        if (!this.hasPermission(event.getBlock().getLocation(), event.getPlayer(),
                AreaFlag.BLOCK_BREAK))
            event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerRespawn(final PlayerRespawnEvent event) {
        event.getPlayer().teleport(ElissaMC.core.getLobbyLocation());
    }

    @EventHandler
    private void onBlockPlace(final BlockPlaceEvent event) {
        if (!this.hasPermission(event.getBlock().getLocation(), event.getPlayer(),
                AreaFlag.BLOCK_PLACE))
            event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDropItem(final PlayerDropItemEvent event) {
        if (!this.hasPermission(event.getPlayer().getLocation(), event.getPlayer(),
                AreaFlag.PLAYER_DROPITEM))
            event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player)
            if (!this.hasPermission(event.getEntity().getLocation(),
                    (Player) event.getEntity(), AreaFlag.PLAYER_GETDAMAGE))
                event.setCancelled(true);

        if (event.getDamager() instanceof Player)
            if (!this.hasPermission(event.getDamager().getLocation(),
                    (Player) event.getDamager(), AreaFlag.PLAYER_DODAMAGE))
                event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDamageByBlock(final EntityDamageByBlockEvent event) {
        if (event.getEntity() instanceof Player)
            if (!this.hasPermission(event.getEntity().getLocation(),
                    (Player) event.getEntity(), AreaFlag.PLAYER_GETDAMAGE))
                event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player)
            if (!this.hasPermission(event.getEntity().getLocation(),
                    (Player) event.getEntity(), AreaFlag.PLAYER_GETDAMAGE))
                event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerPortal(final EntityPortalEnterEvent event) {
        // Pass the event further...
        StorageEngine.gateEnter((Player) event.getEntity(), event.getEntity().getLocation());
    }

    @EventHandler
    private void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof InventoryMenu) {
            if (event.getPlayer() != null) {
                ((InventoryMenu) event.getInventory().getHolder()).inventoryClick(
                        (Player) event.getPlayer(), event.getSlot());
                event.setCancelled(true);
                if (((InventoryMenu) event.getInventory().getHolder()).shouldClose(event.getSlot())) {
                    event.getInventory().close(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        // Load profile to memory or create empty profile.
        StorageEngine.loadProfile(event.getPlayer().getUniqueId());
        event.getPlayer().setCheckMovement(false);
    }

    @EventHandler
    private void onPlayerLeave(final PlayerQuitEvent event) {
        // Leave party.
        if (StorageEngine.getProfile(event.getPlayer().getUniqueId()).getParty() != null) {
            StorageEngine.getProfile(event.getPlayer().getUniqueId())
                    .getParty()
                    .removePlayer(event.getPlayer());
            StorageEngine.getProfile(event.getPlayer().getUniqueId()).setParty(null);
        }

        // Force save of player's profile.
        StorageEngine.saveProfile(event.getPlayer().getUniqueId());
    }

    private boolean hasPermission(final Location location, final Player player,
                                  final AreaFlag flag) {
        ProtectedArea area = Areas.findArea(location);
        if (area != null) {
            if (!area.getPlayerFlag(flag, player.getUniqueId())) {
                player.getPlayer().sendMessage(
                        "You don't have permission for '"
                                + flag.toString() + "' in this area!");
                return false;
            }
            return true;
        }
        return true;
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (event.getPlugin() == ElissaMC.plugin) {
            StorageEngine.saveProfiles();
        }
    }
}