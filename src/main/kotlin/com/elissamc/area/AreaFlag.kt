package com.elissamc.area

enum class AreaFlag {
    /**
     * Block break event.
     */
    BLOCK_BREAK,
    /**
     * Block place event.
     */
    BLOCK_PLACE,
    /**
     * Player doing damage.
     */
    PLAYER_DODAMAGE,
    /**
     * Player dropping item/s.
     */
    PLAYER_DROPITEM,
    /**
     * Player getting damage.
     */
    PLAYER_GETDAMAGE,
    /**
     * Starvation of player.
     */
    PLAYER_STARVATION,
    /**
     * Permission for receiving "Permission denied" message.
     */
    AREA_CHAT_PERMISSIONDENIED,
    /**
     * Permission for receiving area welcome message.
     */
    AREA_CHAT_WELCOME,
    /**
     * Permission for receiving area goodbye message.
     */
    AREA_CHAT_GOODBYE
}