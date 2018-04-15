package com.elissamc.api.MenuSystem.menu

import cn.nukkit.Player
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.inventory.Inventory
import cn.nukkit.inventory.InventoryType
import cn.nukkit.inventory.transaction.action.InventoryAction
import cn.nukkit.item.Item
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.utils.DyeColor
import com.elissamc.ElissaMC
import com.elissamc.api.MenuSystem.ChestInventory
import com.elissamc.games.GameParent

class GamesMenu:Listener {

@EventHandler
 fun grabzItemInChest(event:InventoryTransactionEvent) {
for (action in event.transaction.actions)
{
var inventory:ChestInventory? = null
val data = action.sourceItem.customBlockData
for (inv in event.transaction.inventories)
{
if (inv is ChestInventory)
{
if (inv.owner == "games")
{
event.setCancelled()
inventory = inv
}
}
}

if (action.sourceItem.hasCustomBlockData())
{
assert(inventory != null)
val player = event.transaction.source
if (data.contains("game"))
{
val name = data.getString("game")
inventory!!.close(player)
ElissaMC.plugin.instance!!.gameHandler!!.joinGame(player, name)
}
}
}
}

companion object {

 fun showMenu(player:Player) {
val inv = ChestInventory(player, InventoryType.CHEST, "games", "Server Menu", 9)
mainMenu(inv)
player.addWindow(inv)
}

private fun mainMenu(inv:ChestInventory) {
inv.size = 9
var i = 0
for ((key) in ElissaMC.plugin.instance.gameHandler.games)
{
inv.setItem(i, Item.get(Item.WOOL, DyeColor.LIME.woolData).setCustomName(key).setCustomBlockData(CompoundTag().putString("game", key)))
i++
}
}
}
}
