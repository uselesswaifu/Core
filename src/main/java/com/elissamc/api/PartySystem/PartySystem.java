package com.elissamc.api.PartySystem;

import cn.nukkit.Player;
import cn.nukkit.api.API;
import com.elissamc.ElissaMC;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class PartySystem {

    @API(usage = API.Usage.STABLE, definition = API.Definition.UNIVERSAL)
    public static boolean IsInParty(Player player){
        Map<String, ArrayList<Player>> parties = ElissaMC.plugin.getInstance().party.parties;
        for (Map.Entry<String, ArrayList<Player>> entry : parties.entrySet()) {
            if (entry.getValue().contains(player)) {
                return true;
            }
        }
        return false;
    }

    @API(usage = API.Usage.STABLE, definition = API.Definition.UNIVERSAL)
    public static boolean canJoinGame(Player player, Object game){
        Map<String, ArrayList<Player>> parties = ElissaMC.plugin.getInstance().party.parties;
        for (Map.Entry<String, ArrayList<Player>> entry : parties.entrySet()) {
            if (entry.getValue().contains(player)) {
                int size = parties.get(entry.getKey()).size();
                int slots = getAvailableSlots(game);
                return size < slots;
            }
            }
        return false;
    }

    @API(usage = API.Usage.STABLE, definition = API.Definition.UNIVERSAL)
    public static boolean isPartyLeader(Player player) {
        Map<String, ArrayList<Player>> parties = ElissaMC.plugin.getInstance().party.parties;
        return parties.containsKey(player.getName());
    }

    @API(usage = API.Usage.STABLE, definition = API.Definition.UNIVERSAL)
    public static ArrayList<Player> getPartyMembers(Player player){
        Map<String, ArrayList<Player>> parties = ElissaMC.plugin.getInstance().party.parties;
        for (Map.Entry<String, ArrayList<Player>> entry : parties.entrySet()) {
            if (entry.getValue().contains(player)) {
                if(parties.containsKey(player.getName())){
                    return parties.get(player.getName());
                }
            }
        }
        return null;
    }

    private static int getAvailableSlots(Object game){
        Object value = 0;
        try {
            Method method = game.getClass().getMethod("getAvailableSlots");
            try {
                 value = method.invoke(game);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (int) value;
    }

}
