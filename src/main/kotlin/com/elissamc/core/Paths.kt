package com.elissamc.core

import java.util.UUID

import com.elissamc.ElissaMC

/** Class used for generating paths. */
object Paths {
    /**
     * Returns path for player profile.
     *
     * @param uuid
     * @return
     */
    fun playerProfile(uuid: UUID): String {
        return (ElissaMC.folder.absolutePath + "/profiles/"
                + uuid.toString() + ".yml")
    }

    fun lobbiesPath(): String {
        return ElissaMC.folder.absolutePath + "/lobbies.yml"
    }

    fun gatesPath(): String {
        return ElissaMC.folder.absolutePath + "/gates.yml"
    }

    fun msuCache(): String {
        return ElissaMC.folder.absolutePath + "/msu.cache"
    }

    fun matchRecord(name: String): String {
        return (ElissaMC.folder.absolutePath + "/records/" + name
                + ".record")
    }

    fun cache(name: String): String {
        return (ElissaMC.folder.absolutePath + "/cache/" + name
                + ".cache")
    }

    fun clips(): String {
        return ElissaMC.folder.absolutePath + "/clips/"
    }
}