package com.elissamc.menu

import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.inventory.InventoryType
import cn.nukkit.item.Item
import cn.nukkit.utils.TextFormat
import java.util.*

class ChestInventory(holder: InventoryHolder, type: InventoryType, name: String) : FakeBaseInventory(holder, type) {
    private var named: String = TextFormat.colorize(name)

    override fun getName(): String? {
        return named
    }

    fun setName(s: String) {
        this.named = s
    }


}
