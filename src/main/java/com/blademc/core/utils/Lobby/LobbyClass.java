package com.blademc.core.utils.Lobby;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.Plugin;
import com.blademc.core.BladeMC;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LobbyClass{

    private Object lobby;

    public LobbyClass()
    {

    }

    public LobbyClass(Object lobby, Object plugin)
    {
        this.lobby = lobby;
        Server.getInstance().getPluginManager().registerEvents((Listener) lobby, (Plugin) plugin);
    }


    public void addPlayer(Player player){
        try {
            Method method = lobby.getClass().getMethod("addPlayer", Player.class);
            try {
                method.invoke(lobby, player);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(Player player) {
        try {
            Method method = lobby.getClass().getMethod("removePlayer", Player.class);
            try {
                method.invoke(lobby, player);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
