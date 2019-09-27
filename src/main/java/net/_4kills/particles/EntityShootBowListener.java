package net._4kills.particles;

import com.comphenix.protocol.wrappers.WrappedParticle;
import net._4kills.particles.effect.AbstractParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.ejml.data.DMatrix3;

import net._4kills.particles.packet.WrapperPlayServerWorldParticles;


import java.util.Collection;

import static org.ejml.dense.fixed.CommonOps_DDF3.addEquals;
import static org.ejml.dense.fixed.CommonOps_DDF3.scale;

public class EntityShootBowListener implements Listener {
    private ParticlesPluginHook plugin;

    double n0x;
    double n0y;
    double n0z;

    double dirx_;// direction of arrow
    double diry_;
    double dirz_;

    private EntityShootBowListener(ParticlesPluginHook plugin) {
        this.plugin = plugin;
    }

    public static void register(ParticlesPluginHook plugin) {
        plugin.getServer().getPluginManager().registerEvents(new EntityShootBowListener(plugin), plugin);
    }

    private class DoubleHelixParticleEffect extends AbstractParticleEffect {
        private final Collection<? extends org.bukkit.entity.Player> toPlayers;

        final Vector[] direction = new Vector[1];
        final Location[] position = new Location[1];

        final double[] x = new double[1];
        final double[] y = new double[1];
        final double[] z = new double[1];

        final double theta = 0.2 * Math.PI;

        final double[] nx = {0};
        final double[] ny = {0.5};
        final double[] nz = {0};

        final CraftArrow arrow;



        DoubleHelixParticleEffect(Collection<? extends org.bukkit.entity.Player> toPlayers,
                                  Plugin plugin, CraftArrow arrow) {
            super(toPlayers, plugin);
            this.toPlayers = toPlayers;
            this.arrow = arrow;
            direction[0] = arrow.getLocation().getDirection();
            position[0] = arrow.getLocation();

            x[0] = position[0].getX();
            y[0] = position[0].getY() + 0.5;
            z[0] = position[0].getZ();

            //draw(Particle.WATER_DROP, (float)x[0], (float)y[0], (float)z[0]);

            this.runTaskTimer(plugin, 1, 1);

        }

        @Override
        public void run() {
            if (arrow.isInBlock()) return;

            double nxt = nx[0];
            double nyt = ny[0];
            double nzt = nz[0];

            nx[0] = nxt;
            ny[0] = nyt * Math.cos(theta) - nzt * Math.sin(theta);
            nz[0] = nyt * Math.sin(theta) + nzt * Math.cos(theta);

            position[0] = arrow.getLocation();

            x[0] = nx[0] + position[0].getX();
            y[0] = ny[0] + position[0].getY();
            z[0] = nz[0] + position[0].getZ();

            DMatrix3 loc = new DMatrix3(x[0], y[0],z[0]);
            DMatrix3 norm = new DMatrix3(nx[0], ny[0], nz[0]);
            scale(-2, norm);
            addEquals(norm, loc);
            draw(Particle.SPELL_WITCH, norm, 1);

            draw(Particle.DRIP_WATER, loc, 1);
        }


    }
    /*
    private Vector normalVec(double a, double b, double c, double x, double y, double z)
    {
        x = b * z - c * y;
        y = c*x - a*z;
        z = a*y - b*x;
        return new Vector(x,y,z);
    }

    private double angleBetweenVectors(double a,double b,double c,double x,double y,double z)
    {
        return Math.acos((a*x+b*y+c*z)/(Math.sqrt(a*a+b*b+c*c)*Math.sqrt(x*x+y*y+z*z)));
    }

    private Vector rotateAboutOrigin(double x,double y,double z,double u,double v,double w,double theta) // parameters are doubles
    {
        double sub1 = (u*x+v*y+w*z) * (1-Math.cos(theta));
        double sub2 = u*u+v*v+w*w;
        x = (u*sub1+sub2*x*Math.cos(theta)+Math.sqrt(sub2)*(-w*y+v*z)*Math.sin(theta))/sub2;
        y = (v*sub1+sub2*y*Math.cos(theta)+Math.sqrt(sub2)*(w*x-u*z)*Math.sin(theta))/sub2;
        z = (w*sub1+sub2*z*Math.cos(theta)+Math.sqrt(sub2)*(-v*x+u*y)*Math.sin(theta))/sub2;
        return new Vector(x,y,z);
    }*/



    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        final Entity entity = event.getProjectile();
        System.out.println("stop");
        if (!(entity instanceof CraftArrow)) return;
        final CraftArrow arrow = (CraftArrow) entity;
        final World world = arrow.getWorld();
        DoubleHelixParticleEffect test =  new DoubleHelixParticleEffect(Bukkit.getOnlinePlayers(), plugin, arrow );
       /* Vector[] direction = {arrow.getLocation().getDirection()};
        Location[] position = {arrow.getLocation()};

        // init code
        double R = 5.0;
        Vector n0 = normalVec(position[0].getX(), position[0].getY(), position[0].getZ(), direction[0].getX(), direction[0].getY(), direction[0].getZ());
        double nl = R / Math.sqrt(n0.getX() * n0.getX() + n0.getY() *n0.getY() + n0.getZ() * n0.getZ());
        n0x = n0.getX() * nl;
        n0y = n0.getY() * nl;
        n0z = n0.getZ() * nl;

        dirx_=direction[0].getX();  // direction of arrow
        diry_=direction[0].getY();
        dirz_=direction[0].getZ();

        double px = position[0].getX() + n0x;  // position of arrow
        double py = position[0].getY() + n0y;
        double pz = position[0].getZ() + n0z;

        world.spawnParticle(Particle.DRIP_WATER, px, py, pz, 1);

        // cyclic code
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (arrow.isInBlock()) return;

            direction[0] = arrow.getLocation().getDirection();
            position[0] = arrow.getLocation();

            double yourAngle= 0.2 * Math.PI; //for example

            // do magic
            double dirx=direction[0].getX();  // direction of arrow
            double diry=direction[0].getY();
            double dirz=direction[0].getZ();
            double alpha = angleBetweenVectors(dirx_,diry_,dirz_, dirx, diry, dirz);
            Vector rotAxis = normalVec(dirx_, diry_, dirz_, n0x, n0y, n0z);
            Vector n1 = rotateAboutOrigin(n0x, n0y, n0z, rotAxis.getX(), rotAxis.getY(), rotAxis.getZ(), alpha);
            n1 = rotateAboutOrigin(n1.getX(), n1.getY(), n1.getZ(), dirx, diry, dirz, yourAngle);

            // save previous values for next method call
            n0x = n1.getX();
            n0y = n1.getY();
            n0z = n1.getZ();
            dirx_ = dirx;
            diry_ = diry;
            dirz_ = dirz;

            //new particle location:
            double pax = position[0].getX() + n1.getX();  // position of arrow
            double pay = position[0].getY() + n1.getY();
            double paz = position[0].getZ() + n1.getZ();

            world.spawnParticle(Particle.DRIP_WATER, pax, pay, paz, 1);
        }, 1, 1);

        // SPAWN YOUR PARTICLE w/ px, py, pz*/
        /*
        final Vector[] direction = {arrow.getLocation().getDirection()};
        final Location[] position = {arrow.getLocation()};

        final double[] a = {position[0].getX()};
        final double[] b = {position[0].getY()};
        final double[] c = {position[0].getZ()};

        final double[] u = {direction[0].getX()};
        final double[] v = {direction[0].getY()};
        final double[] w = {direction[0].getZ()};
        /*
        final double R = 1;
        final double phi = R / Math.sqrt(Math.pow(b[0] * w[0] - c[0] * v[0], 2) + Math.pow(c[0] * u[0] - a[0] * w[0], 2) + Math.pow(a[0] * v[0] - b[0] * u[0], 2));

        final double[] x = {a[0] + phi * (b[0] * w[0] - c[0] * v[0])};
        final double[] y = {b[0] + phi * (c[0] * u[0] - a[0] * w[0])};
        final double[] z = {c[0] + phi * (a[0] * v[0] - b[0] * u[0])};

        final double theta = 0.2 * Math.PI;

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (arrow.isInBlock()) return;

            direction[0] = arrow.getLocation().getDirection();
            position[0] = arrow.getLocation();

            a[0] = position[0].getX();
            b[0] = position[0].getY();
            c[0] = position[0].getZ();

            u[0] = -direction[0].getX();
            v[0] = -direction[0].getY();
            w[0] = direction[0].getZ();

            x[0] = (a[0] * (Math.pow(v[0], 2) + Math.pow(w[0], 2)) - u[0] * (b[0] * v[0] + c[0] * w[0] - u[0] * x[0] - v[0] * y[0] - w[0] * z[0])) * (1 - Math.cos(theta)) + x[0] * Math.cos(theta) + (-c[0] * v[0] + b[0] * w[0] - w[0] * y[0] + v[0] * z[0]) * Math.sin(theta);
            y[0] = (b[0] * (Math.pow(u[0], 2) + Math.pow(w[0], 2)) - v[0] * (a[0] * u[0] + c[0] * w[0] - u[0] * x[0] - v[0] * y[0] - w[0] * z[0])) * (1 - Math.cos(theta)) + y[0] * Math.cos(theta) + (c[0] * u[0] - a[0] * w[0] + w[0] * x[0] - u[0] * z[0]) * Math.sin(theta);
            z[0] = (c[0] * (Math.pow(u[0], 2) + Math.pow(v[0], 2)) - w[0] * (a[0] * u[0] + b[0] * v[0] - u[0] * x[0] - v[0] * y[0] - w[0] * z[0])) * (1 - Math.cos(theta)) + z[0] * Math.cos(theta) + (-b[0] * u[0] + a[0] * v[0] - v[0] * x[0] + u[0] * y[0]) * Math.sin(theta);

            x[0] += u[0]; // -
            y[0] += v[0]; // -
            z[0] += w[0];

            world.spawnParticle(Particle.DRIP_WATER, x[0], y[0], z[0], 1);
        }, 0, 1);
        final Vector[] direction = {arrow.getLocation().getDirection()};
        final Location[] position = {arrow.getLocation()};

        final double[] x = {position[0].getX()};
        final double[] y = {position[0].getY() + 0.5};
        final double[] z = {position[0].getZ()};

        final double theta = 0.2 * Math.PI;
        /*
        double[] nx = {0};
        double[] ny = {1.5};
        double[] nz = {0};

        final double[] nx = {0};
        final double[] ny = {0.5};
        final double[] nz = {0};

        /*Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (arrow.isInBlock()) return;
            System.out.println(Math.sqrt(Math.pow(nx[0],2) + Math.pow(ny[0], 2) + Math.pow(nz[0], 2)));
            System.out.println(theta);
            double nxt = nx[0];
            double nyt = ny[0];
            double nzt = nz[0];

            nx[0] = nxt;
            ny[0] = nyt * Math.cos(theta) - nzt * Math.sin(theta);
            nz[0] = nyt * Math.sin(theta) + nzt * Math.cos(theta);

            System.out.println(Math.sqrt(Math.pow(nx[0],2) + Math.pow(ny[0], 2) + Math.pow(nz[0], 2)));
            System.out.print(" 2");
            position[0] = arrow.getLocation();

            x[0] = nx[0] + position[0].getX();
            y[0] = ny[0] + position[0].getY();
            z[0] = nz[0] + position[0].getZ();

            world.spawnParticle(Particle.SPELL_WITCH, x[0] - 2*nx[0], y[0] - 2*ny[0], z[0] - 2*nz[0], 1);

            world.spawnParticle(Particle.DRIP_WATER, x[0], y[0], z[0], 1);
        }, 0, 1);*/
    }
}