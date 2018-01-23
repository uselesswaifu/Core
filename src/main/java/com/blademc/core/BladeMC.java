package com.blademc.core;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.blademc.core.Instance.Instance;

import java.io.File;

public class BladeMC extends PluginBase implements Listener {
    public Instance instance;
    public static File dataFolder;
    public static BladeMC plugin;


    @Override
    public void onEnable() {
        getLogger().info(TextFormat.RED + "BladeMC has been enabled!");
        getServer().getPluginManager().registerEvents(new Instance(this), this);
        instance = new Instance(this);
        instance.load();
        dataFolder = this.getDataFolder();
        plugin = this;


    }

    public Instance getInstance() {
        return instance;
    }

}
