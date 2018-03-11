package com.elissamc.api.MenuSystem.menu;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import com.elissamc.api.MenuSystem.ChestInventory;
import net.buycraft.plugin.bukkit.BuycraftPlugin;
import net.buycraft.plugin.client.ApiException;
import net.buycraft.plugin.data.Category;
import net.buycraft.plugin.data.Package;

import java.io.IOException;
import java.util.List;

import static cn.nukkit.utils.TextFormat.*;

public class StoreMenu implements Listener {

    public static void showMenu(Player player) {
        ChestInventory inv = new ChestInventory(player, InventoryType.CHEST, "store", "Buycraft: Categories", 9);
        mainMenu(inv);
        player.addWindow(inv);
    }

    private static void mainMenu(ChestInventory inv) {
        inv.setSize(9);
        try {

            List<Category> cat = ((BuycraftPlugin) Server.getInstance().getPluginManager().getPlugin("BuycraftX")).getApiClient().retrieveListing().getCategories();
            for (Category c : cat) {
                inv.setItem(c.getOrder(), Item.get(Item.NAME_TAG).setCustomName("Category: " + c.getName()).setCustomBlockData(new CompoundTag().putString("category", c.getName())));
            }
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void grabzItemInChest(InventoryTransactionEvent event) {
        for (InventoryAction action : event.getTransaction().getActions()) {
            ChestInventory inventory = null;
            CompoundTag data = action.getSourceItem().getCustomBlockData();
            for (Inventory inv : event.getTransaction().getInventories()) {
                if (inv instanceof ChestInventory) {
                    if (((ChestInventory) inv).getOwner().equals("store")) {
                        event.setCancelled();
                        inventory = (ChestInventory) inv;
                    }
                }
            }

            if (action.getSourceItem().hasCustomBlockData()) {
                assert inventory != null;
                if (data.contains("category")) {
                    String name = data.getString("category");
                    sendPackages(inventory, name);
                }

                if (data.contains("package")) {
                    String name = data.getString("package");
                    sendInfo(event.getTransaction().getSource(), inventory, name);
                }

                if (data.contains("ignore") && data.getBoolean("ignore"))
                    event.setCancelled();
                if (data.contains("main") && data.getBoolean("main"))
                    mainMenu(inventory);
            }
        }
    }

    private void sendPackages(ChestInventory inventory, String name) {
        inventory.clearAll();
        try {
            List<Category> cat = ((BuycraftPlugin) Server.getInstance().getPluginManager().getPlugin("BuycraftX")).getApiClient().retrieveListing().getCategories();
            for (Category c : cat) {
                if (c.getName().equals(name)) {
                    for (Package p : c.getPackages()) {
                        inventory.setItem(22, Item.get(Item.BOOK_AND_QUILL).setCustomName("" + BOLD + RED + "CANCEL").setCustomBlockData(new CompoundTag().putBoolean("main", true)));
                        inventory.setItem(p.getOrder(), Item.get(Item.PAPER).setCustomName("Package: " + p.getName()).setCustomBlockData(new CompoundTag().putString("package", p.getName())));
                    }
                }
            }
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
    }

    private void sendInfo(Player player, ChestInventory inventory, String name) {
        try {
            List<Category> cat = ((BuycraftPlugin) Server.getInstance().getPluginManager().getPlugin("BuycraftX")).getApiClient().retrieveListing().getCategories();
            for (Category c : cat) {
                for (Package p : c.getPackages()) {
                    if (p.getName().equals(name)) {
                        inventory.close(player);
                        String salestatus = p.getSale().isActive() ? p.getSale().getDiscount().toString() : "NO";
                        player.sendMessage("" + GRAY + p.getName() + ", " + YELLOW + "PRICE: " + GRAY + p.getPrice() + " EFFECTIVE PRICE: " + p.getEffectivePrice() + " SALE: " + salestatus);
                    }
                }
            }
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
    }
}
