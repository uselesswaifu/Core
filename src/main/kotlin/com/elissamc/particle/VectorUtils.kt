package com.elissamc.particle

import cn.nukkit.math.Vector3

object VectorUtils {

    fun rotateAroundAxisX(v: Vector3, angle: Double): Vector3 {
        val y: Double
        val z: Double
        val cos: Double = Math.cos(angle)
        val sin: Double = Math.sin(angle)
        y = v.getY() * cos - v.getZ() * sin
        z = v.getY() * sin + v.getZ() * cos
        return v.setComponents(v.x, y, z)
    }

    fun rotateAroundAxisY(v: Vector3, angle: Double): Vector3 {
        val x: Double
        val z: Double
        val cos: Double = Math.cos(angle)
        val sin: Double = Math.sin(angle)
        x = v.getX() * cos + v.getZ() * sin
        z = v.getX() * -sin + v.getZ() * cos
        return v.setComponents(x, v.y, z)
    }

}
