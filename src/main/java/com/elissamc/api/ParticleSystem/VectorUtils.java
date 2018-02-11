package com.elissamc.api.ParticleSystem;

import cn.nukkit.math.Vector3;

public class VectorUtils {

    public static Vector3 rotateAroundAxisX(Vector3 v, double angle){
        double y = 0; double z = 0; double cos = 0; double sin = 0;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setComponents(v.x, y, z);
    }
    public static Vector3 rotateAroundAxisY(Vector3 v, double angle){
        double x = 0; double z = 0; double cos = 0; double sin = 0;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setComponents(x, v.y, z);
    }

}
