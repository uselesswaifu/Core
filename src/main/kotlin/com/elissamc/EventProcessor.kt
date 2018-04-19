package com.elissamc


import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.item.EntityMinecartAbstract
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.block.BlockBreakEvent
import cn.nukkit.event.block.BlockGrowEvent
import cn.nukkit.event.block.BlockPlaceEvent
import cn.nukkit.event.entity.EntityDamageByBlockEvent
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.inventory.InventoryClickEvent
import cn.nukkit.event.player.*
import cn.nukkit.event.plugin.PluginDisableEvent
import cn.nukkit.level.Location
import cn.nukkit.utils.TextFormat
import com.elissamc.area.AreaFlag
import com.elissamc.area.Areas
import com.elissamc.core.StorageEngine
import com.elissamc.menu.InventoryMenu

class EventProcessor : Listener {
    init {
        Server.getInstance().pluginManager.registerEvents(this, ElissaMC.plugin)
    }

    @EventHandler
    private fun onVehicleDestroyed(event: EntityDamageByEntityEvent) {
        if (event.entity is EntityMinecartAbstract) {
            val area = Areas.findArea(event.entity.location)
            if (area != null) {
                if (event.damager is Player) {
                    if (!(event.damager as Player).isOp) {
                        event.isCancelled = true
                    }
                }
            }
        }
    }

    @EventHandler
    private fun onGrow(event: BlockGrowEvent) {
        val area = Areas.findArea(event.block.location)
        if (area != null) {
            event.isCancelled = true
        }
    }

    @EventHandler
    private fun onBlockBreak(event: BlockBreakEvent) {
        if (this.hasPermission(event.block.location, event.player,
                        AreaFlag.BLOCK_BREAK))
            event.isCancelled = true
    }

    @EventHandler
    private fun onPlayerRespawn(event: PlayerRespawnEvent) {
        event.player.teleport(ElissaMC.core.getLobbyLocation())
    }

    @EventHandler
    private fun onBlockPlace(event: BlockPlaceEvent) {
        if (this.hasPermission(event.block.location, event.player,
                        AreaFlag.BLOCK_PLACE))
            event.isCancelled = true
    }

    @EventHandler
    private fun onPlayerDropItem(event: PlayerDropItemEvent) {
        if (this.hasPermission(event.player.location, event.player,
                        AreaFlag.PLAYER_DROPITEM))
            event.isCancelled = true
    }

    @EventHandler
    private fun onPlayerDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.entity is Player)
            if (this.hasPermission(event.entity.location,
                            event.entity as Player, AreaFlag.PLAYER_GETDAMAGE))
                event.isCancelled = true

        if (event.damager is Player)
            if (this.hasPermission(event.damager.location,
                            event.damager as Player, AreaFlag.PLAYER_DODAMAGE))
                event.isCancelled = true
    }

    @EventHandler
    private fun onPlayerDamageByBlock(event: EntityDamageByBlockEvent) {
        if (event.entity is Player)
            if (this.hasPermission(event.entity.location,
                            event.entity as Player, AreaFlag.PLAYER_GETDAMAGE))
                event.isCancelled = true
    }

    @EventHandler
    private fun onPlayerDamage(event: EntityDamageEvent) {
        if (event.entity is Player)
            if (this.hasPermission(event.entity.location,
                            event.entity as Player, AreaFlag.PLAYER_GETDAMAGE))
                event.isCancelled = true
    }

    @EventHandler
    private fun onPlayerPortal(event: PlayerMoveEvent) {
        // Pass the event further...
        if (event.from.distance(event.to) > 0)
            StorageEngine.gateEnter(event.player, event.player.location)
    }

    @EventHandler
    private fun onInventoryClick(event: InventoryClickEvent) {
        if (event.inventory.holder is InventoryMenu) {
            if (event.player != null) {
                (event.inventory.holder as InventoryMenu).inventoryClick(
                        event.player, event.slot)
                event.isCancelled = true
                if ((event.inventory.holder as InventoryMenu).shouldClose(event.slot)) {
                    event.inventory.close(event.player)
                }
            }
        }
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        // Load profile to memory or create empty profile.
        StorageEngine.loadProfile(event.player.uniqueId)
        event.player.setCheckMovement(false)
    }

    @EventHandler
    private fun onPlayerLeave(event: PlayerQuitEvent) {
        // Leave party.
        if (StorageEngine.getProfile(event.player.uniqueId).party != null) {
            StorageEngine.getProfile(event.player.uniqueId).party!!.removePlayer(event.player)
            StorageEngine.getProfile(event.player.uniqueId).party = null
        }

        // Force save of player's profile.
        StorageEngine.saveProfile(event.player.uniqueId)
    }

    private fun hasPermission(location: Location, player: Player, flag: AreaFlag): Boolean {
        val area = Areas.findArea(location)
        if (area != null) {
            if ((!area.getPlayerFlag(flag, player.uniqueId)!!)) {
                player.player.sendMessage(TextFormat.BOLD.toString() + TextFormat.GRAY + "(" + TextFormat.RED + "!" + TextFormat.GRAY + ") " + TextFormat.RESET +
                        TextFormat.GRAY + "You don't have permission for " + TextFormat.YELLOW + TextFormat.ITALIC + "'"
                        + flag.toString() + "'" + TextFormat.RESET + TextFormat.GRAY + " in this area!")
                return true
            }
            return false
        }
        return false
    }

    @EventHandler
    fun onDisable(event: PluginDisableEvent) {
        if (event.plugin === ElissaMC.plugin) {
            StorageEngine.saveProfiles()
        }
    }
}