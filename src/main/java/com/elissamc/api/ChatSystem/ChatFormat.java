package com.elissamc.api.ChatSystem;

public enum ChatFormat{
    DEFAULT("&7%name: %msg"), VIP("&a[VIP] %name: &f%msg"), VIPPLUS("&a[VIP&6+&a] %name: &f%msg"),
    HELPER("&9[HELPER] %name: &f%msg"), MOD("&2[MOD] %name: &f%msg"), BUILDTEAM("&3[BUILD TEAM] %name: &f%msg"),
    ADMIN("&c[ADMIN] %name: &f%msg"), OWNER("&c[OWNER] %name: &f%msg"), YT("&6[YT] %name: &f%msg");

    private String group;

    ChatFormat(String g){
        this.group = g;
    }

    public String toString() {
        return this.group;
    }
}
