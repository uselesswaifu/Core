package com.elissamc.menu


import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.inventory.Inventory
import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.inventory.InventoryType
import com.elissamc.actions.Action

import java.util.HashMap

class InventoryMenu
/**
 * Creates new Inventory menu object with specified type of inventory, title and list of items.
 *
 * @param type  type of inventory
 * @param title title of inventory
 * @param items items
 * @see InventoryMenuItem
 *
 * @see Action
 */
(type: InventoryType, title: String,
 items: List<InventoryMenuItem>) : InventoryHolder {
    /**
     * Inventory of this menu.
     */
    private val inventory: Inventory
    /**
     * Items in inventory.
     */
    private val items = HashMap<Int, InventoryMenuItem>()

    init {
        for (item in items)
            if (!this.items.containsKey(item.slot))
                this.items[item.slot] = item
            else
                throw RuntimeException("Can't put " + item.itemStack.toString()
                        + " to slot " + item.slot + "! Slot " + item.slot
                        + " is alredy occupied by "
                        + this.items[item.slot]?.itemStack.toString())

        this.inventory = ChestInventory(this, type, title)
        for (item in this.items.values)
            this.inventory.setItem(item.slot, item.itemStack)
    }

    /**
     * Opens this inventory menu to specified player.
     *
     * @param player player to show menu to
     */
    fun showTo(player: Player) {
        player.addWindow(this.getInventory())
    }

    /**
     * Same as calling [InventoryMenu.showTo].
     *
     * @param player player to show menu to
     * @see InventoryMenu.showTo
     */
    fun openInventory(player: Player) {
        player.addWindow(this.getInventory())
    }

    override fun getInventory(): Inventory {
        return this.inventory
    }

    /**
     * Returns true, if the menu should be closed after the item in specified slot is clicked.
     *
     * @param slot
     * @return whether the inventory should close after click
     */
    fun shouldClose(slot: Int): Boolean {
        if(this.items[slot] == null)
            return false
        return this.items.getValue(slot).isCloseAfterClick
    }

    /**
     * Called when somebody clicks item in this inventory.
     *
     * @param player
     * @param slot
     */
    fun inventoryClick(player: Player, slot: Int) {
        if (this.items.containsKey(slot)) {
            this.items[slot]?.executeAction(player)
        } else {
            Server.getInstance().logger.alert("Player '" + player + "' clicked on invalid item at slot '" + slot
                    + "' in inventoryMenu!")
        }
    }
}