package com.elissamc.menu

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.Block
import cn.nukkit.inventory.BaseInventory
import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.inventory.InventoryType
import cn.nukkit.item.Item
import cn.nukkit.math.BlockVector3
import cn.nukkit.nbt.NBTIO
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.network.protocol.BlockEntityDataPacket
import cn.nukkit.network.protocol.ContainerClosePacket
import cn.nukkit.network.protocol.ContainerOpenPacket
import cn.nukkit.scheduler.NukkitRunnable
import com.elissamc.ElissaMC
import java.io.IOException
import java.nio.ByteOrder
import java.util.*


abstract class FakeBaseInventory(holder: InventoryHolder, type: InventoryType) : BaseInventory(holder, type) {

    private val holders = HashMap<UUID, BlockVector3>()

    override fun onOpen(player: Player) {
        super.onOpen(player)
        val INVENTORY_HEIGHT = 3.0
        holders[player.uniqueId] = player.add(0.0, INVENTORY_HEIGHT).asBlockVector3()
        this.sendBlocks(player)
        this.sendFakeTile(player)
        this.sendInventoryInterface(player)
    }

    override fun onClose(player: Player) {
        val pk = ContainerClosePacket()
        pk.windowId = player.getWindowId(this)
        player.dataPacket(pk)

        val pos = getHolder(player)
        if (this.getType().defaultTitle == InventoryType.DOUBLE_CHEST.defaultTitle) {
            player.getLevel().sendBlocks(arrayOf(player), arrayOf(player.level.getBlock(pos.getX(), pos.getY(), pos.getZ()), player.level.getBlock(pos.getX() + 1, pos.getY(), pos.getZ())))
        } else {
            player.getLevel().sendBlocks(arrayOf(player), arrayOf(player.level.getBlock(pos.getX(), pos.getY(), pos.getZ())))
        }

    }

    private fun getHolder(player: Player): BlockVector3 {
        if (holders.containsKey(player.uniqueId))
            return holders[player.uniqueId]!!
        return BlockVector3()
    }


    private fun sendInventoryInterface(player: Player) {
        if (this.getType().defaultTitle == InventoryType.DOUBLE_CHEST.defaultTitle) {
            Server.getInstance().scheduler.scheduleDelayedTask(ElissaMC.plugin, object : NukkitRunnable() {
                override fun run() {
                    inventoryInterface(player)
                }
            }, 4)
        } else
            inventoryInterface(player)
    }

    private fun inventoryInterface(player: Player) {
        val hold = holders[player.uniqueId]!!
        val pk = ContainerOpenPacket()
        pk.windowId = player.getWindowId(this@FakeBaseInventory)
        pk.type = this@FakeBaseInventory.getType().networkType
        pk.x = hold.getX()
        pk.y = hold.getY()
        pk.z = hold.getZ()
        player.dataPacket(pk)
        this@FakeBaseInventory.sendContents(player)
    }

    private fun sendFakeTile(player: Player) {
        holders[player.uniqueId]
        run {
            val pk = BlockEntityDataPacket()
            val pos = getHolder(player)
            val nbt = CompoundTag().putString("id", this.getType().defaultTitle)
            nbt.putString("CustomName", getName())
            if (this.getType().defaultTitle == InventoryType.DOUBLE_CHEST.defaultTitle) {
                nbt.putInt("pairx", pos.getX() + 1)
                nbt.putInt("pairz", pos.getZ())
            }
            try {
                pk.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

            player.dataPacket(pk)
        }

        if (this.getType().defaultTitle == InventoryType.DOUBLE_CHEST.defaultTitle) {
            val pk = BlockEntityDataPacket()
            val pos = getHolder(player)
            pk.x = pos.getX() + 1
            pk.y = pos.getY()
            pk.z = pos.getZ()
            val nbt = CompoundTag().putString("id", this.getType().defaultTitle)
            nbt.putString("CustomName", getName())
            nbt.putInt("pairx", pos.getX())
            nbt.putInt("pairz", pos.getZ())
            try {
                pk.namedTag = NBTIO.write(nbt, ByteOrder.LITTLE_ENDIAN, true)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

            player.dataPacket(pk)
        }
    }

    private fun sendBlocks(player: Player) {
        val pos = getHolder(player)
        if (this.getType().defaultTitle == InventoryType.DOUBLE_CHEST.defaultTitle) {
            player.getLevel().sendBlocks(arrayOf(player), arrayOf(Block.get(Block.CHEST, 0).setComponents(pos.getX().toDouble(), pos.getY().toDouble(), pos.getZ().toDouble()) as Block, Block.get(Block.CHEST, 0).setComponents(pos.getX().toDouble() + 1, pos.getY().toDouble(), pos.getZ().toDouble()) as Block))
        } else {
            val item = Item.fromString(this.getType().defaultTitle)
            player.getLevel().sendBlocks(arrayOf(player), arrayOf(Block.get(item.id).setComponents(pos.getX().toDouble(), pos.getY().toDouble(), pos.getZ().toDouble()) as Block))
        }
    }


}
