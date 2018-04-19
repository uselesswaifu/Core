package com.elissamc.particle


import cn.nukkit.level.Location
import cn.nukkit.level.particle.FlameParticle
import cn.nukkit.level.particle.LavaDripParticle
import cn.nukkit.level.particle.Particle
import cn.nukkit.level.particle.WaterDripParticle
import cn.nukkit.math.Vector3
import cn.nukkit.scheduler.NukkitRunnable
import com.elissamc.particle.util.RandomUtils

class AtomLocationEffect(location: Location) : NukkitRunnable() {

    val location: Location
    /**
     * ParticleType of the nucleus
     */
    var particleNucleus: Particle

    /**
     * ParticleType of orbitals
     */
    var particleOrbital: Particle

    /**
     * Radius of the atom
     */
    var radius = 3.0

    /**
     * Radius of the nucleus as a fraction of the atom-radius
     */
    var radiusNucleus = .2f

    /**
     * Particles to be spawned in the nucleus per iteration
     */
    var particlesNucleus = 400

    /**
     * Particles to be spawned per orbital per iteration
     */
    var particlesOrbital = 400

    /**
     * Orbitals around the nucleus
     */
    var orbitals = 3

    /**
     * Rotation around the Y-axis
     */
    var rotation = 0.0

    /**
     * Velocity of the orbitals
     */
    var angularVelocity = Math.PI / 80.0

    /**
     * Internal counter
     */
    protected var step = 0


    init {
        this.location = Location(location.x, location.y, location.z, location.level)
        particleNucleus = FlameParticle(location)
        particleOrbital = FlameParticle(location)
    }

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

        val location = location
        for (i in 0 until particlesNucleus) {
            val v = RandomUtils.randomVector.multiply(radius * radiusNucleus)
            location.level.addParticle(WaterDripParticle(location.add(v.x, v.y, v.z)))
            location.subtract(v)
        }
        for (i in 0 until particlesOrbital) {
            val angle = step * angularVelocity
            for (j in 0 until orbitals) {
                val xRotation = Math.PI / orbitals * j
                val v = Vector3(Math.cos(angle), Math.sin(angle), 0.0).multiply(radius)
                VectorUtils.rotateAroundAxisX(v, xRotation)
                VectorUtils.rotateAroundAxisY(v, rotation)
                location.level.addParticle(LavaDripParticle(location.add(v.x, v.y, v.z)))
                location.subtract(v)
            }
            step++
        }
    }
}