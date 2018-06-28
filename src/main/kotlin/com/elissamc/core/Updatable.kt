package com.elissamc.core

/**
 * Interface that suggests that this call is updatable.
 */
interface Updatable {
    /**
     * Called when part should start it's update logic.
     */
    fun updateStart()

    /**
     * Called when part should stop it's update logic.
     */
    fun updateStop()
}