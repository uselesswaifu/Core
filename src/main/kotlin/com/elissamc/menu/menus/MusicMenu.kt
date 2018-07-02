package com.elissamc.menu.menus

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.inventory.InventoryType
import cn.nukkit.item.Item
import cn.nukkit.nbt.tag.CompoundTag
import com.elissamc.menu.ChestInventory

class MusicMenu : Listener {

    @EventHandler
    fun grabzItemInChest(event: InventoryTransactionEvent) {
        for (action in event.transaction.actions) {
            var inventory: ChestInventory? = null
            val data = action.sourceItem.customBlockData
            for (inv in event.transaction.inventories) {
                if (inv is ChestInventory) {
                    if (inv.getOwner() == "music") {
                        event.setCancelled()
                        inventory = inv
                    }
                }
            }

            if (action.sourceItem.hasCustomBlockData()) {
                assert(inventory != null)
                val player = event.transaction.source
                if (data.contains("song")) {
                    val name = data.getString("song")
                    inventory!!.close(player)
                }
            }
        }
    }

    companion object {

        fun showMenu(player: Player) {
            val inv = ChestInventory(player, InventoryType.CHEST, "music", "Jukebox")
            mainMenu(inv)
            player.addWindow(inv)
        }

        private fun mainMenu(inv: ChestInventory) {
            inv.setItem(0, Item.get(Item.RECORD_CAT).setCustomBlockData(CompoundTag().putString("song", "cat")))

            inv.setItem(18, Item.get(Item.BEDROCK).setCustomName("Stop Song").setCustomBlockData(CompoundTag().putString("action", "stop")))
            inv.setItem(20, Item.get(Item.FIRE_CHARGE).setCustomName("Random Song").setCustomBlockData(CompoundTag().putString("action", "random")))
            inv.setItem(21, Item.get(Item.BEACON).setCustomName("Change volume").setCustomBlockData(CompoundTag().putString("action", "volume")))
            inv.setItem(23, Item.get(Item.SIGN).setCustomName("Start on login").setCustomBlockData(CompoundTag().putString("action", "login")))
            inv.setItem(24, Item.get(Item.BLAZE_POWDER).setCustomName("Shuffle").setCustomBlockData(CompoundTag().putString("action", "shuffle")))
            inv.setItem(25, Item.get(Item.ARROW).setCustomName("Previous").setCustomBlockData(CompoundTag().putString("action", "previous")))
            inv.setItem(26, Item.get(Item.ARROW).setCustomName("Next").setCustomBlockData(CompoundTag().putString("action", "next")))
        }
    }
}