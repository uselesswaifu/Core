package com.elissamc.menu;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import com.elissamc.actions.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryMenu implements InventoryHolder{
    /**
     * Inventory of this menu.
     */
    private final Inventory inventory;
    /**
     * Items in inventory.
     */
    private final Map<Integer, InventoryMenuItem> items = new HashMap<Integer, InventoryMenuItem>();

    /**
     * Creates new Inventory menu object with specified type of inventory, title and list of items.
     *
     * @param type  type of inventory
     * @param title title of inventory
     * @param items items
     * @see InventoryMenuItem
     * @see Action
     */
    public InventoryMenu(final InventoryType type, final String title,
                         final List<InventoryMenuItem> items) {
        for (InventoryMenuItem item : items)
            if (!this.items.containsKey(item.getSlot()))
                this.items.put(item.getSlot(), item);
            else
                throw new RuntimeException("Can't put " + item.getItemStack().toString()
                        + " to slot " + item.getSlot() + "! Slot " + item.getSlot()
                        + " is alredy occupied by "
                        + this.items.get(item.getSlot()).getItemStack().toString());

        this.inventory = new ChestInventory(this, type, title);
        for (InventoryMenuItem item : this.items.values())
            this.inventory.setItem(item.getSlot(), item.getItemStack());
    }

    /**
     * Opens this inventory menu to specified player.
     *
     * @param player player to show menu to
     */
    public void showTo(final Player player) {
        player.addWindow(this.getInventory());
    }

    /**
     * Same as calling {@link InventoryMenu#showTo(Player)}.
     *
     * @param player player to show menu to
     * @see InventoryMenu#showTo(Player)
     */
    public void openInventory(final Player player) {
        player.addWindow(this.getInventory());
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Returns true, if the menu should be closed after the item in specified slot is clicked.
     *
     * @param slot
     * @return whether the inventory should close after click
     */
    public boolean shouldClose(final int slot) {
        return this.items.get(slot).isCloseAfterClick();
    }

    /**
     * Called when somebody clicks item in this inventory.
     *
     * @param player
     * @param slot
     */
    public void inventoryClick(final Player player, final int slot) {
        if (this.items.containsKey(slot)) {
            this.items.get(slot).executeAction(player);
        } else {
            Server.getInstance().getLogger().alert("Player '" + player + "' clicked on invalid item at slot '" + slot
                    + "' in inventoryMenu!");
        }
    }
}