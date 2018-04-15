package com.elissamc.api.MenuSystem.menu

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.inventory.Inventory
import cn.nukkit.inventory.InventoryType
import cn.nukkit.inventory.transaction.action.InventoryAction
import cn.nukkit.item.Item
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.TextFormat
import com.elissamc.api.MenuSystem.ChestInventory

import java.io.IOException

import cn.nukkit.utils.TextFormat.*

class CosmeticMenu : Listener {

    @EventHandler
    fun grabzItemInChest(event: InventoryTransactionEvent) {
        for (action in event.transaction.actions) {
            var inventory: ChestInventory? = null
            val data = action.sourceItem.customBlockData
            for (inv in event.transaction.inventories) {
                if (inv is ChestInventory) {
                    if (inv.owner == "cosmetic") {
                        event.setCancelled()
                        inventory = inv
                    }
                }
            }

            if (action.sourceItem.hasCustomBlockData()) {
                if (data.contains("menu")) {
                    assert(inventory != null)
                    if (data.getString("menu") == "main")
                        mainMenu(inventory!!)
                    if (data.getString("menu") == "pets")
                        petsMenu(inventory!!)
                    if (data.getString("menu") == "mounts")
                        mountsMenu(inventory!!)
                    if (data.getString("menu") == "trails")
                        trailsMenu(inventory!!)
                    if (data.getString("menu") == "suits")
                        suitsMenu(inventory!!)
                    if (data.getString("menu") == "crate")
                        crateMenu(inventory!!)
                }

                if (data.contains("pet")) {
                    assert(inventory != null)
                    if (data.getString("pet") == "clear") {
                        event.transaction.source.sendMessage(RED.toString() + "Cleared pet selection!")
                        inventory!!.close(event.transaction.source)
                    }
                }

                if (data.contains("ignore") && data.getBoolean("ignore"))
                    event.setCancelled()
            }
        }
    }

    private fun crateMenu(inventory: ChestInventory) {
        inventory.clearAll()
        inventory.setItem(13, Item.get(Item.TRIPWIRE_HOOK).setCustomName("" + BOLD + YELLOW + "1" + GRAY + " Treasure Key for" + YELLOW + " 10$").setCustomBlockData(CompoundTag().putString("command", "cosmetic addkey 1")))
        val purchase = intArrayOf(28, 29, 30, 37, 38, 39, 46, 47, 48)
        val cancel = intArrayOf(34, 35, 36, 43, 44, 45, 52, 53, 54)
        for (i in purchase) {
            inventory.setItem(i - 1, Item.get(Item.EMERALD_BLOCK).setCustomName("" + BOLD + GREEN + "PURCHASE").setCustomBlockData(CompoundTag().putBoolean("purcahse", true)))
        }
        for (i in cancel) {
            inventory.setItem(i - 1, Item.get(Item.REDSTONE_BLOCK).setCustomName("" + BOLD + RED + "CANCEL").setCustomBlockData(CompoundTag().putBoolean("purcahse", false)))
        }
    }

    private fun petsMenu(inventory: ChestInventory) {
        inventory.clearAll()
        inventory.setItem(10, Item.get(Item.RAW_PORKCHOP).setCustomName(AQUA.toString() + "Spawn" + DARK_PURPLE + " Piggy" + RESET + "\n\nOink! Oink!\n\nPermission: NO!").setCustomBlockData(CompoundTag().putString("pet", "pig")))
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED.toString() + "Main Menu").setCustomBlockData(CompoundTag().putString("menu", "main")))
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED.toString() + "Clear current pet").setCustomBlockData(CompoundTag().putString("pet", "clear")))
    }

    private fun mountsMenu(inventory: ChestInventory) {
        inventory.clearAll()
        inventory.setItem(10, Item.get(Item.DRAGON_EGG).setCustomName(AQUA.toString() + "Mount" + DARK_PURPLE + " EnderDragon" + RESET + "\n\nRawr!\n\nPermission: NO!").setCustomBlockData(CompoundTag().putString("mount", "dragon")))
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED.toString() + "Main Menu").setCustomBlockData(CompoundTag().putString("menu", "main")))
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED.toString() + "Clear current mount").setCustomBlockData(CompoundTag().putString("mount", "clear")))
    }

    private fun trailsMenu(inventory: ChestInventory) {
        inventory.clearAll()
        inventory.setItem(10, Item.get(Item.BLAZE_ROD).setCustomName(AQUA.toString() + "Trail" + DARK_PURPLE + " Redstone" + RESET + "\n\nBright! Bright!\n\nPermission: NO!").setCustomBlockData(CompoundTag().putString("mount", "dragon")))
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED.toString() + "Main Menu").setCustomBlockData(CompoundTag().putString("menu", "main")))
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED.toString() + "Clear current mount").setCustomBlockData(CompoundTag().putString("trail", "clear")))
    }

    private fun suitsMenu(inventory: ChestInventory) {
        inventory.clearAll()
        inventory.setItem(10, Item.get(Item.DRAGON_EGG).setCustomName(AQUA.toString() + "Suit" + DARK_PURPLE + " SpaceMan" + RESET + "\n\nDon't forget to blink!\n\nPermission: NO!").setCustomBlockData(CompoundTag().putString("mount", "dragon")))
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED.toString() + "Main Menu").setCustomBlockData(CompoundTag().putString("menu", "main")))
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED.toString() + "Clear current mount").setCustomBlockData(CompoundTag().putString("suit", "clear")))
    }

    companion object {

        fun showMenu(player: Player) {
            val inv = ChestInventory(player, InventoryType.DOUBLE_CHEST, "cosmetic", "Cosmetics", 54)
            mainMenu(inv)
            player.addWindow(inv)
        }

        private fun mainMenu(inv: ChestInventory) {
            inv.size = 54
            inv.clearAll()
            inv.setItem(20, Item.get(Item.BONE).setCustomName(TextFormat.colorize("&c&lPets")).setCustomBlockData(CompoundTag().putString("menu", "pets")))
            inv.setItem(21, Item.get(Item.SADDLE).setCustomName(TextFormat.colorize("&c&lMounts")).setCustomBlockData(CompoundTag().putString("menu", "mounts")))
            inv.setItem(22, Item.get(Item.BLAZE_ROD).setCustomName(TextFormat.colorize("&c&lTrails")).setCustomBlockData(CompoundTag().putString("menu", "trails")))
            inv.setItem(23, Item.get(Item.LEATHER_TUNIC).setCustomName(TextFormat.colorize("&c&lSuits")).setCustomBlockData(CompoundTag().putString("menu", "suits")))
            inv.setItem(24, Item.get(Item.ELYTRA).setCustomName(TextFormat.colorize("&c&lWings")).setCustomBlockData(CompoundTag().putString("menu", "wings")))
            inv.setItem(40, Item.get(Item.ENDER_CHEST).setCustomName(TextFormat.colorize("&5&lMystery Crate")).setLore(WHITE.toString() + "This is a Mystery Crate", WHITE.toString() + "You can win gadgets by opening one", " ", WHITE.toString() + "A key for opening it costs " + YELLOW + "10 souls").setCustomBlockData(CompoundTag().putString("menu", "crate")))

            val nbt = CompoundTag().putBoolean("ignore", true)
            inv.setItem(0, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(1, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(2, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(3, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(4, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(5, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(6, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(7, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(8, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(9, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))

            inv.setItem(18, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(27, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(17, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(26, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(35, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(42, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))

            inv.setItem(43, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(44, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(36, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(37, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(38, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(51, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(52, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(53, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(45, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(46, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
            inv.setItem(47, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt))
        }
    }
}
