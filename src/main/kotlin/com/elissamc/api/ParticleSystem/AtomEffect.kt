package com.elissamc.api.ParticleSystem

import cn.nukkit.level.Location
import cn.nukkit.level.particle.FlameParticle
import cn.nukkit.math.Vector3
import cn.nukkit.scheduler.NukkitRunnable

class AtomEffect(private val location: Location) : NukkitRunnable() {
    private val radius = 3
    private val radiusNucleus = 0.2
    private val particlesNucleus = 10
    private val particlesOrbital = 10
    private val orbitals = 3
    private val rotation = 0
    private val angularVelocity = Math.PI / 80
    private var step = 0
    private var time = 0

    override fun run() {
        time++
        if (time == 100)
            this.cancel()
        for (i in 0 until particlesNucleus) {
            val v = Vector3(0.1, 0.2, 0.3).multiply(radius * radiusNucleus)
            //nucleusparticle
            location.getLevel().addParticle(FlameParticle(location.add(v.x, v.y, v.z)))
            location.subtract(v.x, v.y, v.z)
        }
        for (i in 0 until particlesOrbital) {
            val angle = step * angularVelocity
            for (j in 0 until orbitals) {
                val xRotation = Math.PI / orbitals * j
                val v = Vector3(Math.cos(angle), Math.sin(angle), 0.0).multiply(radius.toDouble())
                VectorUtils.rotateAroundAxisX(v, xRotation)
                VectorUtils.rotateAroundAxisY(v, rotation.toDouble())
                //orbitalparticle
                location.getLevel().addParticle(FlameParticle(location.add(v.x, v.y, v.z)))
                location.subtract(v.x, v.y, v.z)
            }
            ++step
        }

    }

}
