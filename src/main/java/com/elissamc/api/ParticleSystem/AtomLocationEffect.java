package com.elissamc.api.ParticleSystem;


import cn.nukkit.level.Location;
import cn.nukkit.level.particle.FlameParticle;
import cn.nukkit.level.particle.LavaDripParticle;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.level.particle.WaterDripParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.NukkitRunnable;
import com.elissamc.api.ParticleSystem.util.RandomUtils;

public class AtomLocationEffect extends NukkitRunnable {

    private final Location location;
    /**
     * ParticleType of the nucleus
     */
    public Particle particleNucleus;

    /**
     * ParticleType of orbitals
     */
    public Particle particleOrbital;

    /**
     * Radius of the atom
     */
    public double radius = 3;

    /**
     * Radius of the nucleus as a fraction of the atom-radius
     */
    public float radiusNucleus = .2f;

    /**
     * Particles to be spawned in the nucleus per iteration
     */
    public int particlesNucleus = 400;

    /**
     * Particles to be spawned per orbital per iteration
     */
    public int particlesOrbital = 400;

    /**
     * Orbitals around the nucleus
     */
    public int orbitals = 3;

    /**
     * Rotation around the Y-axis
     */
    public double rotation = 0;

    /**
     * Velocity of the orbitals
     */
    public double angularVelocity = Math.PI / 80d;

    /**
     * Internal counter
     */
    protected int step = 0;


    public AtomLocationEffect(Location location){
        this.location = new Location(location.x, location.y, location.z, location.level);
        particleNucleus = new FlameParticle(location);
        particleOrbital = new FlameParticle(location);
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

        Location location = getLocation();
        for (int i = 0; i < particlesNucleus; ++i) {
            Vector3 v = RandomUtils.getRandomVector().multiply(radius * radiusNucleus);
            location.level.addParticle(new WaterDripParticle(location.add(v.x, v.y, v.z)));
            location.subtract(v);
        }
        for (int i = 0; i < particlesOrbital; ++i) {
            double angle = step * angularVelocity;
            for (int j = 0; j < orbitals; j++) {
                double xRotation = (Math.PI / orbitals) * j;
                Vector3 v = new Vector3(Math.cos(angle), Math.sin(angle), 0).multiply(radius);
                VectorUtils.rotateAroundAxisX(v, xRotation);
                VectorUtils.rotateAroundAxisY(v, rotation);
                location.level.addParticle(new LavaDripParticle(location.add(v.x, v.y, v.z)));
                location.subtract(v);
            }
            step++;
        }
    }

    public Location getLocation() {
        return location;
    }
}