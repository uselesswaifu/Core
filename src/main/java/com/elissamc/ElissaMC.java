package com.elissamc;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.elissamc.instance.Instance;

import java.io.File;

public class ElissaMC extends PluginBase implements Listener {
    private Instance instance;
    public static File dataFolder;
    public static ElissaMC plugin;


    @Override
    public void onEnable() {
        getLogger().info(TextFormat.RED + "ElissaMC has been enabled!");
        plugin = this;
        dataFolder = this.getDataFolder();
        getServer().getPluginManager().registerEvents( instance = new Instance(this), this);
        instance.load();
    }

    public Instance getInstance() {
        return instance;
    }

}
