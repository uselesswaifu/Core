package com.elissamc.menu

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.inventory.InventoryType
import cn.nukkit.item.Item
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.DyeColor
import com.elissamc.ElissaMC

class GamesMenu : Listener {

    @EventHandler
    fun grabzItemInChest(event: InventoryTransactionEvent) {
        for (action in event.transaction.actions) {
            var inventory: ChestInventory? = null
            val data = action.sourceItem.customBlockData
            for (inv in event.transaction.inventories) {
                if (inv is ChestInventory) {
                    if (inv.getOwner() == "games") {
                        event.setCancelled()
                        inventory = inv
                    }
                }
            }

            if (action.sourceItem.hasCustomBlockData()) {
                assert(inventory != null)
                val player = event.transaction.source
                if (data.contains("game")) {
                    val name = data.getString("game")
                    inventory!!.close(player)
                    ElissaMC.core.gameHandler.joinGame(player, name)
                }
            }
        }
    }

    companion object {

        fun showMenu(player: Player) {
            val inv = ChestInventory(player, InventoryType.CHEST, "games", "Server Menu")
            mainMenu(inv)
            player.addWindow(inv)
        }

        private fun mainMenu(inv: ChestInventory) {
            inv.size = 9
            for ((i, game) in ElissaMC.core.gameHandler.getGames().entries.withIndex()) {
                inv.setItem(i, Item.get(Item.WOOL, DyeColor.LIME.woolData).setCustomName(game.key).setCustomBlockData(CompoundTag().putString("game", game.key)))
            }
        }
    }
}