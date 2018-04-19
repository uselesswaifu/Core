package com.elissamc.core

import cn.nukkit.Player

enum class Settings {
    /**
     * If the music would be played at the end of music.
     */
    ENDROUND_MUSIC,
    /**
     * Chat has sounds, when player name is in it.
     */
    CHAT_SOUNDS;

    fun hasEnabled(player: Player): Boolean {
        return StorageEngine.getProfile(player.uniqueId).getSetting(this)
    }
}