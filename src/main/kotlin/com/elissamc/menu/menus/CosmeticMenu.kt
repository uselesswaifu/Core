package com.elissamc.menu.menus

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.inventory.InventoryType
import cn.nukkit.item.Item
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.TextFormat
import cn.nukkit.utils.TextFormat.*
import com.elissamc.menu.ChestInventory

class CosmeticMenu : Listener {

    @EventHandler
    fun grabzItemInChest(event: InventoryTransactionEvent) {
        for (action in event.transaction.actions) {
            lateinit var inventory: ChestInventory
            val data = action.sourceItem.customBlockData
            for (inv in event.transaction.inventories) {
                if (inv is ChestInventory) {
                    if (inv.getOwner() == "cosmetic") {
                        event.setCancelled()
                        inventory = inv
                    }
                }
            }

            if (action.sourceItem.hasCustomBlockData()) {
                val player: Player = event.transaction.source
                if (data.contains("menu")) {
                    if (data.getString("menu") == "main")
                        mainMenu(inventory)
                    if (data.getString("menu") == "pets")
                        petsMenu(inventory, player)
                    if (data.getString("menu") == "mounts")
                        mountsMenu(inventory, player)
                    if (data.getString("menu") == "trails")
                        trailsMenu(inventory, player)
                    if (data.getString("menu") == "suits")
                        suitsMenu(inventory, player)
                    if(data.getString("menu") == "wings")
                        wingsMenu(inventory, player)
                    if (data.getString("menu") == "crate")
                        crateMenu(inventory)
                    event.setCancelled()
                }

                if (data.contains("pet")) {
                    if (data.getString("pet") == "clear") {
                        event.transaction.source.sendMessage(RED.toString() + "Cleared pet selection!")
                        mainMenu(inventory)
                    }
                }

                if (data.contains("ignore") && data.getBoolean("ignore"))
                    event.setCancelled()
                if(data.contains("purchase")) {
                    if (data.getBoolean("purchase"))
                        player.sendMessage("Officially bought a crate (doesn't exist yet!)")
                    else
                        player.sendMessage(RED.toString() + "Cancelled Purchase for Treasure Key.")
                    inventory.close(player)
                }
            }
        }
    }

    private fun crateMenu(inventory: ChestInventory) {
        inventory.clearAll()
        inventory.setItem(13, Item.get(Item.TRIPWIRE_HOOK).setCustomName("" + BOLD + YELLOW + "1" + GRAY + " Treasure Key for" + YELLOW + " 10$").setCustomBlockData(CompoundTag().putString("command", "cosmetic addkey 1")))
        val purchase = intArrayOf(28, 29, 30, 37, 38, 39, 46, 47, 48)
        val cancel = intArrayOf(34, 35, 36, 43, 44, 45, 52, 53, 54)
        for (i in purchase) {
            inventory.setItem(i - 1, Item.get(Item.EMERALD_BLOCK).setCustomName("" + BOLD + GREEN + "PURCHASE").setCustomBlockData(CompoundTag().putBoolean("purchase", true)))
        }
        for (i in cancel) {
            inventory.setItem(i - 1, Item.get(Item.REDSTONE_BLOCK).setCustomName("" + BOLD + RED + "CANCEL").setCustomBlockData(CompoundTag().putBoolean("purchase", false)))
        }
    }

    private fun petsMenu(inventory: ChestInventory, player: Player) {
        inventory.clearAll()
        inventory.setItem(10, Item.get(Item.RAW_PORKCHOP).setCustomName(itemName(AQUA.toString() + "Spawn" + DARK_PURPLE + " Piggy", "Oink! Oink!", "pig", player)).setCustomBlockData(CompoundTag().putString("pet", "pig")))
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED.toString() + "Main Menu").setCustomBlockData(CompoundTag().putString("menu", "main")))
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED.toString() + "Clear current pet").setCustomBlockData(CompoundTag().putString("pet", "clear")))
    }

    private fun mountsMenu(inventory: ChestInventory, player: Player) {
        inventory.clearAll()
        inventory.setItem(10, Item.get(Item.DRAGON_EGG).setCustomName(itemName(AQUA.toString() + "Mount" + DARK_PURPLE + " EnderDragon", "Rawr!", "enderdragon", player)).setCustomBlockData(CompoundTag().putString("mount", "dragon")))
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED.toString() + "Main Menu").setCustomBlockData(CompoundTag().putString("menu", "main")))
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED.toString() + "Clear current mount").setCustomBlockData(CompoundTag().putString("mount", "clear")))
    }

    private fun trailsMenu(inventory: ChestInventory, player: Player) {
        inventory.clearAll()
        inventory.setItem(10, Item.get(Item.BLAZE_ROD).setCustomName(itemName(AQUA.toString() + "Trail" + DARK_PURPLE + " Redstone", "Bright! Bright!", "redstone", player)).setCustomBlockData(CompoundTag().putString("mount", "dragon")))
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED.toString() + "Main Menu").setCustomBlockData(CompoundTag().putString("menu", "main")))
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED.toString() + "Clear current mount").setCustomBlockData(CompoundTag().putString("trail", "clear")))
    }

    private fun suitsMenu(inventory: ChestInventory, player: Player) {
        inventory.clearAll()
        inventory.setItem(10, Item.get(Item.DRAGON_EGG).setCustomName(itemName(AQUA.toString() + "Suit" + DARK_PURPLE + " SpaceMan", "Don't forget to blink!", "spaceman", player)).setCustomBlockData(CompoundTag().putString("mount", "dragon")))
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED.toString() + "Main Menu").setCustomBlockData(CompoundTag().putString("menu", "main")))
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED.toString() + "Clear current mount").setCustomBlockData(CompoundTag().putString("suit", "clear")))
    }

    private fun wingsMenu(inventory: ChestInventory, player: Player) {
        inventory.clearAll()
        inventory.setItem(10, Item.get(Item.DRAGON_EGG).setCustomName(itemName(AQUA.toString() + "Wing" + DARK_PURPLE + " SpaceMan", "Don't forget to blink!", "spaceman", player)).setCustomBlockData(CompoundTag().putString("mount", "dragon")))
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED.toString() + "Main Menu").setCustomBlockData(CompoundTag().putString("menu", "main")))
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED.toString() + "Clear current mount").setCustomBlockData(CompoundTag().putString("suit", "clear")))
    }

    private fun itemName(name: String, subname: String, perm: String, player: Player): String {
        val builder = StringBuilder()
        builder.append(name + RESET + "\n\n")
        builder.append(subname + RESET + "\n\n")
        val p: String = if (player.hasPermission("elissamc.cosmetic.$perm"))
            GREEN.toString() + "YES"
        else
            RED.toString() + "NO"
        builder.append(YELLOW.toString() + "Permission: " + p)
        return builder.toString()
    }

    companion object {

        fun showMenu(player: Player) {
            val inv = ChestInventory(player, InventoryType.DOUBLE_CHEST, "cosmetic", "Cosmetics")
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