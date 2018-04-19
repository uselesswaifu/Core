package com.elissamc.menu

import cn.nukkit.Player
import cn.nukkit.item.Item
import com.elissamc.actions.Action

/** Class that represents item in inventory menu. */
class InventoryMenuItem
/**
 * Creates new Inventory menu item from specified params.
 *
 * @param itemStack
 * itemStack to use as icon.
 * @param action
 * action to execute when player clicks icon.
 * @param slot
 * slot in inventory, where the itemStack should be.
 * @param isCloseAfterClick
 * boolean, that specify, if the menu should close after click on this item.
 */
(
        /** ItemStack of item. */
        val itemStack: Item,

        /** Action to execute when item is clicked. */
        private val action: Action,

        /** Slot of inventory. */
        val slot: Int,

        /** Specifies if the InventoryMenu have to close, after click. */
        val isCloseAfterClick: Boolean) {

    /**
     * Executes action with specified player(sender).
     *
     * @param player
     * player that is task executed
     */
    fun executeAction(player: Player) {
        this.action.execute(player)
    }
}