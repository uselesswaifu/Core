// @formatter:off
/*
 * Pexel Project - Minecraft minigame server platform.
 * Copyright (C) 2014 Matej Kormuth <http://www.matejkormuth.eu>
 *
 * This file is part of Pexel.
 *
 * Pexel is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Pexel is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
// @formatter:on
package com.elissamc.core;

import java.util.UUID;

import com.elissamc.ElissaMC;

/**
 * Class used for generating paths.
 *
 * @author Mato Kormuth
 *
 */
public class Paths {
    /**
     * Returns path for player profile.
     *
     * @param uuid
     * @return
     */
    public static final String playerProfile(final UUID uuid) {
        return ElissaMC.folder.getAbsolutePath() + "/profiles/"
                + uuid.toString() + ".yml";
    }

    public static String lobbiesPath() {
        return ElissaMC.folder.getAbsolutePath() + "/lobbies.yml";
    }

    public static String gatesPath() {
        return ElissaMC.folder.getAbsolutePath() + "/gates.yml";
    }

    public static String msuCache() {
        return ElissaMC.folder.getAbsolutePath() + "/msu.cache";
    }

    public static String matchRecord(final String name) {
        return ElissaMC.folder.getAbsolutePath() + "/records/" + name
                + ".record";
    }

    public static String cache(final String name) {
        return ElissaMC.folder.getAbsolutePath() + "/cache/" + name
                + ".cache";
    }

    public static String clips() {
        return ElissaMC.folder.getAbsolutePath() + "/clips/";
    }
}