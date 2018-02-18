package com.elissamc.api.MenuSystem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockHopper;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemHopper;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.scheduler.NukkitRunnable;
import com.elissamc.ElissaMC;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


abstract class FakeBaseInventory extends BaseInventory {

    private Map<UUID, Vector3> holders = new HashMap<>();

    FakeBaseInventory(InventoryHolder holder, InventoryType type, Map<Integer, Item> items, Integer overrideSize) {
        super(holder, type, items, overrideSize);
    }

    @Override
    public void onOpen(Player player) {
        super.onOpen(player);
        double INVENTORY_HEIGHT = 3;
        holders.put(player.getUniqueId(), player.add(0, INVENTORY_HEIGHT));
        this.sendBlocks(player);
        this.sendFakeTile(player);
        this.sendInventoryInterface(player);
    }

    @Override
    public void onClose(Player player) {
        ContainerClosePacket pk = new ContainerClosePacket();
        pk.windowId = player.getWindowId(this);
        player.dataPacket(pk);

        Vector3 hold = holders.get(player.getUniqueId());
        if (this.getType().getDefaultTitle().equals(InventoryType.DOUBLE_CHEST.getDefaultTitle())) {
            player.getLevel().sendBlocks(new Player[]{player}, new Block[]{
                    player.level.getBlock((int) hold.getX(), (int) hold.getY(), (int) hold.getZ()),
                    player.level.getBlock((int) hold.getX() + 1, (int) hold.getY(), (int) hold.getZ())
            });
        } else {
            player.getLevel().sendBlocks(new Player[]{player}, new Block[]{
                    player.level.getBlock((int) hold.getX(), (int) hold.getY(), (int) hold.getZ())});
        }

    }

    private void sendInventoryInterface(Player player) {
        if (this.getType().getDefaultTitle().equals(InventoryType.DOUBLE_CHEST.getDefaultTitle())) {
            Server.getInstance().getScheduler().scheduleDelayedTask(ElissaMC.plugin, new NukkitRunnable() {
                @Override
                public void run() {
                    inventoryInterface(player);
                }
            }, 4);
        }
        else
            inventoryInterface(player);
    }

    private void inventoryInterface(Player player){
        Vector3 hold = holders.get(player.getUniqueId());
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowId = player.getWindowId(FakeBaseInventory.this);
        pk.type = FakeBaseInventory.this.getType().getNetworkType();
        pk.x = (int) hold.getX();
        pk.y = (int) hold.getY();
        pk.z = (int) hold.getZ();
        player.dataPacket(pk);
        FakeBaseInventory.this.sendContents(player);
    }

    private void sendFakeTile(Player player) {
        Vector3 hold = holders.get(player.getUniqueId());
        {
            BlockEntityDataPacket pk = new BlockEntityDataPacket();
            pk.x = (int) hold.getX();
            pk.y = (int) hold.getY();
            pk.z = (int) hold.getZ();
            CompoundTag nbt = new CompoundTag().putString("id", this.getType().getDefaultTitle());
            nbt.putString("CustomName", getName());
            if (this.getType().getDefaultTitle().equals(InventoryType.DOUBLE_CHEST.getDefaultTitle())) {
                nbt.putInt("pairx", (int) hold.getX() + 1);
                nbt.putInt("pairz", (int) hold.getZ());
            }
            try {
                pk.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            player.dataPacket(pk);
        }

        if (this.getType().getDefaultTitle().equals(InventoryType.DOUBLE_CHEST.getDefaultTitle())) {
            BlockEntityDataPacket pk = new BlockEntityDataPacket();
            pk.x = (int) hold.getX() + 1;
            pk.y = (int) hold.getY();
            pk.z = (int) hold.getZ();
            CompoundTag nbt = new CompoundTag().putString("id", this.getType().getDefaultTitle());
            nbt.putString("CustomName", getName());
            nbt.putInt("pairx", (int) hold.getX());
            nbt.putInt("pairz", (int) hold.getZ());
            try {
                pk.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            player.dataPacket(pk);
        }
    }

    private void sendBlocks(Player player) {
        Vector3 hold = holders.get(player.getUniqueId());
        if (this.getType().getDefaultTitle().equals(InventoryType.DOUBLE_CHEST.getDefaultTitle())) {
            player.getLevel().sendBlocks(new Player[]{player}, new Block[]{
                    (Block) Block.get(Block.CHEST, 0).setComponents(hold.getX(), hold.getY(), hold.getZ()),
                    (Block) Block.get(Block.CHEST, 0).setComponents(hold.getX() + 1, hold.getY(), hold.getZ())
            });
        } else {
            Item item = Item.fromString(this.getType().getDefaultTitle());
            player.getLevel().sendBlocks(new Player[]{player}, new Block[]{
                    (Block) Block.get(item.getId()).setComponents(hold.getX(), hold.getY(), hold.getZ())});
        }
    }


}
