package com.elissamc.core;

import cn.nukkit.Player;

public enum Settings {
    /**
     * If the music would be played at the end of music.
     */
    ENDROUND_MUSIC,
    /**
     * Chat has sounds, when player name is in it.
     */
    CHAT_SOUNDS;

    public boolean hasEnabled(final Player player) {
        return StorageEngine.getProfile(player.getUniqueId()).getSetting(this);
    }
}