package com.elissamc.listener

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityHuman
import cn.nukkit.entity.data.Skin
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.block.BlockBreakEvent
import cn.nukkit.event.block.BlockPlaceEvent
import cn.nukkit.event.entity.EntityDamageEvent
import cn.nukkit.event.player.*
import cn.nukkit.event.plugin.PluginDisableEvent
import cn.nukkit.item.Item
import cn.nukkit.level.Location
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.DoubleTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.scheduler.NukkitRunnable
import cn.nukkit.utils.DyeColor
import cn.nukkit.utils.TextFormat
import cn.nukkit.utils.TextFormat.*
import com.elissamc.ElissaMC
import com.elissamc.api.MenuSystem.menu.CosmeticMenu
import com.elissamc.api.MenuSystem.menu.GamesMenu
import com.elissamc.api.MenuSystem.menu.MusicMenu
import com.elissamc.api.MenuSystem.menu.StoreMenu
import com.elissamc.api.ParticleSystem.WeirdEffect
import com.elissamc.task.ServerTutorial.ServerTutorial
import net.holograms.Holograms
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class LobbyListener : Listener {

    private val entity: EntityHuman
    private val vanished = HashSet<UUID>()

    init {
        Server.getInstance().pluginManager.registerEvents(CosmeticMenu(), ElissaMC.plugin)
        Server.getInstance().pluginManager.registerEvents(StoreMenu(), ElissaMC.plugin)
        Server.getInstance().pluginManager.registerEvents(GamesMenu(), ElissaMC.plugin)
        Server.getInstance().pluginManager.registerEvents(MusicMenu(), ElissaMC.plugin)
        val skin = Skin(File(ElissaMC.folder, "satan.png"))
        val loc = Location(-150.5, 83.0, 329.5, Server.getInstance().defaultLevel)
        val nbt = CompoundTag()
                .putList(ListTag<DoubleTag>("Pos")
                        .add(DoubleTag("", loc.getX()))
                        .add(DoubleTag("", loc.getY()))
                        .add(DoubleTag("", loc.getZ())))
                .putList(ListTag<DoubleTag>("Motion")
                        .add(DoubleTag("", 0.0))
                        .add(DoubleTag("", 0.0))
                        .add(DoubleTag("", 0.0)))
                .putList(ListTag<FloatTag>("Rotation")
                        .add(FloatTag("", (-159.3534393310547).toFloat()))
                        .add(FloatTag("", (-4.282138347625732).toFloat())))
                .putBoolean("npc", true)

        nbt.putCompound("Skin", CompoundTag()
                .putBoolean("Transparent", false)
                .putByteArray("Data", skin.data).putString("ModelId", skin.model))
        entity = Entity.createEntity("Human", loc.getLevel().getChunk(loc.floorX shr 4, loc.floorZ shr 4), nbt) as EntityHuman

        val satan = Holograms.getHologramManager().createHologram("satan", loc.add(0.0, 0.5), true)
        satan.addLine(RED.toString() + "The Souls Collector")
        satan.addLine(WHITE.toString() + "Tutorial")
        WeirdEffect(loc).runTaskTimer(ElissaMC.plugin, 0, 2)

        val hologram = Holograms.getHologramManager().createHologram("joinLobby", Location(-145.5, 88.5, 311.5, Server.getInstance().defaultLevel), true)
        hologram.addLine(WHITE.toString() + "Welcome to" + GOLD + " ElissaMC Network")
        hologram.addLine("$GOLD%name%")
        hologram.addLine(" ")
        hologram.addLine(WHITE.toString() + "Right click your compass to")
        hologram.addLine(WHITE.toString() + "open the server selector!")
        hologram.addLine(" ")
        hologram.addLine("" + BOLD + GOLD + "STORE" + RESET + WHITE + " store.elissamc.com")
        hologram.addLine("" + BOLD + GOLD + "WEBSITE" + RESET + WHITE + " www.elissamc.com")
        hologram.addLine(GOLD.toString() + "%count%" + WHITE + " Members Online")
    }

    private fun getPlayer(uuid: UUID): Player? {
        for (player in Server.getInstance().onlinePlayers.values) {
            if (player.uniqueId === uuid) {
                return player
            }
        }
        return null
    }

    @EventHandler
    fun onPrePlayerJoin(event: PlayerLoginEvent) {
        val skin = event.player.skin
        try {
            skin.cape = skin.Cape(URL("https://i.imgur.com/1d7QY2F.png"))
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        event.player.skin = skin
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.setJoinMessage("")
        Server.getInstance().scheduler.scheduleDelayedTask(ElissaMC.plugin, object : NukkitRunnable() {
            override fun run() {
                event.player.teleport(Server.getInstance().defaultLevel.safeSpawn)
                event.player.sendTitle("" + BOLD + WHITE + "ElissaMC", YELLOW.toString() + "Winner Winner Chicken Dinner", 20, 40, 20)
                event.player.sendActionBar(GOLD.toString() + "BETA STAGE v1.0")
            }
        }, 40)
        this.initPlayer(event.player)
        if (this.vanished.size > 0) {
            for (uuid in this.vanished) {
                val vanishedPlayer = getPlayer(uuid)
                vanishedPlayer?.hidePlayer(event.player)
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onNPCEntityAttack(event: PlayerInteractEntityEvent) {
        if (event.entity.namedTag.contains("npc"))
            if (event.entity.namedTag.getBoolean("npc")) {
                ServerTutorial(event.player).runTaskTimer(ElissaMC.plugin, 0, 20 * 5)
            }
    }

    @EventHandler
    fun onPlayerHunger(event: PlayerFoodLevelChangeEvent) {
        event.foodLevel = 20
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        if (event.entity !is Player)
            return
        event.setCancelled()
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (!event.player.hasPermission("elissamc.build"))
            event.setCancelled()
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!event.player.hasPermission("elissamc.break"))
            event.setCancelled()
    }

    @EventHandler
    fun onRightClickItem(event: PlayerInteractEvent) {
        if (event.action == PlayerInteractEvent.Action.PHYSICAL)
            return
        if (!event.item.hasCustomBlockData())
            return
        if (event.item.customBlockData.getBoolean("leave")) ElissaMC.plugin.instance.gameHandler.removeGame(event.player)
        val s = event.item.customBlockData.getString("lobby")
        event.setCancelled()
        if (s == "cosmetics")
            CosmeticMenu.showMenu(event.player)
        if (s == "store")
            StoreMenu.showMenu(event.player)
        if (s == "servers")
            GamesMenu.showMenu(event.player)
        if (s == "music")
            MusicMenu.showMenu(event.player)
        if (s == "players")
            vanishPlayer(event.player)
    }

    private fun vanishPlayer(player: Player) {
        if (!vanished.contains(player.uniqueId)) {
            vanished.add(player.uniqueId)
            val item: Item = Item.get(Item.DYE, DyeColor.GRAY.dyeData).setCustomName("" + BOLD + WHITE + "Toggle Players" + RESET + DARK_GRAY + " | " + RED + "Vanished" + GRAY + " (Right Click)").setCustomBlockData(CompoundTag().putString("lobby", "players"))
            player.inventory.setItem(7, null)
            player.sendPopup(TextFormat.ITALIC.toString() + item.name)
            for (p in player.level.players.values) {
                player.hidePlayer(p)
            }
        } else {
            vanished.remove(player.uniqueId)
            val item: Item = Item.get(Item.DYE, DyeColor.LIME.dyeData).setCustomName("" + BOLD + WHITE + "Toggle Players" + RESET + DARK_GRAY + " | " + GREEN + "Visibile" + GRAY + " (Right Click)").setCustomBlockData(CompoundTag().putString("lobby", "players"))
            player.inventory.setItem(7, item)
            player.sendPopup(TextFormat.ITALIC.toString() + item.name)
            for (p in player.level.players.values) {
                player.showPlayer(p)
            }
        }
    }

    private fun initPlayer(player: Player) {
        player.setGamemode(Player.ADVENTURE)
        val inv = player.inventory
        inv.clearAll()
        inv.setItem(0, Item.get(Item.CHEST).setCustomName("" + BOLD + WHITE + "Cosmetics" + RESET + GRAY + " (Right Click)").setCustomBlockData(CompoundTag().putString("lobby", "cosmetics")))
        inv.setItem(1, Item.get(Item.BOOK).setCustomName("" + BOLD + WHITE + "Store" + RESET + GRAY + " (Right Click)").setCustomBlockData(CompoundTag().putString("lobby", "store")))
        inv.setItem(4, Item.get(Item.COMPASS).setCustomName("" + BOLD + WHITE + "Servers" + RESET + GRAY + " (Right Click)").setCustomBlockData(CompoundTag().putString("lobby", "servers")))
        inv.setItem(7, Item.get(Item.DYE, 10).setCustomName("" + BOLD + WHITE + "Toggle Players" + RESET + DARK_GRAY + " | " + GREEN + "Visible" + GRAY + " (Right Click)").setCustomBlockData(CompoundTag().putString("lobby", "players")))
        inv.setItem(8, Item.get(Item.BOOKSHELF).setCustomName("" + BOLD + WHITE + "Music" + RESET + GRAY + " (Right Click)").setCustomBlockData(CompoundTag().putString("lobby", "music")))
    }

    @EventHandler
    fun onDisable(event: PluginDisableEvent) {
        if (event.plugin === ElissaMC.plugin) {
            Server.getInstance().broadcastMessage(RED.toString() + "Entity has been removed!")
            entity.level.removeEntity(entity)
        }
    }
}
