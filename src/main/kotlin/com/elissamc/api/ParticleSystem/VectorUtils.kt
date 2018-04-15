package com.elissamc.api.ParticleSystem

import cn.nukkit.math.Vector3

object VectorUtils {

    fun rotateAroundAxisX(v: Vector3, angle: Double): Vector3 {
        var y = 0.0
        var z = 0.0
        var cos = 0.0
        var sin = 0.0
        cos = Math.cos(angle)
        sin = Math.sin(angle)
        y = v.getY() * cos - v.getZ() * sin
        z = v.getY() * sin + v.getZ() * cos
        return v.setComponents(v.x, y, z)
    }

    fun rotateAroundAxisY(v: Vector3, angle: Double): Vector3 {
        var x = 0.0
        var z = 0.0
        var cos = 0.0
        var sin = 0.0
        cos = Math.cos(angle)
        sin = Math.sin(angle)
        x = v.getX() * cos + v.getZ() * sin
        z = v.getX() * -sin + v.getZ() * cos
        return v.setComponents(x, v.y, z)
    }

}
