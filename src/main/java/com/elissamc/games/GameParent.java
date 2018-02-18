package com.elissamc.games;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.PluginTask;
import com.elissamc.ElissaMC;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GameParent {

    private Object game;

    public GameParent(Object lobby, Object plugin) {
        this.game = lobby;
        Server.getInstance().getPluginManager().registerEvents((Listener) lobby, (Plugin) plugin);
        Server.getInstance().getScheduler().scheduleRepeatingTask(ElissaMC.plugin, (PluginTask<ElissaMC>) lobby, 20);
    }


    public void addPlayer(Player player) {
        try {
            Method method = game.getClass().getMethod("addPlayer", Player.class);
            try {
                method.invoke(game, player);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(Player player) {
        try {
            Method method = game.getClass().getMethod("removePlayer", Player.class);
            try {
                method.invoke(game, player);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}