package com.elissamc.instance;

import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Level;
import cn.nukkit.permission.Permission;
import com.elissamc.ElissaMC;
import com.elissamc.api.ChatSystem.ChatListener;
import com.elissamc.api.ParkourSystem.ParkourSystem;
import com.elissamc.games.GameHandler;
import com.elissamc.instance.CMD.NickCmd;
import com.elissamc.instance.CMD.PluginsCmd;
import com.elissamc.instance.CMD.ReportCmd;
import com.elissamc.listener.LobbyListener;
import com.elissamc.player.event.PartyChatEventBC;

public class Instance implements Listener {

    private static String maps[] = {"world"};
    private ElissaMC mommy;
    private GameHandler gameHandler;

    public Instance(ElissaMC mommy) {
        this.mommy = mommy;
        for (final String map : maps) {
            Server.getInstance().loadLevel(map);
            Level level = Server.getInstance().getLevelByName(map);
            level.setRainTime(9999);
            level.setRaining(false);
            level.setThundering(false);
            level.stopTime();
        }
    }

    public void load() {
        this.registerEvents();
        this.registerCommands();
        this.registerPerms();
        gameHandler = new GameHandler();
    }

    private void registerPerms() {
        this.mommy.getServer().getPluginManager().addPermission(new Permission("elissamc.build", null, Permission.DEFAULT_FALSE));
        this.mommy.getServer().getPluginManager().addPermission(new Permission("elissamc.break", null, Permission.DEFAULT_FALSE));
        this.mommy.getServer().getPluginManager().addPermission(new Permission("elissamc.parkour", null, Permission.DEFAULT_TRUE));
    }

    private void registerEvents() {
        mommy.getServer().getPluginManager().registerEvents(new PluginsCmd(), ElissaMC.plugin);
        mommy.getServer().getPluginManager().registerEvents(new PartyChatEventBC(), ElissaMC.plugin);
        mommy.getServer().getPluginManager().registerEvents(new LobbyListener(), ElissaMC.plugin);
        mommy.getServer().getPluginManager().registerEvents(new ParkourSystem(), ElissaMC.plugin);
        mommy.getServer().getPluginManager().registerEvents(new ChatListener(), ElissaMC.plugin);
    }

    private void registerCommands() {
        mommy.getServer().getCommandMap().register("report", new ReportCmd(this));
        mommy.getServer().getCommandMap().register("nick", new NickCmd(this));
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }
}
