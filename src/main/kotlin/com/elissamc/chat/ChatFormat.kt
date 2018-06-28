package com.elissamc.chat

import cn.nukkit.utils.TextFormat

enum class ChatFormat(private val group: String) {
    DEFAULT("&7%name: %msg"), VIP("&a[VIP] %name: &f%msg"), VIPPLUS("&a[VIP&6+&a] %name: &f%msg"),
    HELPER("&9[HELPER] %name: &f%msg"), MOD("&2[MOD] %name: &f%msg"), BUILDTEAM("&3[BUILD TEAM] %name: &f%msg"),
    ADMIN("&c[ADMIN] %name: &f%msg"), OWNER("&c[OWNER] %name: &f%msg"), YT("&6[YT] %name: &f%msg"),

    SUCCESS("" + TextFormat.GREEN + "%msg"), ERROR("" + TextFormat.RED + "%msg");

    override fun toString(): String {
        return this.group
    }

    companion object {
        fun success(s: String): String {
            return ChatFormat.SUCCESS.toString().replace("%msg", s)
        }

        fun error(s: String): String {
            return ChatFormat.ERROR.toString().replace("%msg", s)
        }
    }
}