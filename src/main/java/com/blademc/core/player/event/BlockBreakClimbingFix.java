package com.blademc.player.event;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.level.Location;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;

import java.util.HashMap;
import java.util.Map;

public class BlockBreakClimbingFix implements Listener {

    private Map<String, Location> positions = new HashMap<>();

    @EventHandler
    public void onRecieve(DataPacketReceiveEvent event){
        Player player = event.getPlayer();
        if(event.getPacket() instanceof PlayerActionPacket){
            PlayerActionPacket pk = (PlayerActionPacket) event.getPacket();
            if(pk.action == PlayerActionPacket.ACTION_START_BREAK){
                positions.put(player.getName(), player.getLocation());
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.isCancelled()){
            Player player = event.getPlayer();
            if(positions.containsKey(player.getName())){
                Location pos = positions.get(player.getName());
                this.revert(player, pos);
            }
        }
    }

    public void revert(Player player, Location pos){
        MovePlayerPacket pk = new MovePlayerPacket();
        pk.x = (float) pos.x;
        pk.y = (float) pos.y;
        pk.z = (float) pos.z;
        pk.yaw = (float) pos.yaw;
        pk.headYaw = (float) pos.yaw;
        pk.pitch = (float) pos.pitch;
        pk.mode = MovePlayerPacket.MODE_NORMAL;
        pk.eid = 0;
        player.dataPacket(pk);
    }
}
