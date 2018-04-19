package com.elissamc.particle.util

import cn.nukkit.block.Block
import cn.nukkit.math.Vector3

import java.util.Random

object RandomUtils {

    val random = Random(System.nanoTime())

    val randomVector: Vector3
        get() {
            val x: Double = random.nextDouble() * 2 - 1
            val y: Double = random.nextDouble() * 2 - 1
            val z: Double = random.nextDouble() * 2 - 1

            return Vector3(x, y, z).normalize()
        }

    val randomCircleVector: Vector3
        get() {
            val rnd: Double = random.nextDouble() * 2.0 * Math.PI
            val x: Double
            val z: Double
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