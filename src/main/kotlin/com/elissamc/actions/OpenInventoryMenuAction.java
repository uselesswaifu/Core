package com.elissamc.actions;


import cn.nukkit.Player;
import com.elissamc.menu.InventoryMenu;

/**
 * Inventory menu action that opens another inventory menu.
 */
public class OpenInventoryMenuAction implements Action {
    private final InventoryMenu inventoryMenu;

    /**
     * Creates new action that opens specified inventory menu when player clicks icon.
     *
     * @param im
     *            another InventoryMenu
     */
    public OpenInventoryMenuAction(final InventoryMenu im) {
        this.inventoryMenu = im;
    }

    @Override
    public void execute(final Player player) {
        this.inventoryMenu.showTo(player);
    }
}