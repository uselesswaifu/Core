package com.elissamc.chat

import cn.nukkit.utils.TextFormat
import com.elissamc.games.Minigame

object ChatManager {
    private val minigameFormat = TextFormat.DARK_GREEN.toString() + "[%minigame%] " + TextFormat.WHITE + " %msg%"
    private val errorFormat = TextFormat.RED.toString() + "%msg%"
    private val successFormat = TextFormat.GREEN.toString() + "%msg%"

    fun error(msg: String): String {
        return ChatManager.errorFormat.replace("%msg%", msg)
    }

    fun success(msg: String): String {
        return ChatManager.successFormat.replace("%msg%", msg)
    }

}