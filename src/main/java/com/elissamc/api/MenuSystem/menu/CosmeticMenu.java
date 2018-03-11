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
import cn.nukkit.utils.TextFormat;
import com.elissamc.api.MenuSystem.ChestInventory;

import java.io.IOException;
import java.util.List;

import static cn.nukkit.utils.TextFormat.*;

public class CosmeticMenu implements Listener {

    public static void showMenu(Player player) {
        ChestInventory inv = new ChestInventory(player, InventoryType.DOUBLE_CHEST, "cosmetic", "Cosmetics", 54);
        mainMenu(inv);
        player.addWindow(inv);
    }

    private static void mainMenu(ChestInventory inv) {
        inv.setSize(54);
        inv.clearAll();
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
                    if (data.getString("menu").equals("main"))
                        mainMenu(inventory);
                    if (data.getString("menu").equals("pets"))
                        petsMenu(inventory);
                    if (data.getString("menu").equals("mounts"))
                        mountsMenu(inventory);
                    if (data.getString("menu").equals("trails"))
                        trailsMenu(inventory);
                    if (data.getString("menu").equals("suits"))
                        suitsMenu(inventory);
                    if (data.getString("menu").equals("crate"))
                        crateMenu(inventory);
                }

                if (data.contains("pet")) {
                    assert inventory != null;
                    if (data.getString("pet").equals("clear")) {
                        event.getTransaction().getSource().sendMessage(RED + "Cleared pet selection!");
                        inventory.close(event.getTransaction().getSource());
                    }
                }

                if (data.contains("ignore") && data.getBoolean("ignore"))
                    event.setCancelled();
            }
        }
    }

    private void crateMenu(ChestInventory inventory) {
        inventory.clearAll();
        inventory.setItem(13, Item.get(Item.TRIPWIRE_HOOK).setCustomName("" + BOLD + YELLOW + "1" + GRAY + " Treasure Key for" + YELLOW + " 10$").setCustomBlockData(new CompoundTag().putString("command", "cosmetic addkey 1")));
        int purchase[] = {28, 29, 30, 37, 38, 39, 46, 47, 48};
        int cancel[] = {34, 35, 36, 43, 44, 45, 52, 53, 54};
        for (int i : purchase) {
            inventory.setItem(i - 1, Item.get(Item.EMERALD_BLOCK).setCustomName("" + BOLD + GREEN + "PURCHASE").setCustomBlockData(new CompoundTag().putBoolean("purcahse", true)));
        }
        for (int i : cancel) {
            inventory.setItem(i - 1, Item.get(Item.REDSTONE_BLOCK).setCustomName("" + BOLD + RED + "CANCEL").setCustomBlockData(new CompoundTag().putBoolean("purcahse", false)));
        }
    }

    private void petsMenu(ChestInventory inventory) {
        inventory.clearAll();
        inventory.setItem(10, Item.get(Item.RAW_PORKCHOP).setCustomName(AQUA + "Spawn" + DARK_PURPLE + " Piggy" + RESET + "\n\nOink! Oink!\n\nPermission: NO!").setCustomBlockData(new CompoundTag().putString("pet", "pig")));
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED + "Main Menu").setCustomBlockData(new CompoundTag().putString("menu", "main")));
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED + "Clear current pet").setCustomBlockData(new CompoundTag().putString("pet", "clear")));
    }

    private void mountsMenu(ChestInventory inventory) {
        inventory.clearAll();
        inventory.setItem(10, Item.get(Item.DRAGON_EGG).setCustomName(AQUA + "Mount" + DARK_PURPLE + " EnderDragon" + RESET + "\n\nRawr!\n\nPermission: NO!").setCustomBlockData(new CompoundTag().putString("mount", "dragon")));
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED + "Main Menu").setCustomBlockData(new CompoundTag().putString("menu", "main")));
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED + "Clear current mount").setCustomBlockData(new CompoundTag().putString("mount", "clear")));
    }

    private void trailsMenu(ChestInventory inventory) {
        inventory.clearAll();
        inventory.setItem(10, Item.get(Item.BLAZE_ROD).setCustomName(AQUA + "Trail" + DARK_PURPLE + " Redstone" + RESET + "\n\nBright! Bright!\n\nPermission: NO!").setCustomBlockData(new CompoundTag().putString("mount", "dragon")));
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED + "Main Menu").setCustomBlockData(new CompoundTag().putString("menu", "main")));
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED + "Clear current mount").setCustomBlockData(new CompoundTag().putString("trail", "clear")));
    }

    private void suitsMenu(ChestInventory inventory) {
        inventory.clearAll();
        inventory.setItem(10, Item.get(Item.DRAGON_EGG).setCustomName(AQUA + "Suit" + DARK_PURPLE + " SpaceMan" + RESET + "\n\nDon't forget to blink!\n\nPermission: NO!").setCustomBlockData(new CompoundTag().putString("mount", "dragon")));
        inventory.setItem(39, Item.get(Item.ARROW).setCustomName(RED + "Main Menu").setCustomBlockData(new CompoundTag().putString("menu", "main")));
        inventory.setItem(41, Item.get(Item.REDSTONE_BLOCK).setCustomName(RED + "Clear current mount").setCustomBlockData(new CompoundTag().putString("suit", "clear")));
    }
}
