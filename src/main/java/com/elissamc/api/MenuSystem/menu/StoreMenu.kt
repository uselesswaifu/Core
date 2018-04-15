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
import com.elissamc.api.MenuSystem.ChestInventory
import net.buycraft.plugin.client.ApiException
import net.buycraft.plugin.data.Category
import net.buycraft.plugin.data.Package
import net.buycraft.plugin.nukkit.BuycraftPlugin

import java.io.IOException

import cn.nukkit.utils.TextFormat.*

class StoreMenu : Listener {

    @EventHandler
    fun grabzItemInChest(event: InventoryTransactionEvent) {
        for (action in event.transaction.actions) {
            var inventory: ChestInventory? = null
            val data = action.sourceItem.customBlockData
            for (inv in event.transaction.inventories) {
                if (inv is ChestInventory) {
                    if (inv.owner == "store") {
                        event.setCancelled()
                        inventory = inv
                    }
                }
            }

            if (action.sourceItem.hasCustomBlockData()) {
                assert(inventory != null)
                if (data.contains("category")) {
                    val name = data.getString("category")
                    sendPackages(inventory!!, name)
                }

                if (data.contains("package")) {
                    val name = data.getString("package")
                    sendInfo(event.transaction.source, inventory!!, name)
                }

                if (data.contains("ignore") && data.getBoolean("ignore"))
                    event.setCancelled()
                if (data.contains("main") && data.getBoolean("main"))
                    mainMenu(inventory!!)
            }
        }
    }

    private fun sendPackages(inventory: ChestInventory, name: String) {
        inventory.clearAll()
        try {
            val cat = (Server.getInstance().pluginManager.getPlugin("BuycraftX") as BuycraftPlugin).apiClient.retrieveListing().categories
            for (c in cat) {
                if (c.name == name) {
                    for (p in c.packages) {
                        inventory.setItem(22, Item.get(Item.BOOK_AND_QUILL).setCustomName("" + BOLD + RED + "CANCEL").setCustomBlockData(CompoundTag().putBoolean("main", true)))
                        inventory.setItem(p.order, Item.get(Item.PAPER).setCustomName("Package: " + p.name).setCustomBlockData(CompoundTag().putString("package", p.name)))
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ApiException) {
            e.printStackTrace()
        }

    }

    private fun sendInfo(player: Player, inventory: ChestInventory, name: String) {
        try {
            val cat = (Server.getInstance().pluginManager.getPlugin("BuycraftX") as BuycraftPlugin).apiClient.retrieveListing().categories
            for (c in cat) {
                for (p in c.packages) {
                    if (p.name == name) {
                        inventory.close(player)
                        val salestatus = if (p.sale.isActive) p.sale.discount.toString() else "NO"
                        player.sendMessage("" + GRAY + p.name + ", " + YELLOW + "PRICE: " + GRAY + p.price + " EFFECTIVE PRICE: " + p.effectivePrice + " SALE: " + salestatus)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ApiException) {
            e.printStackTrace()
        }

    }

    companion object {

        fun showMenu(player: Player) {
            val inv = ChestInventory(player, InventoryType.CHEST, "store", "Buycraft: Categories", 9)
            mainMenu(inv)
            player.addWindow(inv)
        }

        private fun mainMenu(inv: ChestInventory) {
            inv.size = 9
            try {
                val cat = (Server.getInstance().pluginManager.getPlugin("BuycraftX") as BuycraftPlugin).apiClient.retrieveListing().categories
                for (c in cat) {
                    inv.setItem(c.order, Item.get(Item.NAME_TAG).setCustomName("Category: " + c.name).setCustomBlockData(CompoundTag().putString("category", c.name)))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ApiException) {
                e.printStackTrace()
            }

        }
    }
}
