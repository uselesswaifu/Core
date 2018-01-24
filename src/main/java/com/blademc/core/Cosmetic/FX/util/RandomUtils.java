package com.blademc.core.Cosmetic.FX.util;

import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;

import java.util.Random;

public final class RandomUtils {

    public static final Random random = new Random(System.nanoTime());

    private RandomUtils() {
        // No instance allowed
    }

    public static Vector3 getRandomVector() {
        double x, y, z;
        x = random.nextDouble() * 2 - 1;
        y = random.nextDouble() * 2 - 1;
        z = random.nextDouble() * 2 - 1;

        return new Vector3(x, y, z).normalize();
    }

    public static Vector3 getRandomCircleVector() {
        double rnd, x, z;
        rnd = random.nextDouble() * 2 * Math.PI;
        x = Math.cos(rnd);
        z = Math.sin(rnd);

        return new Vector3(x, 0, z);
    }

    public static Block getRandomMaterial(Block[] materials) {
        return materials[random.nextInt(materials.length)];
    }

    public static double getRandomAngle() {
        return random.nextDouble() * 2 * Math.PI;
    }

}