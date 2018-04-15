package com.elissamc.api.ParticleSystem

import cn.nukkit.level.Location
import cn.nukkit.level.particle.FlameParticle
import cn.nukkit.scheduler.NukkitRunnable

class WeirdEffect(private val loc: Location) : NukkitRunnable() {

    /**
     * When an object implementing interface `Runnable` is used
     * to create a thread, starting the thread causes the object's
     * `run` method to be called in that separately executing
     * thread.
     *
     *
     * The general contract of the method `run` is that it may
     * take any action whatsoever.
     *
     * @see Thread.run
     */
    override fun run() {
        val strands = 4
        val particles = 40
        val radius = 1
        val curve = 1
        val rotation = Math.PI.toInt() / 4
        val location = loc
        for (i in 1..strands) {
            for (j in 1..particles) {
                val ratio = j.toFloat() / particles
                val angle = curve.toDouble() * ratio.toDouble() * 2.0 * Math.PI / strands + 2.0 * Math.PI * i.toDouble() / strands + rotation.toDouble()
                val x = Math.cos(angle) * ratio.toDouble() * radius.toDouble()
                val z = Math.sin(angle) * ratio.toDouble() * radius.toDouble()
                location.getLevel().addParticle(FlameParticle(location.add(x, 0.0, z)))
                location.subtract(x, 0.0, z)
            }
        }
    }
}
