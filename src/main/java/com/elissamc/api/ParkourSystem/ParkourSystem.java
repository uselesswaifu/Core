package com.elissamc.api.ParkourSystem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockPressurePlateBase;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.elissamc.ElissaMC;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static cn.nukkit.utils.TextFormat.*;

public class ParkourSystem extends VanillaCommand implements Listener {

    private Config config = new Config(new File(ElissaMC.dataFolder + "/ParkourSystem/", "Timer.yml"), Config.YAML);
    private Map<UUID, Integer> parkour = new HashMap<>();
    private Map<UUID, Long> timer = new HashMap<>();
    private Map<Integer, Location> checkpoints = new HashMap<>();
    private Level l = Server.getInstance().getDefaultLevel();
    private Location start = new Location(-305, 53, 312, 90.12568, -2.6629138, l);
    private Location end = new Location(-320, 125, 314, l);

    public ParkourSystem() {
        super("parkour");
        Server.getInstance().getCommandMap().register("parkour", this);
        checkpoints.put(1, new Location(-311, 62, 304, -266.4483, -2.736549, l));
        checkpoints.put(2, new Location(-313, 70, 328, -235.58936, -14.071013, l));
        checkpoints.put(3, new Location(-318, 82, 309, -110.93039, 15.221949, l));
        checkpoints.put(4, new Location(-320, 89, 311, -359.941, 23.097181, l));
        checkpoints.put(5, new Location(-316, 96, 312, -358.83707, 7.493875, l));
        checkpoints.put(6, new Location(-320, 108, 310, -0.75057983, -1.7061664, l));
        checkpoints.put(7, new Location(-320, 125, 314, -3.8417358, 7.4202847, l));
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if (!(commandSender instanceof Player))
            return false;
        Player player = (Player) commandSender;
        if (args.length > 0) {
            if (args[0].equals("start")) {
                player.sendActionBar("Parkour Challenge Started");
                player.teleport(start);
            }

            if (args[0].equals("reset")) {
                player.sendActionBar("Parkour Challenge Restarted!");
                player.teleport(start);
            }

            if (args[0].equals("checkpoint")) {
                if (parkour.containsKey(player.getUniqueId())) {
                    Location pos = checkpoints.get(parkour.get(player.getUniqueId()));
                    if (pos != null) {
                        player.sendActionBar("Sent to Checkpoint!");
                        player.teleport(pos);
                    }
                }
            }
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerInteractPressurePlate(PlayerInteractEvent event) {
        if (event.getAction() == PlayerInteractEvent.Action.PHYSICAL) {
            Player player = event.getPlayer();
            Block block = event.getBlock();
            if (block instanceof BlockPressurePlateBase) {
                if (block.getX() == start.getX() && block.getY() == start.getY() && block.getZ() == start.getZ())
                    addToParkour(player);
                else if (block.getX() == end.getX() && block.getY() == end.getY() && block.getZ() == end.getZ())
                    endParkour(player);
                else
                    checkCheckpoint(player, block);
            }
        }
    }

    private void endParkour(Player player) { // TODO: GIVE SOULS ON BEATING SCORE
        if(!parkour.containsKey(player.getUniqueId())){
            player.sendMessage(TextFormat.colorize("&a&lThis is the finish line for the parkour! Get to the start line and climb back up here!"));
            return;
        }

        long millis = System.currentTimeMillis() - timer.get(player.getUniqueId());
        String time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

        if(!config.exists(player.getUniqueId().toString())) {
            player.sendMessage("" + GREEN + BOLD + "Congratulations on completing the parkour! You finised in " + YELLOW + time + GREEN + "!");
            player.sendMessage("" + GREEN + BOLD + "Try again to get an even better record!");
            config.set(player.getUniqueId().toString(), millis);
            config.save();
        }

        if(millis > (long) config.get(player.getUniqueId().toString())) {
            long oldtim = (long) config.get(player.getUniqueId().toString());
            String old = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(oldtim),
                    TimeUnit.MILLISECONDS.toSeconds(oldtim) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(oldtim)));
            player.sendMessage(TextFormat.colorize("&a&lYour time &e&l%time &a&ldid not beat your previous record of &e&l%old&a&l!\n" +
                    "&a&lTry again to beat your old record!").replaceAll("%time", time).replaceAll("%old", old));
        }

        if(millis < (long) config.get(player.getUniqueId().toString())) {
            player.sendMessage(TextFormat.colorize("&a&lThat's a new record of &e&l%time&a&l! Try again to get an even\n" +
                    "better record!".replaceAll("%time", time)));
        }
        parkour.remove(player.getUniqueId());
        timer.remove(player.getUniqueId());
    }

    private void checkCheckpoint(Player player, Block block) {
        int x = (int) block.getX();
        int y = (int) block.getY();
        int z = (int) block.getZ();
        Map.Entry<Integer, Location> check = null;
        if(!timer.containsKey(player.getUniqueId()))
            return;
        for (Map.Entry<Integer, Location> checkpoint : checkpoints.entrySet()) {
            if (checkpoint.getValue().getX() == x && checkpoint.getValue().getY() == y && checkpoint.getValue().getZ() == z) {
                check = checkpoint;
            }
        }
        if (check != null) {
            if (!parkour.containsKey(player.getUniqueId()) || !parkour.get(player.getUniqueId()).equals(check.getKey())) {
                player.sendMessage("" + BOLD + GREEN + "You reached " + YELLOW + "Checkpoint #" + Integer.toString(check.getKey()) + GREEN + ". You can type" + YELLOW + " /parkour checkpoint" + GREEN + " to get back to this place");
                parkour.put(player.getUniqueId(), check.getKey());
            }
        }
    }

    private void addToParkour(Player player) {
        if (!parkour.containsKey(player.getUniqueId()))
            player.sendMessage("" + GREEN + BOLD + "Parkour challenge started! Use" + YELLOW + " /parkour reset" + GREEN + " to restart!");
        else player.sendMessage("" + GREEN + BOLD + "Reset your timer to 00:00! Get to the finish line!");
        parkour.put(player.getUniqueId(), 0);
        timer.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
