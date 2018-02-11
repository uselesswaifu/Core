package com.elissamc.api.ParticleSystem;

import cn.nukkit.level.Location;
import cn.nukkit.level.particle.FlameParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.NukkitRunnable;

public class AtomEffect extends NukkitRunnable {
    private int radius = 3;
    private double radiusNucleus = 0.2;
    private int particlesNucleus = 10;
    private int particlesOrbital = 10;
    private int orbitals = 3;
    private int rotation = 0;
    private double angularVelocity = Math.PI / 80;
    private int step = 0;
    private final Location location;
    private int time = 0;

    public AtomEffect(Location location) {
        this.location = location;
    }

    @Override
    public void run() {
        time++;
        if(time == 100)
            this.cancel();
        for (int i = 0; i < particlesNucleus; ++i) {
            Vector3 v = new Vector3(0.1, 0.2, 0.3).multiply(radius * radiusNucleus);
            //nucleusparticle
            location.getLevel().addParticle(new FlameParticle(location.add(v.x, v.y, v.z)));
            location.subtract(v.x, v.y, v.z);
        }
        for (int i = 0; i < particlesOrbital; ++i) {
            double angle = step * angularVelocity;
            for (int j = 0; j < orbitals; ++j) {
                double xRotation = (Math.PI / orbitals) * j;
                Vector3 v = (new Vector3(Math.cos(angle), Math.sin(angle), 0)).multiply(radius);
                VectorUtils.rotateAroundAxisX(v, xRotation);
                VectorUtils.rotateAroundAxisY(v, rotation);
                //orbitalparticle
                location.getLevel().addParticle(new FlameParticle(location.add(v.x, v.y, v.z)));
                location.subtract(v.x, v.y, v.z);
            }
            ++step;
        }

    }

}
