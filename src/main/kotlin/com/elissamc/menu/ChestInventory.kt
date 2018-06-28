package com.elissamc.menu

import cn.nukkit.inventory.InventoryHolder
import cn.nukkit.inventory.InventoryType
import cn.nukkit.item.Item
import cn.nukkit.utils.TextFormat
import java.util.*

class ChestInventory(holder: InventoryHolder, type: InventoryType, owner: String, name: String) : FakeBaseInventory(holder, type) {
    private var named: String = TextFormat.colorize(name)
    private var owner: String = owner

    override fun getTitle(): String? {
        return named
    }

    fun getOwner(): String{
        return owner
    }

    fun setName(s: String) {
        this.named = s
    }


}
