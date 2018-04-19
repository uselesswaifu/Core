package com.elissamc;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.scheduler.NukkitRunnable;
import com.elissamc.actions.CommandAction;
import com.elissamc.actions.TeleportAction;
import com.elissamc.area.AreaFlag;
import com.elissamc.area.Lobby;
import com.elissamc.core.Region;
import com.elissamc.core.StorageEngine;
import com.elissamc.core.TeleportGate;

import java.util.UUID;

public class HardCoded {

    public static Vector3f antigravity = new Vector3f(0, 20.2F, 0);

    @SuppressWarnings("deprecation")
    public static final void main() {

        //Initialize main gates
        initGates();

        //Initialize lobbies
        initLobbies();
    }

    private static void initLobbies() {
        try {
            StorageEngine.addLobby(new Lobby("hub", new Region(new Vector3(-93, 34, -137),
                    new Vector3(226, 155, 208), Server.getInstance().getLevelByName("Lobby"))));
        } catch (Exception e) {
            return;
        }

        StorageEngine.getLobby("hub").setSpawn(Server.getInstance().getLevelByName("Lobby").getSafeSpawn().getLocation());
        StorageEngine.getLobby("hub").setThresholdY(20);

        //dobrakmato - block interactions
        StorageEngine.getLobby("hub").setPlayerFlag(AreaFlag.BLOCK_BREAK, true,
                UUID.fromString("966ad920-d45e-3fe5-8956-bf7a7a877ab4"));
        StorageEngine.getLobby("hub").setPlayerFlag(AreaFlag.BLOCK_PLACE, true,
                UUID.fromString("966ad920-d45e-3fe5-8956-bf7a7a877ab4"));
    }

    private static void initGates() {
        try {
            StorageEngine.addGate("Lsurvival", new TeleportGate(new Region(new Vector3(-7,
                    50, 258), new Vector3(-9, 54, 264), ElissaMC.getWorld("world")),
                    new TeleportAction(null)));

            StorageEngine.addGate("Lstarving", new TeleportGate(new Region(new Vector3(26,
                    50, 266), new Vector3(28, 55, 260), ElissaMC.getWorld("world")),
                    new TeleportAction(null)));

            StorageEngine.addGate("Lminigame", new TeleportGate(new Region(new Vector3(7, 50,
                    280), new Vector3(13, 55, 282), ElissaMC.getWorld("world")),
                    new TeleportAction(new Location(1972.5, 147.5,
                            2492.5, ElissaMC.getWorld("world")))));

            //Initialize gates
            StorageEngine.addGate("mg_colorwar", new TeleportGate(
                    new Region(new Vector3(1976, 147, 2532), new Vector3(1972, 153, 2534),
                            ElissaMC.getWorld("world")), new CommandAction("pcmd cwtest")));

            StorageEngine.addGate("mg_tnttag", new TeleportGate(new Region(new Vector3(1962,
                    147, 2532), new Vector3(1967, 153, 2534), ElissaMC.getWorld("world")),
                    new CommandAction("pcmd tnttest")));
        }
        catch (Exception e){
            Server.getInstance().getLogger().error("Could not initialize Gates!");
        }
    }
}