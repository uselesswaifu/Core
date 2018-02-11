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
import cn.nukkit.utils.TextFormat;
import com.elissamc.api.MenuSystem.ChestInventory;

import static cn.nukkit.utils.TextFormat.*;

public class CosmeticMenu implements Listener {

    public static void showMenu(Player player) {
        ChestInventory inv = new ChestInventory(player, InventoryType.DOUBLE_CHEST, "cosmetic", "&d&lCosmetics");
        mainMenu(inv);
        player.addWindow(inv);
    }

    private static void mainMenu(ChestInventory inv) {
        inv.setSize(54);
        inv.setItem(20, Item.get(Item.BONE).setCustomName(TextFormat.colorize("&c&lPets")).setCustomBlockData(new CompoundTag().putString("menu", "pets")));
        inv.setItem(21, Item.get(Item.SADDLE).setCustomName(TextFormat.colorize("&c&lMounts")).setCustomBlockData(new CompoundTag().putString("menu", "mounts")));
        inv.setItem(22, Item.get(Item.BLAZE_ROD).setCustomName(TextFormat.colorize("&c&lTrails")).setCustomBlockData(new CompoundTag().putString("menu", "trails")));
        inv.setItem(23, Item.get(Item.LEATHER_TUNIC).setCustomName(TextFormat.colorize("&c&lSuits")).setCustomBlockData(new CompoundTag().putString("menu", "suits")));
        inv.setItem(24, Item.get(Item.ELYTRA).setCustomName(TextFormat.colorize("&c&lWings")).setCustomBlockData(new CompoundTag().putString("menu", "wings")));
        inv.setItem(40, Item.get(Item.ENDER_CHEST).setCustomName(TextFormat.colorize("&5&lMystery Crate")).setLore(WHITE + "This is a Mystery Crate", WHITE + "You can win gadgets by opening one", " ", WHITE + "A key for opening it costs " + YELLOW + "10 souls").setCustomBlockData(new CompoundTag().putString("menu", "crate")));

        CompoundTag nbt = new CompoundTag().putBoolean("ignore", true);
        inv.setItem(0, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(1, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(2, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(3, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(4, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(5, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(6, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(7, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(8, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(9, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));

        inv.setItem(18, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(27, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(17, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(26, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(35, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(42, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));

        inv.setItem(43, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(44, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(36, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(37, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(38, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(51, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(52, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(53, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(45, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(46, Item.get(Item.STAINED_GLASS_PANE, 14).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
        inv.setItem(47, Item.get(Item.STAINED_GLASS_PANE, 7).setCustomName(WHITE.toString()).setCustomBlockData(nbt));
    }

    @EventHandler
    public void grabzItemInChest(InventoryTransactionEvent event) {
        for (InventoryAction action : event.getTransaction().getActions()) {
            ChestInventory inventory = null;
            CompoundTag data = action.getSourceItem().getCustomBlockData();
            for (Inventory inv : event.getTransaction().getInventories()) {
                if (inv instanceof ChestInventory) {
                    if (((ChestInventory) inv).getOwner().equals("cosmetic")) {
                        event.setCancelled();
                        inventory = (ChestInventory) inv;
                    }
                }
            }

            if (action.getSourceItem().hasCustomBlockData()) {
                if (data.contains("menu")) {
                    assert inventory != null;
                    if (data.getString("menu").equals("pets"))
                        petsMenu(inventory, event.getTransaction().getSource());
                }

                if (data.contains("ignore") && data.getBoolean("ignore"))
                    event.setCancelled();
            }
        }
    }

    private void petsMenu(ChestInventory inventory, Player player) {
        inventory.clearAll();
        inventory.setSize(44);
        inventory.setName(GRAY + "Pets");
        inventory.setItem(10, Item.get(Item.RAW_PORKCHOP).setCustomName(AQUA + "Spawn" + DARK_PURPLE + " Piggy" + RESET + "\n\nOink! Oink!\n\nPermission: YES!").setCustomBlockData(new CompoundTag().putString("pet", "pig")));
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED + "Main Menu").setCustomBlockData(new CompoundTag().putString("menu", "main")));
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED + "Clear current pet").setCustomBlockData(new CompoundTag().putString("pet", "clear")));
        inventory.sendContents(player);
    }
}
