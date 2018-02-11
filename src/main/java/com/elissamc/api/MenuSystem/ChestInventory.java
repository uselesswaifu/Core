package com.elissamc.api.MenuSystem;

import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.utils.TextFormat;

public class ChestInventory extends FakeBaseInventory {
    private String owner;
    private String name;
    public ChestInventory(InventoryHolder holder, InventoryType type, String owner, String name) {
        super(holder, type);
        this.owner = owner;
        this.name = TextFormat.colorize(name);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String s){
        this.name = s;
    }

    public String getOwner() {
        return owner;
    }


}
