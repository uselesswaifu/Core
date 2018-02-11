package com.elissamc.task.ServerTutorial;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.scheduler.NukkitRunnable;

import static cn.nukkit.utils.TextFormat.*;

public class ServerTutorial extends NukkitRunnable {

    private int stage = 0;
    private Player player;
    private Level l = Server.getInstance().getDefaultLevel();
    private Location previousLoc;
    private Location locs[] = {
            new Location(-160.3890975496729, 100.0, 302.65173069276443, 307.15384, 26.55564, l),
            new Location(-228.46697606052106, 88.0, 288.4036739560498, 310.9807, 33.4011, l),
            new Location(-273.4095471915437, 84.0, 288.4437772466859, 57.75351, 12.304578, l),
            new Location(-154.69999998807907, 106.0, 346.3194764605599, 208.09845, 26.041174, l),
            new Location(-144.3900863763124, 93.0, 294.30000001192093, 341.02112, 16.031563, l),
            new Location(-180.69999998807907, 103.0, 320.69999998807907, 245.1742, 9.333818, l),
    };

    public ServerTutorial(Player player) {
        this.player = player;
        this.previousLoc = player.getLocation();
        player.setImmobile();
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        switch (stage) {
            case 0:
                player.sendTitle(YELLOW + "Welcome", GRAY + "You just started the tutorial!", 5, 80, 5);
                player.teleport(locs[stage]);
                break;
            case 1:
                player.sendTitle(GOLD + "Choosing the Server!", WHITE + "Use the compass in your inventory to choose a gametype", 20, 80, 20);
                player.teleport(locs[stage]);
                break;
            case 2:
                player.sendTitle(GOLD + "Minigames", WHITE + "The lobby contains many fun minigames and secrets!", 20, 80, 20);
                player.teleport(locs[stage]);
                break;
            case 3:
                player.sendTitle(GOLD + "Souls", WHITE + "Souls are a global currency working on all gametypes!", 5, 80, 5);
                player.teleport(locs[stage]);
                break;
            case 4:
                player.sendTitle(GOLD + "Gadgets!", WHITE + "Gadgets are a fun way to spend your time!", 5, 40, 20);
                player.teleport(locs[stage]);
                break;
            case 5:
                player.sendTitle(GOLD + "Mystery Crates!", WHITE + "Pay 10 souls to open a Mystery Crate!", 5, 40, 20);
                player.teleport(locs[stage]);
                break;
            case 6:
                player.sendTitle(GOLD + "Tutorial finished!", WHITE + "Thank you for taking your time!", 5, 40, 20);
                player.teleport(this.previousLoc);
                break;
            default:
                player.setImmobile(false);
                this.cancel();
                break;
        }
        stage++;
    }
}
