package com.elissamc.api.MenuSystem

import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.inventory.InventoryType
import cn.nukkit.item.Item
import cn.nukkit.utils.TextFormat

import java.util.HashMap

class ChestInventory(holder: InventoryHolder, type: InventoryType, val owner: String, n: String, size: Int) : FakeBaseInventory(holder, type, HashMap<Int, Item>(), size) {
    private var named: String = TextFormat.colorize(n)

    override fun getName(): String? {
        return named
    }

    fun setName(s: String) {
        this.named = s
    }


}
