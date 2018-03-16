package com.elissamc.api.ChatSystem;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.plugin.service.RegisteredServiceProvider;
import cn.nukkit.utils.TextFormat;
import me.lucko.luckperms.api.LuckPermsApi;

import java.util.Objects;

public class ChatListener implements Listener {

    private LuckPermsApi api;

    public ChatListener() {
        RegisteredServiceProvider<LuckPermsApi> provider = Server.getInstance().getServiceManager().getProvider(LuckPermsApi.class);
        if (provider != null) {
            api = provider.getProvider();

        }
    }

    private boolean contains(String s) {
        for (ChatFormat cf : ChatFormat.values()) {
            if (cf.name().equals(s))
                return true;
        }
        return false;
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String group = Objects.requireNonNull(api.getUser(player.getUniqueId())).getPrimaryGroup().toUpperCase();
        if (!this.contains(group))
            group = "DEFAULT";
        String s = ChatFormat.valueOf(group).toString();
        s = s.replaceAll("%name", player.getName());
        s = s.replaceAll("%msg", event.getMessage());
        s = TextFormat.colorize(s);
        event.setFormat(s);

    }
}
