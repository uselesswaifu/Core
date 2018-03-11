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
import com.elissamc.ElissaMC;
import com.elissamc.api.MenuSystem.ChestInventory;
import com.elissamc.games.GameParent;

import java.util.Map;

public class GamesMenu implements Listener {

    public static void showMenu(Player player) {
        ChestInventory inv = new ChestInventory(player, InventoryType.CHEST, "games", "Server Menu", 9);
        mainMenu(inv);
        player.addWindow(inv);
    }

    private static void mainMenu(ChestInventory inv) {
        inv.setSize(9);
        int i = 0;
        for (Map.Entry<String, GameParent> game : ElissaMC.plugin.getInstance().getGameHandler().getGames().entrySet()) {
            inv.setItem(i, Item.get(Item.WOOL).setCustomName(game.getKey()).setCustomBlockData(new CompoundTag().putString("game", game.getKey())));
            i++;
        }
    }

    @EventHandler
    public void grabzItemInChest(InventoryTransactionEvent event) {
        for (InventoryAction action : event.getTransaction().getActions()) {
            ChestInventory inventory = null;
            CompoundTag data = action.getSourceItem().getCustomBlockData();
            for (Inventory inv : event.getTransaction().getInventories()) {
                if (inv instanceof ChestInventory) {
                    if (((ChestInventory) inv).getOwner().equals("games")) {
                        event.setCancelled();
                        inventory = (ChestInventory) inv;
                    }
                }
            }

            if (action.getSourceItem().hasCustomBlockData()) {
                assert inventory != null;
                Player player = event.getTransaction().getSource();
                if (data.contains("game")) {
                    String name = data.getString("game");
                    ElissaMC.plugin.getInstance().getGameHandler().joinGame(player, name);
                }
            }
        }
    }
}
