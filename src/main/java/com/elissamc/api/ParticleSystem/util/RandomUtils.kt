package com.elissamc.api.ParticleSystem.util

import cn.nukkit.block.Block
import cn.nukkit.math.Vector3

import java.util.Random

object RandomUtils {

    val random = Random(System.nanoTime())

    val randomVector: Vector3
        get() {
            val x: Double
            val y: Double
            val z: Double
            x = random.nextDouble() * 2 - 1
            y = random.nextDouble() * 2 - 1
            z = random.nextDouble() * 2 - 1

            return Vector3(x, y, z).normalize()
        }

    val randomCircleVector: Vector3
        get() {
            val rnd: Double
            val x: Double
            val z: Double
            rnd = random.nextDouble() * 2.0 * Math.PI
            x = Math.cos(rnd)
            z = Math.sin(rnd)

            return Vector3(x, 0.0, z)
        }

    val randomAngle: Double
        get() = random.nextDouble() * 2.0 * Math.PI

    fun getRandomMaterial(materials: Array<Block>): Block {
        return materials[random.nextInt(materials.size)]
    }

}// No instance allowed