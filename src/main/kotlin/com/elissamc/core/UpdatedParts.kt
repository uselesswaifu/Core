package com.elissamc.core

import jline.internal.Log

import java.util.ArrayList

/** Class that turns off all parts when needed. */
object UpdatedParts {
    private val parts = ArrayList<Updatable>()

    fun shutdown() {
        for (part in UpdatedParts.parts)
            part.updateStop()
        UpdatedParts.parts.clear()
    }

    fun registerPart(part: Updatable) {
        Log.info("[UpdatedParts] Registering: " + part.javaClass.simpleName)
        UpdatedParts.parts.add(part)
    }
}