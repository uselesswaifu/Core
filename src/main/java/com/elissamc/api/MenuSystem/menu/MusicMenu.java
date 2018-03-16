package com.elissamc.api.MenuSystem.menu;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import com.elissamc.api.MenuSystem.ChestInventory;

public class MusicMenu implements Listener {

    public static void showMenu(Player player) {
        ChestInventory inv = new ChestInventory(player, InventoryType.CHEST, "music", "Jukebox", 27);
        mainMenu(inv);
        player.addWindow(inv);
    }

    private static void mainMenu(ChestInventory inv) {
        inv.setItem(0, Item.get(Item.RECORD_CAT).setCustomBlockData(new CompoundTag().putString("song", "cat")));

        inv.setItem(18, Item.get(Item.BEDROCK).setCustomName("Stop Song").setCustomBlockData(new CompoundTag().putString("action", "stop")));
        inv.setItem(20, Item.get(Item.FIRE_CHARGE).setCustomName("Random Song").setCustomBlockData(new CompoundTag().putString("action", "random")));
        inv.setItem(21, Item.get(Item.BEACON).setCustomName("Change volume").setCustomBlockData(new CompoundTag().putString("action", "volume")));
        inv.setItem(23, Item.get(Item.SIGN).setCustomName("Start on login").setCustomBlockData(new CompoundTag().putString("action", "login")));
        inv.setItem(24, Item.get(Item.BLAZE_POWDER).setCustomName("Shuffle").setCustomBlockData(new CompoundTag().putString("action", "shuffle")));
        inv.setItem(25, Item.get(Item.ARROW).setCustomName("Previous").setCustomBlockData(new CompoundTag().putString("action", "previous")));
        inv.setItem(26, Item.get(Item.ARROW).setCustomName("Next").setCustomBlockData(new CompoundTag().putString("action", "next")));
    }

    @EventHandler
    public void grabzItemInChest(InventoryTransactionEvent event) {
        for (InventoryAction action : event.getTransaction().getActions()) {
            ChestInventory inventory = null;
            CompoundTag data = action.getSourceItem().getCustomBlockData();
            for (Inventory inv : event.getTransaction().getInventories()) {
                if (inv instanceof ChestInventory) {
                    if (((ChestInventory) inv).getOwner().equals("music")) {
                        event.setCancelled();
                        inventory = (ChestInventory) inv;
                    }
                }
            }

            if (action.getSourceItem().hasCustomBlockData()) {
                assert inventory != null;
                Player player = event.getTransaction().getSource();
                if (data.contains("song")) {
                    String name = data.getString("song");
                    inventory.close(player);
                }
            }
        }
    }
}
