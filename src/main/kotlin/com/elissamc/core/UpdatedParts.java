package com.elissamc.core;

import jline.internal.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that turns off all parts when needed. Idk what is this...
 *
 * @author Mato Kormuth
 *
 */
public class UpdatedParts {
    private static List<Updatable> parts = new ArrayList<Updatable>();

    public static void shutdown() {
        for (Updatable part : UpdatedParts.parts)
            part.updateStop();
        UpdatedParts.parts.clear();
    }

    public static void registerPart(final Updatable part) {
        Log.info("[UpdatedParts] Registering: " + part.getClass().getSimpleName());
        UpdatedParts.parts.add(part);
    }
}