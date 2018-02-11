package com.elissamc.player.event;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.utils.TextFormat;
import com.elissamc.api.PartySystem.PartySystem;
import com.elissamc.ElissaMC;

import java.util.Objects;

public class PartyChatEventBC implements Listener {

    @EventHandler
    public void onChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        if(PartySystem.IsInParty(player)){
            if(ElissaMC.plugin.getInstance().party.partychat.containsKey(player.getName()))
                if(ElissaMC.plugin.getInstance().party.partychat.get(player.getName())){
                    event.setCancelled();
                    String msg = TextFormat.YELLOW + TextFormat.BOLD.toString() + "Party » " + TextFormat.RESET + player.getDisplayName() + TextFormat.GRAY + " :" + event.getMessage();
                    for(Player member : Objects.requireNonNull(PartySystem.getPartyMembers(player))){
                        member.sendMessage(msg);
                    }
                }
        }
    }
}