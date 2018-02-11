package com.elissamc.api.ParticleSystem;

import cn.nukkit.level.Location;
import cn.nukkit.level.particle.FlameParticle;
import cn.nukkit.scheduler.NukkitRunnable;

public class WeirdEffect extends NukkitRunnable {

    private Location loc;

    public WeirdEffect(Location loc) {
        this.loc = loc;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        int strands = 4;
        int particles = 40;
        int radius = 1;
        int curve = 1;
        int rotation = (int) Math.PI / 4;
            Location location = loc;
            for (int i = 1; i <= strands; ++i) {
                for (int j = 1; j <= particles; ++j) {
                    float ratio = (float) j / particles;
                    double angle = curve * ratio * 2 * Math.PI / strands + (2 * Math.PI * i / strands) + rotation;
                    double x = Math.cos(angle) * ratio * radius;
                    double z = Math.sin(angle) * ratio * radius;
                    location.getLevel().addParticle(new FlameParticle(location.add(x, 0, z)));
                    location.subtract(x, 0, z);
                }
            }
    }
}
