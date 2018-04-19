package com.elissamc.chat;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import com.elissamc.games.Minigame;

public class ChatManager {
    private static final String minigameFormat = TextFormat.DARK_GREEN
            + "[%minigame%] "
            + TextFormat.WHITE
            + " %msg%";
    private static final String errorFormat = TextFormat.RED
            + "%msg%";
    private static final String successFormat = TextFormat.GREEN
            + "%msg%";
    private static final String chatDefaultFormat = TextFormat.GRAY
            + "%player% > %msg%";
    private static final String chatOpFormat = TextFormat.GOLD
            + "[OP] %player% > %msg%";

    public static final String error(final String msg) {
        return ChatManager.errorFormat.replace("%msg%", msg);
    }

    public static final String success(final String msg) {
        return ChatManager.successFormat.replace("%msg%", msg);
    }

    public static final String chatPlayer(final String msg, final Player player) {
        return ChatManager.chatDefaultFormat.replace("%player%", player.getDisplayName()).replace(
                "%msg%", msg);
    }

    public static final String chatPlayerOp(final String msg, final Player player) {
        return ChatManager.chatOpFormat.replace("%player%", player.getDisplayName()).replace(
                "%msg%", msg);
    }

    public static final String chatPlayerFriend(final String msg, final Player player) {
        return TextFormat.BLUE
                + ChatManager.chatDefaultFormat.replace("%player%",
                player.getDisplayName()).replace("%msg%", msg);
    }

    /**
     * Returns formatted 'minigame' message.
     *
     * @param minigame minigame
     * @param msg      message to format
     * @return formatted message
     */
    public static final String minigame(final Minigame minigame, final String msg) {
        return ChatManager.minigameFormat.replace("%minigame%",
                minigame.getDisplayName()).replace("%msg%", msg);
    }
}